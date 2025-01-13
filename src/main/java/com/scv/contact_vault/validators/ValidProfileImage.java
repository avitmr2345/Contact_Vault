package com.scv.contact_vault.validators;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
                ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidator.class)

public @interface ValidProfileImage {
        String message() default "Invalid file";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
}