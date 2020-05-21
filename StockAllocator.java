package com.t0day.recruitment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StockAllocator {

    // Get accumulated quantities after shipping an order
    public List<int[]> getStockQuantAfterShippingOrders(List<Order> orders, List<Restock> restocks) {
        List<int[]> quantitiesAfterShippingTime = new ArrayList<>();
        for (Order order : orders) {
            deductQuantsFromEarlierRestocks(order, restocks);
            quantitiesAfterShippingTime.add(getQuantsBeforeShipTime(order, restocks));
        }
        return quantitiesAfterShippingTime;
    }

    // Get orders with sufficient stocks at the shipping time
    public List<Order> getAcceptedOrders(List<Order> orders, List<Restock> restocks) {
        List<Order> acceptedOrders = new ArrayList<>();
        int[] availableQuantities;

        for (Order order : orders) {
            availableQuantities = getQuantsBeforeShipTime(order, restocks);

            if (order.getQuantities()[0] <= availableQuantities[0] && order.getQuantities()[1] <= availableQuantities[1]) {
                acceptedOrders.add(order);
                restocks = deductQuantsFromEarlierRestocks(order, restocks);
            }
        }
        return acceptedOrders.stream()
                .sorted(Comparator.comparing(Order::getShippingTime))
                .collect(Collectors.toList());
    }

    // Get available restocks before a shipping time
    public List<Restock> getRestocksBeforeShipTime(Order order, List<Restock> restocks) {
        return restocks.stream()
                .filter(restock -> restock.getArrivalTime().isBefore(order.getShippingTime()))
                .collect(Collectors.toList());
    }

    // Get the stock quantity info before a shipping time
    public int[] getQuantsBeforeShipTime(Order order, List<Restock> restocks) {
        int[] stockQuant = {0, 0};
        getRestocksBeforeShipTime(order, restocks).stream()
                .forEach(restock -> {
                    stockQuant[0] += restock.getQuantities()[0];
                    stockQuant[1] += restock.getQuantities()[1];
                });
        return stockQuant;
    }

    // Deduct restocks based on an order. It starts from the restocks come near shipping time
    // So the order which being shipped at 13:15 will firstly deduct the restcok arriving at 13:00 then at 12:00
    public List<Restock> deductQuantsFromEarlierRestocks(Order order, List<Restock> restocks) {
        var orderQuantity1 = order.getQuantities()[0];
        var orderQuantity2 = order.getQuantities()[1];
        var restockQuantity1 = 0;
        var restockQuantity2 = 0;

        for (int i = restocks.size() - 1; i >= 0; i--) {
            if (restocks.get(i).getArrivalTime().isBefore(order.getShippingTime())) {
                if (orderQuantity1 != 0 || orderQuantity2 != 0) {
                    restockQuantity1 = restocks.get(i).getQuantities()[0];
                    restockQuantity2 = restocks.get(i).getQuantities()[1];

                    var quantities = deductRestockQuantByOrderQuant(orderQuantity1, restockQuantity1);
                    orderQuantity1 = quantities[0];
                    restockQuantity1 = quantities[1];

                    quantities = deductRestockQuantByOrderQuant(orderQuantity2, restockQuantity2);
                    orderQuantity2 = quantities[0];
                    restockQuantity2 = quantities[1];

                    restocks.get(i).setQuantities(new int[]{restockQuantity1, restockQuantity2});
                }
            }
        }
        return restocks;
    }

    // Deduct restock quantity with order quantity. Carry on the order quantity left if it is larger than a restock quantity
    public int[] deductRestockQuantByOrderQuant(int orderQuantity, int restockQuantity) {
        if (orderQuantity <= restockQuantity) {
            restockQuantity -= orderQuantity;
            orderQuantity = 0;
        } else {
            orderQuantity -= restockQuantity;
            restockQuantity = 0;
        }
        return new int[]{orderQuantity, restockQuantity};
    }

    // Print outputs
    public void printAcceptedOrdersAndStockQuants(List<Order> acceptedOrders, List<int[]> quantitiesAfterShippingAcceptedOrders) {
        var orderId = "";
        var quantity1 = 0;
        var quantity2 = 0;

        for (int i = 0; i < acceptedOrders.size(); i++) {
            orderId = acceptedOrders.get(i).getOrderID();
            quantity1 = quantitiesAfterShippingAcceptedOrders.get(i)[0];
            quantity2 = quantitiesAfterShippingAcceptedOrders.get(i)[1];

            System.out.println(String.format("%s, [%d, %d]", orderId, quantity1, quantity2));
        }
    }
}
