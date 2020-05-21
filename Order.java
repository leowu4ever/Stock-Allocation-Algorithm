package com.t0day.recruitment;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Objects;

public class Order {
    private final String orderId;
    private final LocalTime checkoutTime;
    private final LocalTime shippingTime;
    private final int[] quantities;

    public Order(String orderId, LocalTime checkoutTime, LocalTime shippingTime, int[] quantities) {
        this.orderId = orderId;
        this.checkoutTime = checkoutTime;
        this.shippingTime = shippingTime;
        this.quantities = quantities;
    }

    public String getOrderID() {
        return orderId;
    }

    public LocalTime getCheckoutTime() {
        return checkoutTime;
    }

    public LocalTime getShippingTime() {
        return shippingTime;
    }

    public int[] getQuantities() {
        return quantities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderId, order.orderId) &&
                Objects.equals(checkoutTime, order.checkoutTime) &&
                Objects.equals(shippingTime, order.shippingTime) &&
                Arrays.equals(quantities, order.quantities);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(orderId, checkoutTime, shippingTime);
        result = 31 * result + Arrays.hashCode(quantities);
        return result;
    }
}
