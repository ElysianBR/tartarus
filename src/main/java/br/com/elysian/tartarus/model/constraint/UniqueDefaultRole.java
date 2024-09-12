package br.com.elysian.tartarus.model.constraint;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RoleValidator.class)
public @interface UniqueDefaultRole {
    String message() default "Account already has a default role.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
