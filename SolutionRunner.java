package com.t0day.recruitment;

import com.t0day.recruitment.data.OrderStore;
import com.t0day.recruitment.data.RestockStore;

import java.io.IOException;
import java.util.List;

public class SolutionRunner {
    public static void main(String[] args) throws IOException {
        OrderStore orderStore;
        RestockStore restockStore;
        if (args.length == 2) {
            orderStore = OrderStore.fromFile(args[0]);
            restockStore = RestockStore.fromFile(args[1]);
        } else {
            orderStore = OrderStore.fromResource("orders.csv");
            restockStore = RestockStore.fromResource("restocks.csv");
        }
        List<Order> orders = orderStore.orders();
        List<Restock> restocks = restockStore.restocks();

        // TODO: Allocate stock to orders

        // Get accpeted orders
        StockAllocator stockAllocator = new StockAllocator();
        List<Order> acceptedOrders = stockAllocator.getAcceptedOrders(orders, restocks);

        // Get stock quantities after shipping accepted orders
        restocks = restockStore.restocks();
        List<int[]> quantitiesAfterShippingAcceptedOrders = stockAllocator.getStockQuantAfterShippingOrders(acceptedOrders, restocks);

        // Print accepted order Id and stock stock quantities
        stockAllocator.printAcceptedOrdersAndStockQuants(acceptedOrders, quantitiesAfterShippingAcceptedOrders);
    }
}
