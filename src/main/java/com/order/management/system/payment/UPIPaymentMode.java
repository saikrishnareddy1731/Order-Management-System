package com.order.management.system.payment;

public class UPIPaymentMode implements PaymentMode {

    @Override
    public boolean makePayment() {
        return true;
    }
}
