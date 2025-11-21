package com.order.management.system.strategy;

import com.order.management.system.model.warehouse.Warehouse;

import java.util.List;

public class NearestWarehouseSelectionStrategy extends WarehouseSelectionStrategy {

    @Override
    public Warehouse selectWarehouse(List<Warehouse> warehouseList) {
       //algo to pick the nearest algo, for now I am just picking the first warehouse for demo purpose
        return warehouseList.get(0);
    }


}
