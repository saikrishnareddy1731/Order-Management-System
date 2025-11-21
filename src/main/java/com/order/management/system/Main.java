package com.order.management.system;

import com.order.management.system.model.order.Order;
import com.order.management.system.model.product.Product;
import com.order.management.system.model.product.ProductCategory;
import com.order.management.system.model.user.Address;
import com.order.management.system.model.user.User;
import com.order.management.system.model.warehouse.Inventory;
import com.order.management.system.model.warehouse.Warehouse;
import com.order.management.system.service.ProductDeliverySystem;
import com.order.management.system.strategy.NearestWarehouseSelectionStrategy;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String args[]){
        System.out.println("========================================");
        System.out.println("   ORDER MANAGEMENT SYSTEM STARTED");
        System.out.println("========================================\n");

        Main mainObj = new Main();

        //1. create warehouses in the system
        System.out.println("Step 1: Creating Warehouses...");
        List<Warehouse> warehouseList = new ArrayList<>();
        warehouseList.add(mainObj.addWarehouseAndItsInventory());
        System.out.println("✓ Warehouse created with inventory\n");

        //2. create users in the system
        System.out.println("Step 2: Creating Users...");
        List<User> userList = new ArrayList<>();
        userList.add(mainObj.createUser());
        System.out.println("✓ User 'SJ' created with ID: 1\n");

        //3. feed the system with the initial informations
        System.out.println("Step 3: Initializing Product Delivery System...");
        ProductDeliverySystem productDeliverySystem = new ProductDeliverySystem(userList, warehouseList);
        System.out.println("✓ System initialized successfully\n");

        System.out.println("========================================");
        System.out.println("   STARTING ORDER FLOW");
        System.out.println("========================================\n");

        mainObj.runDeliveryFlow(productDeliverySystem, 1);

        System.out.println("\n========================================");
        System.out.println("   ORDER MANAGEMENT SYSTEM COMPLETED");
        System.out.println("========================================");
    }



    private Warehouse addWarehouseAndItsInventory(){

        Warehouse warehouse = new Warehouse();

        Inventory inventory = new Inventory();

        inventory.addCategory(0001, "Peppsii Large Cold Drink" , 100);
        inventory.addCategory(0004, "Doovee small Soap" , 50);

        //CREATE 3 Products

        Product product1 = new Product();
        product1.productId = 1;
        product1.productName = "Peepsii";

        Product product2 = new Product();
        product2.productId = 2;
        product2.productName = "Peepsii";

        Product product3 = new Product();
        product3.productId = 3;
        product3.productName = "Doovee";


        inventory.addProduct(product1, 0001);
        inventory.addProduct(product2, 0001);
        inventory.addProduct(product3, 0004);

        warehouse.inventory = inventory;

        System.out.println("  - Added Product Category: Peppsii Large Cold Drink (Price: ₹100)");
        System.out.println("  - Added Product Category: Doovee small Soap (Price: ₹50)");
        System.out.println("  - Added 2 Pepsi products to inventory");
        System.out.println("  - Added 1 Doovee product to inventory");

        return warehouse;
    }


    private User createUser(){
        User user = new User();
        user.userId = 1;
        user.userName = "SJ";
        user.address = new Address(230011, "city", "state");

        System.out.println("  - User ID: " + user.userId);
        System.out.println("  - User Name: " + user.userName);
        System.out.println("  - Address: " + user.address.getCity() + ", " + user.address.getState() + " - " + user.address.getPinCode());

        return user;
    }

    private void runDeliveryFlow(ProductDeliverySystem productDeliverySystem, int userId){

        //1. Get the user object
        System.out.println("Step 4: Fetching User Details...");
        User user = productDeliverySystem.getUser(userId);
        System.out.println("✓ User retrieved: " + user.userName + " (ID: " + user.userId + ")\n");

        //2. get warehouse based on user preference
        System.out.println("Step 5: Selecting Warehouse...");
        Warehouse warehouse = productDeliverySystem.getWarehouse(new NearestWarehouseSelectionStrategy());
        System.out.println("✓ Warehouse selected using NearestWarehouseStrategy\n");

        //3. get all the inventory to show the user
        System.out.println("Step 6: Fetching Inventory...");
        Inventory inventory = productDeliverySystem.getInventory(warehouse);
        System.out.println("✓ Inventory retrieved with " + inventory.productCategoryList.size() + " product categories\n");

        ProductCategory productCategoryIWantToOrder = null;
        for(ProductCategory productCategory : inventory.productCategoryList){
            System.out.println("  Available: " + productCategory.categoryName + " - ₹" + productCategory.price);
            if(productCategory.categoryName.equals("Peppsii Large Cold Drink")){
                productCategoryIWantToOrder = productCategory;
            }
        }
        System.out.println();

        //4. add product to cart
        System.out.println("Step 7: Adding Products to Cart...");
        int quantityToAdd = 2;
        productDeliverySystem.addProductToCart(user, productCategoryIWantToOrder, quantityToAdd);
        System.out.println("✓ Added " + quantityToAdd + "x " + productCategoryIWantToOrder.categoryName + " to cart\n");

        //4. place order
        System.out.println("Step 8: Placing Order...");
        Order order = productDeliverySystem.placeOrder(user, warehouse);
        System.out.println("✓ Order created successfully");
        System.out.println("  - Invoice Total: ₹" + order.invoice.totalFinalPrice);
        System.out.println("  - Item Price: ₹" + order.invoice.totalItemPrice);
        System.out.println("  - Tax: ₹" + order.invoice.totalTax + "\n");

        //5. checkout
        System.out.println("Step 9: Processing Checkout...");
        productDeliverySystem.checkout(order);
        System.out.println("✓ Checkout completed successfully");
        System.out.println("  - Inventory updated (removed " + quantityToAdd + " items)");
        System.out.println("  - Payment processed via UPI");
        System.out.println("  - Cart emptied");
    }
}