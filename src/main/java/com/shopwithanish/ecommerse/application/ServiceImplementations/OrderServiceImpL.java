package com.shopwithanish.ecommerse.application.ServiceImplementations;

import com.shopwithanish.ecommerse.application.AllCustomExceptions.ApiException;
import com.shopwithanish.ecommerse.application.Enums.OrderStatus;
import com.shopwithanish.ecommerse.application.Enums.PaymentMethod;
import com.shopwithanish.ecommerse.application.Model.*;
import com.shopwithanish.ecommerse.application.Repository.*;
import com.shopwithanish.ecommerse.application.ResponceDtos.OrderItemResponseDto;
import com.shopwithanish.ecommerse.application.ResponceDtos.OrderResponseDto;
import com.shopwithanish.ecommerse.application.ResponceDtos.ProductResponceDto;
import com.shopwithanish.ecommerse.application.Services.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpL implements OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final AddressRepository addressRepository;

    @Override
    @Transactional
    public OrderResponseDto place_an_order(Long addressId, Users currentUser, PaymentMethod paymentMethod) {
        //steps

        Addresss addresssinDB= addressRepository.findById(addressId).orElseThrow(()-> new ApiException(" address not found before placing any order plz address "));
        //fetch log user cart
        String email= currentUser.getEmail();
        Cart cart =cartRepository.findCartByEmail(email).orElseThrow(()-> new ApiException( "cart is null/empty"));

        //get all cart-item in cart
        List<CartItem> cartItemList= cart.getCartItemList();
        if(cartItemList.isEmpty()){
            throw new ApiException("no item in cartItem-list");
        }

        //check cart-item avl in stock or not
        for (CartItem cartItem : cartItemList) {
            Product product = cartItem.getProduct();

            if (product.getStock() < cartItem.getQuantity()) {
                throw new ApiException(
                        "Insufficient stock for product: " + product.getProductName());
            }
        }

        //if every thing validate n clear not create order object
        Order order=new Order();
        order.setEmail(email);
        order.setOrderStatus(OrderStatus.PLACED);
        order.setUser(currentUser);
        order.setOrderDate(LocalDateTime.now());

        order.setOrderStatus(OrderStatus.PLACED);

        if (paymentMethod == PaymentMethod.CASH) {
            order.setOrderStatus(OrderStatus.CONFIRMED);
        } else {
            order.setOrderStatus(OrderStatus.PLACED);
        }


        Double totalAmt=0.0; //initial

        for (CartItem cartItem : cartItemList) {
            Product product = cartItem.getProduct();
           //reduce this product stock in DB
            // 50 -5
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product); //save after reduce stock

            //crate order item copy all values of cart item to order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setOrderItemPrice(cartItem.getProductsPrice());
            orderItem.setOrderItemQuantity(cartItem.getQuantity());
            orderItem.setProduct(product);

            order.getOrderItemList().add(orderItem);  //add newUp-to-date order-item to order's list of order-items
            totalAmt+= orderItem.getOrderItemPrice() * orderItem.getOrderItemQuantity();
        }

        order.setTotalAmount(totalAmt);
        Order savedOrder = orderRepository.save(order); //now save this order obj in orderRepo

        //till now order is placed with all detail
        //now clear cart becoz cart-item copied to order-items

        cart.getCartItemList().clear();
        cart.setTotalCartPrice(0.0);
        cartRepository.save(cart);//save changes in cart

        //now savedOrder to orderResponceDto
        OrderResponseDto ordd=fnCallsavedOrder_to_orderResponceDto(savedOrder , addressId , paymentMethod );

        return ordd;


    }

    public OrderResponseDto fnCallsavedOrder_to_orderResponceDto(Order savedOrder, Long addressId , PaymentMethod paymentMethod){

        OrderResponseDto ord=new OrderResponseDto();

        ord.setOrderId(savedOrder.getOrderId());
        ord.setOrderDate(savedOrder.getOrderDate());
        ord.setOrderStatus(savedOrder.getOrderStatus());
        ord.setTotalAmount(savedOrder.getTotalAmount());
        ord.setAddressId(addressId);
        ord.setPaymentMethod(paymentMethod);
        //now
        //orderResponceDto has list of OrderItemResponseDto so we need to create this list and update it manually
        List<OrderItemResponseDto> orderItemResponseDtoList=new ArrayList<>();

        List<OrderItem> orderItemList =savedOrder.getOrderItemList();
        for (OrderItem orderItem : orderItemList) {

            OrderItemResponseDto itemDto = new OrderItemResponseDto();

            itemDto.setOrderItemId(orderItem.getOrderItemId());
            itemDto.setOrderItemQuantity(Math.toIntExact(orderItem.getOrderItemQuantity()));
            itemDto.setOrderItemPrice(orderItem.getOrderItemPrice());



            Product product = orderItem.getProduct();

            ProductResponceDto productDto = new ProductResponceDto();
            productDto.setProductId(product.getProductId());
            productDto.setProductName(product.getProductName());
            productDto.setDescription(product.getDescription());
            productDto.setSpecialPrice(product.getSpecialPrice());
            productDto.setQuantity(orderItem.getOrderItemQuantity());
            productDto.setDiscount(product.getDiscount());
            productDto.setImage(product.getImage());
            productDto.setStock(String.valueOf(product.getStock()));
            productDto.setPrice(product.getPrice());

            itemDto.setProductResponceDto(productDto);
            orderItemResponseDtoList.add(itemDto);
        }


        ord.setOrderItemResponseDtoList(orderItemResponseDtoList);
        //ek set kelya peksha list banvali and direct list add keli/set keli

        return ord;
    }
}
