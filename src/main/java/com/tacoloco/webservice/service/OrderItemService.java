package com.tacoloco.webservice.service;

import com.tacoloco.webservice.model.CalculateTotalResponse;
import com.tacoloco.webservice.model.OrderItem;
import com.tacoloco.webservice.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for managing order items.
 */
@Service
public class OrderItemService {
    private static final int MINIMUM_QTY_FOR_DISCOUNT = 4;
    private static final int DISCOUNT_PERCENTAGE = 20;

    @Autowired
    private OrderItemRepository orderItemRepository;

    /**
     * Calculates the total for the given {@link OrderItem}s.
     * @param orders The List of {@link OrderItem}s.
     * @return {@link CalculateTotalResponse} conatining the total amount.
     */
    public ResponseEntity<CalculateTotalResponse> calculateTotal(List<OrderItem> orders) {
        double totalPrice = 0.0;
        int totalQuantity = 0;

        Map<String, Integer> quantitiesByItemName = orders.stream().collect(Collectors.toMap(OrderItem::getItemName,
                OrderItem::getQuantity, (value1, value2) -> value1 + value2));

        for (Map.Entry<String, Integer> entry : quantitiesByItemName.entrySet()) {
            totalQuantity += entry.getValue();
            totalPrice += entry.getValue() * orderItemRepository.findPriceByItemName(entry.getKey()).orElseThrow();
        }

        if (totalQuantity >= MINIMUM_QTY_FOR_DISCOUNT) {
            totalPrice -= totalPrice * DISCOUNT_PERCENTAGE / 100;
        }
        return ResponseEntity.ok(new CalculateTotalResponse(totalPrice));
    }

    public boolean isValid(String itemName) {
        return orderItemRepository.doesItemNameExist(itemName);
    }
}
