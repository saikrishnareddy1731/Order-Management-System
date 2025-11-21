package com.order.management.system.model.order;

public class Invoice {

    public int totalItemPrice;
    public int totalTax;
    public int totalFinalPrice;

    //generate Invoice
    public void generateInvoice(Order order){

        //it will compute and update the above details
        totalItemPrice = 200;
        totalTax = 20;
        totalFinalPrice = 220;
    }
}
