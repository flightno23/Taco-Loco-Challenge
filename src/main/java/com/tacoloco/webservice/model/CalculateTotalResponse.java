package com.tacoloco.webservice.model;

/**
 * The response information for the calculate total API.
 */
public final class CalculateTotalResponse {
    /**
     * The total price calculated.
     */
    private double totalPrice;

    /**
     * @param price The total price.
     */
    public CalculateTotalResponse(double price) {
        this.totalPrice = price;
    }

    /**
     * @return The total price calculated.
     */
    public double getTotalPrice() {
        return totalPrice;
    }

}
