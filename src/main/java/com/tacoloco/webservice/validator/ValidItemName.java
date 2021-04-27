package com.tacoloco.webservice.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ItemNameValidator.class)
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface ValidItemName {
    public String message() default "The item name is not a valid menu item";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

}
