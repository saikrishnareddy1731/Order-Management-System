package com.order.management.system.strategy;

import com.order.management.system.model.warehouse.Warehouse;

import java.util.List;

public abstract class WarehouseSelectionStrategy {

    public abstract Warehouse selectWarehouse(List<Warehouse> warehouseList);
}
