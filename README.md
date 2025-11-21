# Order Management System

## 📋 System Requirements

### Functional Requirements
1. **User Management**: System should manage multiple users with personal information and addresses
2. **Warehouse Management**: Support multiple warehouses with location-based inventory
3. **Product Catalog**: Organize products in categories with pricing
4. **Shopping Cart**: Users can add/remove items and manage cart before checkout
5. **Warehouse Selection**: Select optimal warehouse based on configurable strategies (e.g., nearest location)
6. **Order Processing**: Create orders from cart items with invoice generation
7. **Payment Processing**: Support multiple payment modes (UPI, Card, etc.)
8. **Inventory Management**: Real-time inventory updates on order placement
9. **Checkout Flow**: Complete transaction with payment verification and cart cleanup

### Non-Functional Requirements
- **Extensibility**: Easy to add new payment modes and warehouse selection strategies
- **Modularity**: Separation of concerns with controller pattern
- **Maintainability**: Clean architecture with design patterns

---

## 🗂️ Core Objects

| Object | Data | Description |
|--------|------|-------------|
| **User** | userId, userName, Address, Cart, orderIds | Customer entity with shopping cart |
| **Cart** | Map<ProductCategoryId, Count> | Manages items before order placement |
| **Warehouse** | Inventory, Address | Storage facility with stock |
| **Inventory** | List<ProductCategory> | Category-wise product organization |
| **ProductCategory** | categoryId, categoryName, List<Product>, price | Product grouping with pricing |
| **Product** | productId, productName | Individual product item |
| **Order** | User, Warehouse, productCategoryMap, Invoice, Payment, OrderStatus, Address | Complete transaction record |
| **Invoice** | totalItemPrice, totalTax, totalFinalPrice | Order billing information |
| **Payment** | PaymentMode | Payment processing handler |
| **Address** | pinCode, city, state | Location information |
| **OrderStatus** | DELIVERED, CANCELLED, RETURNED, UNDELIVERED | Order state enum |

---

## 🏗️ UML Diagrams

### System Architecture

```mermaid
graph TB
    subgraph "Controller Layer"
        UC[UserController<br/>━━━━━━━━<br/>List users]
        WC[WarehouseController<br/>━━━━━━━━<br/>List warehouses<br/>WarehouseSelectionStrategy]
        OC[OrderController<br/>━━━━━━━━<br/>List orders<br/>Map userIdVsOrders]
    end
    
    subgraph "Core Domain"
        U[User<br/>━━━━━━━━<br/>userId<br/>userName<br/>Address<br/>Cart]
        W[Warehouse<br/>━━━━━━━━<br/>Inventory<br/>Address]
        O[Order<br/>━━━━━━━━<br/>User<br/>Warehouse<br/>Invoice<br/>Payment<br/>OrderStatus]
    end
    
    subgraph "Product Hierarchy"
        I[Inventory<br/>━━━━━━━━<br/>List ProductCategory]
        PC[ProductCategory<br/>━━━━━━━━<br/>categoryId<br/>categoryName<br/>price<br/>List Product]
        P[Product<br/>━━━━━━━━<br/>productId<br/>productName]
    end
    
    subgraph "Supporting"
        C[Cart<br/>━━━━━━━━<br/>Map productCategoryId<br/>vs Count]
        INV[Invoice<br/>━━━━━━━━<br/>totalItemPrice<br/>totalTax<br/>totalFinalPrice]
        PAY[Payment<br/>━━━━━━━━<br/>PaymentMode]
        ADDR[Address<br/>━━━━━━━━<br/>pinCode<br/>city<br/>state]
    end
    
    subgraph "Strategy Pattern"
        WS[WarehouseSelectionStrategy<br/>Abstract]
        NWS[NearestWarehouseStrategy]
        PM[PaymentMode<br/>Interface]
        UPI[UPIPaymentMode]
        CARD[CardPaymentMode]
    end
    
    UC --> U
    WC --> W
    WC --> WS
    OC --> O
    
    U --> C
    U --> ADDR
    W --> I
    W --> ADDR
    I --> PC
    PC --> P
    
    O --> U
    O --> W
    O --> INV
    O --> PAY
    
    PAY --> PM
    WS --> NWS
    PM --> UPI
    PM --> CARD
```

### Class Diagram

