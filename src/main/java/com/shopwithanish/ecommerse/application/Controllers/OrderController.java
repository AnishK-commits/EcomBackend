package com.shopwithanish.ecommerse.application.Controllers;

import com.shopwithanish.ecommerse.application.AllAboutSecurity.AuthUtil;
import com.shopwithanish.ecommerse.application.AppConstants;
import com.shopwithanish.ecommerse.application.Enums.OrderStatus;
import com.shopwithanish.ecommerse.application.Enums.PaymentMethod;
import com.shopwithanish.ecommerse.application.Model.OrdersPaginationResponce;
import com.shopwithanish.ecommerse.application.Model.Users;
import com.shopwithanish.ecommerse.application.ResponceDtos.OrderResponseDto;
import com.shopwithanish.ecommerse.application.Services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    AuthUtil authUtil;

    //placing an order
    @PostMapping("/place-an-order/addressId-{addressId}/paymentMethod-{paymentMethod}")
    ResponseEntity<?> place_an_order(@PathVariable Long addressId , @Valid @PathVariable PaymentMethod paymentMethod){


        Users currentUser = authUtil.LoggedInUser();
        OrderResponseDto orderResponseDto= orderService.place_an_order(addressId , currentUser ,paymentMethod );

       return new ResponseEntity<>(orderResponseDto , HttpStatus.OK);
    }

    @GetMapping("/orders/my-orders")
    public ResponseEntity<?> getMyOrders() {
        Users currentUser = authUtil.LoggedInUser();
        return ResponseEntity.ok(orderService.getOrdersForLoggedInUser(currentUser.getEmail()));
    }

    @GetMapping("/admin/orders/get-all-orders-in-database")
    public ResponseEntity<OrdersPaginationResponce> getAllOrders( @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                  @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                  @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BYORDERS, required = false) String sortBy,
                                                                  @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder)
    {

        return ResponseEntity.ok(orderService.getOrdersInWholeDataBase(pageNumber, pageSize, sortBy, sortOrder ));
    }

    @GetMapping("/admin/get-Order-Detail-from-orderId/{orderId}")
    public  ResponseEntity<OrderResponseDto> getOrderDetailFromOrderId(@PathVariable Long orderId){

        OrderResponseDto dto=orderService.getOrderDetailFromOrderId(orderId);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/admin/update-order-status-from-orderId/{orderId}/{status}")
    public  ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long orderId , @PathVariable OrderStatus status){

        OrderResponseDto dto=orderService.updateOrderStatus(orderId , status);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/seller/orders/get-all-seller-orders-in-database")
    public ResponseEntity<OrdersPaginationResponce> getAllOrdersofSeller( @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                  @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                  @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BYORDERS, required = false) String sortBy,
                                                                  @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder)
    {

        return ResponseEntity.ok(orderService.getAllOrdersofSeller(pageNumber, pageSize, sortBy, sortOrder ));
    }

    @GetMapping("/seller/get-Order-Detail-from-orderId/{orderId}")
    public  ResponseEntity<OrderResponseDto> getOrderDetailFromOrderIdlSELLERView(@PathVariable Long orderId){

        OrderResponseDto dto=orderService.getOrderDetailFromOrderIdlSELLERView(orderId);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/seller/update-order-status-from-orderId/{orderId}/{status}")
    public  ResponseEntity<OrderResponseDto> updateOrderStatusSellerView(@PathVariable Long orderId , @PathVariable OrderStatus status){

        OrderResponseDto dto=orderService.updateOrderStatus(orderId , status);
        return ResponseEntity.ok(dto);
    }

}
