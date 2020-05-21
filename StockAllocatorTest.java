package com.t0day.recruitment.data;

import com.t0day.recruitment.Order;
import com.t0day.recruitment.Restock;
import com.t0day.recruitment.StockAllocator;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class StockAllocatorTest {
    @Test
    public void testGetRestocksBeforeShipTime() throws IOException {
        var orders = getMockOrders();
        var restocks = getMockRestocks();
        var stockAllocator = new StockAllocator();

        var actualRestocks = stockAllocator.getRestocksBeforeShipTime(orders.get(0), restocks);
        var actualRestock1 = actualRestocks.get(0);
        var actualRestock2 = actualRestocks.get(1);
        var actualRestocksSize = actualRestocks.size();

        var expectedRestock1 = new Restock(LocalTime.of(12, 0), new int[]{10, 10});
        var expectedRestock2 = new Restock(LocalTime.of(13, 0), new int[]{10, 10});
        var expectedRestockSize = 2;

        assertEquals(expectedRestock1, actualRestock1);
        assertEquals(expectedRestock2, actualRestock2);
        assertEquals(expectedRestockSize, actualRestocksSize);
    }

    @Test
    public void testGetQuantsBeforeShipTime() throws IOException {
        var orders = getMockOrders();
        var restocks = getMockRestocks();
        var stockAllocator = new StockAllocator();

        var actualQuantities = stockAllocator.getQuantsBeforeShipTime(orders.get(0), restocks);
        int[] expectedQuantities = {20, 20};
        assertArrayEquals(expectedQuantities, actualQuantities);
    }

    @Test
    public void testDeductQuantsFromEarlierRestocks() throws IOException {
        var orders = getMockOrders();
        var restocks = getMockRestocks();
        var stockAllocator = new StockAllocator();

        var actualRestocks = stockAllocator.deductQuantsFromEarlierRestocks(orders.get(0), restocks);
        var actualRestock1 = actualRestocks.get(0);
        var actualRestock2 = actualRestocks.get(1);
        var actualRestocksSize = actualRestocks.size();

        var expectedRestock1 = new Restock(LocalTime.of(12, 0), new int[]{10, 0});
        var expectedRestock2 = new Restock(LocalTime.of(13, 0), new int[]{5, 0});
        var expectedRestockSize = 2;

        assertEquals(expectedRestock1, actualRestock1);
        assertEquals(expectedRestock2, actualRestock2);
        assertEquals(expectedRestockSize, actualRestocksSize);
    }

    @Test
    public void testGetAcceptedOrders() throws IOException {
        var orders = getMockOrders();
        var restocks = getMockRestocks();
        var stockAllocator = new StockAllocator();

        var acceptedOrders = stockAllocator.getAcceptedOrders(orders, restocks);
        var actualAcceptedOrder1 = acceptedOrders.get(0);
        var actualAcceptedOrder2 = acceptedOrders.get(1);
        var actualAcceptedOrdersSize = acceptedOrders.size();

        var expectedAcceptedOrder1 = new Order("2", LocalTime.of(10, 02), LocalTime.of(12, 15), new int[]{10, 0});
        var expectedAcceptedOrder2 = new Order("1", LocalTime.of(10, 01), LocalTime.of(13, 30), new int[]{5, 20});
        var expectedAcceptedOrdersSize = 2;

        assertEquals(expectedAcceptedOrder1, actualAcceptedOrder1);
        assertEquals(expectedAcceptedOrder2, actualAcceptedOrder2);
        assertEquals(expectedAcceptedOrdersSize, actualAcceptedOrdersSize);
    }

    @Test
    public void testGetStockQuantAfterShippingOrders() throws IOException {
        var orders = getMockOrders();
        var restocks = getMockRestocks();
        var stockAllocator = new StockAllocator();
        var acceptedOrders = stockAllocator.getAcceptedOrders(orders, restocks);

        restocks = getMockRestocks();
        var actualQuantitiesAfterShippingTime = stockAllocator.getStockQuantAfterShippingOrders(acceptedOrders, restocks);
        var actualQuantitiesAfterOrder1ShippingTime = actualQuantitiesAfterShippingTime.get(0);
        var actualQuantitiesAfterOrder2ShippingTime = actualQuantitiesAfterShippingTime.get(1);

        var expectedQuantitiesAfterOrder1ShippingTime = new int[]{0, 10};
        var expectedQuantitiesAfterOrder2ShippingTime = new int[]{5, 0};

        assertArrayEquals(expectedQuantitiesAfterOrder1ShippingTime, actualQuantitiesAfterOrder1ShippingTime);
        assertArrayEquals(expectedQuantitiesAfterOrder2ShippingTime, actualQuantitiesAfterOrder2ShippingTime);
    }

    @Test
    public void testDeductRestockQuantBySmallerOrderQuant() {
        var stockAllocator = new StockAllocator();

        var orderQuantity = 1;
        var restockQuantity = 2;

        var actualUpdatedQuantities = stockAllocator.deductRestockQuantByOrderQuant(orderQuantity, restockQuantity);
        int[] expectedUpdatedQuantities = {0, 1};

        assertArrayEquals(expectedUpdatedQuantities, actualUpdatedQuantities);
    }

    @Test
    public void testDeductRestockQuantByLargerOrderQuant() {
        var stockAllocator = new StockAllocator();

        var orderQuantity = 2;
        var restockQuantity = 1;

        var actualUpdatedQuantities = stockAllocator.deductRestockQuantByOrderQuant(orderQuantity, restockQuantity);
        int[] expectedUpdatedQuantities = {1, 0};

        assertArrayEquals(expectedUpdatedQuantities, actualUpdatedQuantities);
    }

    public List<Order> getMockOrders() throws IOException {
        return OrderStore.fromResource("example_orders.csv").orders();
    }

    public List<Restock> getMockRestocks() throws IOException {
        return RestockStore.fromResource("example_restocks.csv").restocks();
    }

}
