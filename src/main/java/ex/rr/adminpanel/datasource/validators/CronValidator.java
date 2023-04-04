package ex.rr.adminpanel.datasource.validators;

import ex.rr.adminpanel.datasource.annotations.Cron;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.scheduling.support.CronExpression;

public class CronValidator implements ConstraintValidator<Cron, String> {

    @Override
    public void initialize(Cron constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return CronExpression.isValidExpression(s);
    }
}
