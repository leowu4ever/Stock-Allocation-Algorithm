package com.t0day.recruitment;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Objects;

public class Restock {

    private final LocalTime arrivalTime;
    private int[] quantities;

    public Restock(LocalTime arrivalTime, int[] quantities) {
        this.arrivalTime = arrivalTime;
        this.quantities = quantities;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public int[] getQuantities() {
        return quantities;
    }

    public void setQuantities(int[] quantities) {
        this.quantities = quantities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restock restock = (Restock) o;
        return Objects.equals(arrivalTime, restock.arrivalTime) &&
                Arrays.equals(quantities, restock.quantities);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(arrivalTime);
        result = 31 * result + Arrays.hashCode(quantities);
        return result;
    }
}