package ex.rr.adminpanel.datasource.annotations;

import ex.rr.adminpanel.datasource.validators.CronValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {CronValidator.class}
)
public @interface Cron {
    String message() default "Invalid Cron expression.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
