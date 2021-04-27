package com.tacoloco.webservice.controller;

import com.tacoloco.webservice.model.CalculateTotalResponse;
import com.tacoloco.webservice.model.OrderItem;
import com.tacoloco.webservice.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Controller for operating on the restaurant's order items
 */
@RestController
@Validated
@RequestMapping("/")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    /**
     * Calculates the total price for the given {@link OrderItem}s.
     * @param orders List of {@link OrderItem}s whose total needs to be calculated.
     * @return {@link CalculateTotalResponse} containing the calculated total.
     */
    @PostMapping("/calculateTotal")
    public @ResponseBody
    ResponseEntity<CalculateTotalResponse> calculateTotal(@RequestBody @NotEmpty @Valid List<OrderItem> orders) {
        return orderItemService.calculateTotal(orders);
    }
}
