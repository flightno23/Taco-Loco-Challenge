package com.tacoloco.webservice.model;

import com.tacoloco.webservice.validator.ValidItemName;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * The Order item information.
 */
public final class OrderItem {
    /**
     * The name of the item.
     */
    @NotBlank
    @ValidItemName
    private final String itemName;

    /**
     * The quantity of the item ordered.
     */
    @Min(1)
    private final int quantity;

    /**
     * @param itemName name of the item.
     * @param quantity quantity of the item.
     */
    public OrderItem(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }

    /**
     * @return The name of the item.
     */
    public String getItemName() {
        return itemName;
    }


    /**
     * @return The quantity of the item.
     */
    public int getQuantity() {
        return quantity;
    }
}
