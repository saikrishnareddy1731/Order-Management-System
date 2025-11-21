package com.order.management.system.model.warehouse;

import com.order.management.system.model.user.Address;

import java.util.Map;

//warehouse or store, which generally manages the inventory or act as Inventory controller
public class Warehouse {

    public Inventory inventory;
    Address address;

    //update inventory
    public void removeItemFromInventory(Map<Integer, Integer> productCategoryAndCountMap){

        //it will update the items in the inventory based upon product category.
        inventory.removeItems(productCategoryAndCountMap);
    }

    public void addItemToInventory(Map<Integer, Integer> productCategoryAndCountMap){

        //it will update the items in the inventory based upon product category.
    }
}
