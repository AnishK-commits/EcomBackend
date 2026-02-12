package com.shopwithanish.ecommerse.application.Controllers;


import com.shopwithanish.ecommerse.application.ResponceDtos.PaymentResponceDto;
import com.shopwithanish.ecommerse.application.Services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    PaymentService paymentService;


         @PostMapping("/initiate-payment/orderId/{orderid}")
         public ResponseEntity<?> intiatePayment(@PathVariable Long orderid){

           PaymentResponceDto prd= paymentService.intiatePayment(orderid);
                return new ResponseEntity<>(prd , HttpStatus.OK);
         }



        @PostMapping("//payments/cash/success/order/{orderId}")
        public ResponseEntity<?> markCashPaymentSuccess(@PathVariable Long orderId) {

            paymentService.markCashPaymentSuccess(orderId);

            return ResponseEntity.ok("Cash payment collected successfully");
        }


}
