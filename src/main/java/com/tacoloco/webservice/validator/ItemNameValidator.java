package com.tacoloco.webservice.validator;

import com.tacoloco.webservice.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator for the item name.
 */
@Component
public class ItemNameValidator implements ConstraintValidator<ValidItemName, String> {

    @Autowired
    private OrderItemService orderItemService;

    /**
     * Checks whether the given item name is valid as part of the constraint.
     * @param itemName The name of the item.
     * @param context The {@link ConstraintValidatorContext}.
     * @return True if the item name is valid, False otherwise.
     */
    @Override
    public boolean isValid(String itemName, ConstraintValidatorContext context) {
        return orderItemService.isValid(itemName);
    }

}