package com.shopwithanish.ecommerse.application.ServiceImplementations;

import com.shopwithanish.ecommerse.application.AllCustomExceptions.ApiException;
import com.shopwithanish.ecommerse.application.Enums.AppPaymentStatus;
import com.shopwithanish.ecommerse.application.Enums.PaymentGatewaySTATUS;
import com.shopwithanish.ecommerse.application.Enums.PaymentMethod;
import com.shopwithanish.ecommerse.application.Model.Order;
import com.shopwithanish.ecommerse.application.Model.Payment;
import com.shopwithanish.ecommerse.application.Repository.OrderRepository;
import com.shopwithanish.ecommerse.application.Repository.PaymentRepository;
import com.shopwithanish.ecommerse.application.ResponceDtos.PaymentResponceDto;
import com.shopwithanish.ecommerse.application.Services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpL implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;


    @Override
    public PaymentResponceDto intiatePayment(Long orderid) {

       Order orderPlacedINDB = orderRepository.findById(orderid).orElseThrow(()-> new ApiException("Order not found with this id: "+orderid));


        // COD does not need online payment
        if (orderPlacedINDB.getPaymentMethod()== PaymentMethod.CASH) {
            throw new ApiException("Cash on delivery does not require online payment");
        }

      Payment payment=  paymentRepository.findPaymentByOrderId(orderid).orElse(null);
        if(payment!=null){
            //means exist in db payment already done no need of double payment
            throw new ApiException("Payment already initiated for this order");
        }
        //if not create payment obj

        Payment payment1=new Payment();
        payment1.setInitiatedAt(LocalDateTime.now());
        payment1.setPaymentMethod(orderPlacedINDB.getPaymentMethod());
        payment1.setAmount(orderPlacedINDB.getTotalAmount());
        payment1.setPaymentStatus(AppPaymentStatus.PENDING);
        payment1.setPgName("RAZORPAY"); // or STRIPE, PAYPAL
        payment1.setOrder(orderPlacedINDB);

        paymentRepository.save(payment1);

        // Here you call Razorpay / Stripe API
        // and get gatewayPaymentId n msg n responce  n satus
        //wait 15 days


        return modelMapper.map(payment1, PaymentResponceDto.class);
    }
}
