package com.tacoloco.webservice.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Respository for managing order items.
 */
@Repository
public class OrderItemRepository {
    /**
     * Initial default map of item's pricing keyed by their name.
     * TODO: Add the data to a database.
     */
    private Map<String, Double> priceByItemName;

    public OrderItemRepository() {
        this.priceByItemName = new HashMap<>(3);
        priceByItemName.put("Veggie Taco", 2.50);
        priceByItemName.put("Chicken Taco", 3.00);
        priceByItemName.put("Beef Taco", 3.00);
        priceByItemName.put("Chorizo Taco", 3.50);
    }

    /**
     * Retrieves the price for a given item name.
     * @param itemName The name of the item.
     * @return {@code Optional} containing the price if item name was available.
     */
    public Optional<Double> findPriceByItemName(String itemName) {
        return Optional.ofNullable(priceByItemName.get(itemName));
    }

    /**
     * Finds all the available menu item names and caches it.
     * @return The Set of item names.
     */
    @Cacheable("items")
    public Set<String> findAllItemNames()
    {
        return priceByItemName.keySet();
    }

    /**
     * Check whether a given item name exists in the data source.
     * @param itemName The name of the item.
     * @return True if item exists, False otherwise.
     */
    public boolean doesItemNameExist(String itemName) {
        return findAllItemNames().contains(itemName);
    }
}