```mermaid
classDiagram
    class ProductDeliverySystem {
        -UserController userController
        -WarehouseController warehouseController
        -OrderController orderController
        +getUser(userId) User
        +getWarehouse(strategy) Warehouse
        +getInventory(warehouse) Inventory
        +addProductToCart(user, product, count)
        +placeOrder(user, warehouse) Order
        +checkout(order)
    }

    class UserController {
        -List~User~ userList
        +addUser(user)
        +removeUser(user)
        +getUser(userId) User
    }

    class WarehouseController {
        -List~Warehouse~ warehouseList
        -WarehouseSelectionStrategy strategy
        +addNewWarehouse(warehouse)
        +removeWarehouse(warehouse)
        +selectWarehouse(strategy) Warehouse
    }

    class OrderController {
        -List~Order~ orderList
        -Map~Integer,List~Order~~ userIDVsOrders
        +createNewOrder(user, warehouse) Order
        +removeOrder(order)
        +getOrderByCustomerId(userId) List~Order~
        +getOrderByOrderId(orderId) Order
    }

    class User {
        -int userId
        -String userName
        -Address address
        -Cart userCartDetails
        -List~Integer~ orderIds
        +getUserCart() Cart
    }

    class Cart {
        -Map~Integer,Integer~ productCategoryIdVsCountMap
        +addItemInCart(categoryId, count)
        +removeItemFromCart(categoryId, count)
        +emptyCart()
        +getCartItems() Map
    }

    class Warehouse {
        -Inventory inventory
        -Address address
        +removeItemFromInventory(map)
        +addItemToInventory(map)
    }

    class Inventory {
        -List~ProductCategory~ productCategoryList
        +addCategory(id, name, price)
        +addProduct(product, categoryId)
        +removeItems(map)
    }

    class ProductCategory {
        -int productCategoryId
        -String categoryName
        -List~Product~ products
        -double price
        +addProduct(product)
        +removeProduct(count)
    }

    class Product {
        -int productId
        -String productName
    }

    class Order {
        -User user
        -Address deliveryAddress
        -Map~Integer,Integer~ productCategoryAndCountMap
        -Warehouse warehouse
        -Invoice invoice
        -Payment payment
        -OrderStatus orderStatus
        +checkout()
        +makePayment(paymentMode) boolean
        +generateOrderInvoice()
    }

    class Invoice {
        -int totalItemPrice
        -int totalTax
        -int totalFinalPrice
        +generateInvoice(order)
    }

    class Payment {
        -PaymentMode paymentMode
        +makePayment() boolean
    }

    class PaymentMode {
        <<interface>>
        +makePayment()* boolean
    }

    class UPIPaymentMode {
        +makePayment() boolean
    }

    class CardPaymentMode {
        +makePayment() boolean
    }

    class Address {
        -int pinCode
        -String city
        -String state
    }

    class OrderStatus {
        <<enumeration>>
        DELIVERED
        CANCELLED
        RETURNED
        UNDELIVERED
    }

    class WarehouseSelectionStrategy {
        <<abstract>>
        +selectWarehouse(warehouseList)* Warehouse
    }

    class NearestWarehouseSelectionStrategy {
        +selectWarehouse(warehouseList) Warehouse
    }

    ProductDeliverySystem --> UserController
    ProductDeliverySystem --> WarehouseController
    ProductDeliverySystem --> OrderController
    
    UserController --> User
    User --> Cart
    User --> Address
    
    WarehouseController --> Warehouse
    WarehouseController --> WarehouseSelectionStrategy
    NearestWarehouseSelectionStrategy --|> WarehouseSelectionStrategy
    
    Warehouse --> Inventory
    Warehouse --> Address
    Inventory --> ProductCategory
    ProductCategory --> Product
    
    OrderController --> Order
    Order --> User
    Order --> Warehouse
    Order --> Invoice
    Order --> Payment
    Order --> OrderStatus
    Order --> Address
    
    Payment --> PaymentMode
    UPIPaymentMode ..|> PaymentMode
    CardPaymentMode ..|> PaymentMode
```

### Sequence Diagram - Order Flow

