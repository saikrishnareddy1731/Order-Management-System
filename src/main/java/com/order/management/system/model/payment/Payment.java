package com.order.management.system.model.payment;


import com.order.management.system.payment.PaymentMode;

public class Payment {

   PaymentMode paymentMode;

   public Payment(PaymentMode paymentMode){
       this.paymentMode = paymentMode;
   }

    public boolean makePayment(){
        return paymentMode.makePayment();
    }

}
