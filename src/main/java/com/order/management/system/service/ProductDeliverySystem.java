package com.order.management.system.service;

import com.order.management.system.controller.OrderController;
import com.order.management.system.controller.UserController;
import com.order.management.system.controller.WarehouseController;
import com.order.management.system.strategy.WarehouseSelectionStrategy;
import com.order.management.system.model.cart.Cart;
import com.order.management.system.model.order.Order;
import com.order.management.system.model.product.ProductCategory;
import com.order.management.system.model.user.User;
import com.order.management.system.model.warehouse.Inventory;
import com.order.management.system.model.warehouse.Warehouse;

import java.util.List;

public class ProductDeliverySystem {

    UserController userController;
    WarehouseController warehouseController;
    OrderController orderController;

    public ProductDeliverySystem(List<User> userList, List<Warehouse> warehouseList){
        userController = new UserController(userList);
        warehouseController = new WarehouseController(warehouseList , null);
        orderController = new OrderController();
    }

    //get user object
    public User getUser(int userId){
        return userController.getUser(userId);
    }

    //get warehouse
    public Warehouse getWarehouse(WarehouseSelectionStrategy warehouseSelectionStrategy){
        return warehouseController.selectWarehouse(warehouseSelectionStrategy);

    }

    //get inventory
    public Inventory getInventory(Warehouse warehouse){
        return warehouse.inventory;

    }

    //add product to cart
    public void addProductToCart(User user, ProductCategory product, int count){
        Cart cart = user.getUserCart();
        cart.addItemInCart(product.productCategoryId, count);
    }

    //place order
    public Order placeOrder(User user, Warehouse warehouse){
        return orderController.createNewOrder(user, warehouse);
    }

    public void checkout(Order order){
        order.checkout();
    }

}