```mermaid
sequenceDiagram
    participant M as Main
    participant PDS as ProductDeliverySystem
    participant UC as UserController
    participant WC as WarehouseController
    participant U as User
    participant C as Cart
    participant WH as Warehouse
    participant OC as OrderController
    participant O as Order
    participant PAY as Payment

    M->>PDS: getUser(userId)
    PDS->>UC: getUser(userId)
    UC-->>PDS: User
    
    M->>PDS: getWarehouse(strategy)
    PDS->>WC: selectWarehouse(strategy)
    WC-->>PDS: Warehouse
    
    M->>PDS: getInventory(warehouse)
    PDS->>WH: Get inventory
    WH-->>PDS: Inventory
    
    M->>PDS: addProductToCart(user, product, count)
    PDS->>U: getUserCart()
    U->>C: Get Cart
    PDS->>C: addItemInCart(categoryId, count)
    
    M->>PDS: placeOrder(user, warehouse)
    PDS->>OC: createNewOrder(user, warehouse)
    OC->>O: new Order(user, warehouse)
    O->>O: generateInvoice()
    OC-->>PDS: Order
    
    M->>PDS: checkout(order)
    PDS->>O: checkout()
    O->>WH: removeItemFromInventory(map)
    O->>PAY: makePayment(paymentMode)
    PAY-->>O: Payment Success
    O->>C: emptyCart()
```

### Component Flow Diagram

```mermaid
flowchart LR
    subgraph Input
        MAIN[Main Application]
    end
    
    subgraph Facade
        PDS[ProductDeliverySystem]
    end
    
    subgraph Controllers
        UC[UserController]
        WC[WarehouseController]
        OC[OrderController]
    end
    
    subgraph Domain
        U[User + Cart]
        W[Warehouse + Inventory]
        O[Order + Invoice + Payment]
    end
    
    subgraph Strategy
        WS[Warehouse Strategy]
        PS[Payment Strategy]
    end
    
    MAIN --> PDS
    PDS --> UC
    PDS --> WC
    PDS --> OC
    UC --> U
    WC --> W
    WC --> WS
    OC --> O
    O --> PS
```

### State Diagram - Order Status

```mermaid
stateDiagram-v2
    [*] --> Created: Order Placed
    Created --> Processing: Payment Initiated
    Processing --> PaymentSuccess: Payment Complete
    Processing --> PaymentFailed: Payment Failed
    PaymentSuccess --> Shipped: Warehouse Dispatch
    Shipped --> Delivered: Delivered to User
    PaymentFailed --> Cancelled: Order Cancelled
    Delivered --> Returned: Return Requested
    Returned --> [*]
    Cancelled --> [*]
    Delivered --> [*]
```

---

## 🎯 Design Patterns Used

### 1. Strategy Pattern
**Purpose**: Enable runtime selection of algorithms

**Implementation**:
- **Warehouse Selection**: `WarehouseSelectionStrategy` → `NearestWarehouseSelectionStrategy`
- **Payment Processing**: `PaymentMode` → `UPIPaymentMode`, `CardPaymentMode`

### 2. Facade Pattern
**Purpose**: Simplify complex subsystem interactions

**Implementation**: `ProductDeliverySystem` provides unified interface to UserController, WarehouseController, and OrderController

### 3. Controller Pattern
**Purpose**: Separate business logic from domain objects

**Implementation**: UserController, WarehouseController, OrderController manage their respective domains

---

## 📊 Key Relationships

| Relationship | From | To | Type |
|--------------|------|-----|------|
| Composition | User | Cart | Has-a |
| Composition | Warehouse | Inventory | Has-a |
| Composition | Inventory | ProductCategory | Has-a |
| Composition | ProductCategory | Product | Has-a |
| Composition | Order | Invoice | Has-a |
| Composition | Order | Payment | Has-a |
| Association | Order | User | References |
| Association | Order | Warehouse | References |
| Inheritance | NearestWarehouseSelectionStrategy | WarehouseSelectionStrategy | Is-a |
| Implementation | UPIPaymentMode | PaymentMode | Implements |
| Implementation | CardPaymentMode | PaymentMode | Implements |

---

## 🔄 Request-Response Flow

**Example**: Order 2 Pepsi Cold Drinks

```
Request Flow:
Main → ProductDeliverySystem → UserController → User (ID: 1)
                              ↓
                         WarehouseController → NearestWarehouseStrategy → Warehouse
                              ↓
                         Warehouse → Inventory → ProductCategory (Pepsi, ID: 0001)
                              ↓
                         Cart.addItem(0001, 2)
                              ↓
                         OrderController → Order(User, Warehouse)
                              ↓
                         Order.checkout()
                              ↓
                         Warehouse.removeInventory(0001, 2)
                              ↓
                         Payment.makePayment(UPI) → Success
                              ↓
                         Cart.emptyCart()
                              ↓
Response: Order Completed
```

---

## 🚀 Quick Start

```bash
# Run the application
java -cp target/classes com.order.management.system.Main
```

**Expected Output**: Order placed for 2 Pepsi items with invoice generation and payment processing.

---
