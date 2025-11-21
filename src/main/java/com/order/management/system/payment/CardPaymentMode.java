package com.order.management.system.payment;

public class CardPaymentMode implements PaymentMode {

    @Override
    public boolean makePayment() {

        return true;
    }
}
