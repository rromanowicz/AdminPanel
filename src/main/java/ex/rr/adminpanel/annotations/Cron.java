package ex.rr.adminpanel.annotations;

import ex.rr.adminpanel.database.config.DataSourceRouting;
import ex.rr.adminpanel.validators.CronValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code @Cron} annotation marks the field to be verified if it contains a valid cron expression.
 *
 * @author  rromanowicz
 */
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
