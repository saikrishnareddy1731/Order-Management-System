package com.order.management.system.model.order;

import com.order.management.system.model.payment.Payment;
import com.order.management.system.model.user.Address;
import com.order.management.system.model.user.User;
import com.order.management.system.model.warehouse.Warehouse;
import com.order.management.system.payment.PaymentMode;
import com.order.management.system.payment.UPIPaymentMode;

import java.util.Map;

public class Order {

    public  User user;
    public Address deliveryAddress;
    public Map<Integer, Integer> productCategoryAndCountMap;
    public Warehouse warehouse;
    public Invoice invoice;
    public Payment payment;
    public OrderStatus orderStatus;

    public Order(User user, Warehouse warehouse){
      this.user = user;
      this.productCategoryAndCountMap = user.getUserCart().getCartItems();
      this.warehouse = warehouse;
      this.deliveryAddress = user.address;
      invoice = new Invoice();
      invoice.generateInvoice(this);
    }

    public void checkout(){

        //1. update inventory
        warehouse.removeItemFromInventory(productCategoryAndCountMap);

        //2. make Payment
        boolean isPaymentSuccess = makePayment(new UPIPaymentMode());

        //3. make cart empty
        if(isPaymentSuccess) {
            user.getUserCart().emptyCart();
        }
        else{
            warehouse.addItemToInventory(productCategoryAndCountMap);
        }

    }

    public boolean makePayment(PaymentMode paymentMode){
        payment = new Payment(paymentMode);
       return payment.makePayment();
    }

    public void generateOrderInvoice(){
        invoice.generateInvoice(this);
    }
}
