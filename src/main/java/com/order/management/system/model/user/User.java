package com.order.management.system.model.user;

import com.order.management.system.model.cart.Cart;

import java.util.ArrayList;
import java.util.List;

public class User {

    public int userId;
    public String userName;
    public Address address;
    public Cart userCartDetails;
    public List<Integer> orderIds;

    public User(){
        userCartDetails = new Cart();
        orderIds = new ArrayList<>();
    }

    //get UserCart
    public Cart getUserCart(){
        return userCartDetails;
    }
}
