package com.shopwithanish.ecommerse.application.ServiceImplementations;

import com.shopwithanish.ecommerse.application.AllAboutSecurity.AuthUtil;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpL implements OrderService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final AuthUtil authUtil;

    @Value("${image.base.url}")
    private String image_base_url;

    // ============================================================
    // PLACE ORDER
    // ============================================================

    @Override
    @Transactional
    public OrderResponseDto place_an_order(Long addressId, Users currentUser, PaymentMethod paymentMethod) {

        Addresss address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ApiException("Address not found"));

        Cart cart = cartRepository.findCartByEmail(currentUser.getEmail())
                .orElseThrow(() -> new ApiException("Cart not found"));

        if (cart.getCartItemList() == null || cart.getCartItemList().isEmpty()) {
            throw new ApiException("Cart is empty");
        }

        Order order = new Order();
        order.setEmail(currentUser.getEmail());
        order.setUser(currentUser);
        order.setOrderDate(LocalDateTime.now());
        order.setPaymentMethod(paymentMethod);
        order.setAddresss(address);
        order.setOrderStatus(paymentMethod == PaymentMethod.CASH
                ? OrderStatus.CONFIRMED
                : OrderStatus.PLACED);

        double totalAmount = 0.0;

        for (CartItem cartItem : cart.getCartItemList()) {

            Product product = cartItem.getProduct();

            if (product.getStock() < cartItem.getQuantity()) {
                throw new ApiException("Insufficient stock for " + product.getProductName());
            }

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setOrderItemQuantity(cartItem.getQuantity());
            orderItem.setOrderItemPrice(cartItem.getProductsPrice());
            orderItem.setSeller(product.getSeller());

            order.getOrderItemList().add(orderItem);

            totalAmount += orderItem.getOrderItemPrice() * orderItem.getOrderItemQuantity();
        }

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        cart.getCartItemList().clear();
        cart.setTotalCartPrice(0.0);
        cartRepository.save(cart);

        return buildOrderResponse(savedOrder, false, null);
    }

    // ============================================================
    // SELLER VIEW (FILTER ITEMS)
    // ============================================================

    @Override
    public OrdersPaginationResponce getAllOrdersofSeller(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Users seller = authUtil.LoggedInUser();

        Page<Order> orderPage =
                orderRepository.findOrdersBySeller(seller.getUserid(), pageable);

        List<OrderResponseDto> content = orderPage.getContent()
                .stream()
                .map(order -> buildOrderResponse(order, true, seller.getUserid()))
                .collect(Collectors.toList());

        OrdersPaginationResponce response = new OrdersPaginationResponce();
        response.setContent(content);
        response.setPageNumber(orderPage.getNumber());
        response.setPageSize(orderPage.getSize());
        response.setTotalPages(orderPage.getTotalPages());
        response.setTotalElements((int) orderPage.getTotalElements());
        response.setLastPage(orderPage.isLast());

        return response;
    }

    // ============================================================
    // CUSTOMER VIEW (NO FILTER)
    // ============================================================

    @Override
    public List<OrderResponseDto> getOrdersForLoggedInUser(String email) {

        List<Order> orders = orderRepository.findByEmailOrderByOrderDateDesc(email);

        return orders.stream()
                .map(order -> buildOrderResponse(order, false, null))
                .collect(Collectors.toList());
    }

    // ============================================================
    // COMMON DTO BUILDER
    // ============================================================

    private OrderResponseDto buildOrderResponse(
            Order order,
            boolean isSellerView,
            Long sellerUserId) {

        OrderResponseDto dto = new OrderResponseDto();

        dto.setOrderId(order.getOrderId());
        dto.setOrderDate(order.getOrderDate());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setAddressId(order.getAddresss().getAddressid());

        List<OrderItem> items;

        if (isSellerView) {
            items = order.getOrderItemList().stream()
                    .filter(oi -> oi.getSeller() != null &&
                            oi.getSeller().getUserid().equals(sellerUserId))
                    .toList();
        } else {
            items = order.getOrderItemList();
        }

        List<OrderItemResponseDto> itemDtos = new ArrayList<>();

        for (OrderItem item : items) {

            OrderItemResponseDto itemDto = new OrderItemResponseDto();
            itemDto.setOrderItemId(item.getOrderItemId());
            itemDto.setOrderItemQuantity(Math.toIntExact(item.getOrderItemQuantity()));
            itemDto.setOrderItemPrice(item.getOrderItemPrice());

            Product product = item.getProduct();

            ProductResponceDto productDto = new ProductResponceDto();
            productDto.setProductId(product.getProductId());
            productDto.setProductName(product.getProductName());
            productDto.setDescription(product.getDescription());
            productDto.setSpecialPrice(product.getSpecialPrice());
            productDto.setDiscount(product.getDiscount());
            productDto.setPrice(product.getPrice());
            productDto.setStock(String.valueOf(product.getStock()));
            productDto.setQuantity(item.getOrderItemQuantity());
            productDto.setImage(constructImageUrL(product.getImage()));

            itemDto.setProductResponceDto(productDto);
            itemDtos.add(itemDto);
        }

        dto.setOrderItemResponseDtoList(itemDtos);

        return dto;
    }

    @Transactional
    @Override
    public OrdersPaginationResponce getOrdersInWholeDataBase(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Order> orderPage = orderRepository.findAll(pageable);

        List<OrderResponseDto> content = orderPage.getContent()
                .stream()
                .map(order -> buildOrderResponse(order, false, null)) // 🔥 no seller filter
                .collect(Collectors.toList());

        OrdersPaginationResponce response = new OrdersPaginationResponce();
        response.setContent(content);
        response.setPageNumber(orderPage.getNumber());
        response.setPageSize(orderPage.getSize());
        response.setTotalPages(orderPage.getTotalPages());
        response.setTotalElements((int) orderPage.getTotalElements());
        response.setLastPage(orderPage.isLast());

        return response;
    }

    @Override
    public OrderResponseDto getOrderDetailFromOrderId(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Normal full view (no seller filtering)
        return buildOrderResponse(order, false, null);
    }


    @Override
    public OrderResponseDto getOrderDetailFromOrderIdlSELLERView(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Users seller = authUtil.LoggedInUser();

        return buildOrderResponse(order, true, seller.getUserid());
    }

    @Transactional
    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(status);
        Order updatedOrder = orderRepository.save(order);

        // Normal full view (no seller filtering)
        return buildOrderResponse(updatedOrder, false, null);
    }


    private String constructImageUrL(String image_name) {

        return image_base_url.endsWith("/") ? image_base_url + image_name : image_base_url + "/" + image_name;
    }
}
