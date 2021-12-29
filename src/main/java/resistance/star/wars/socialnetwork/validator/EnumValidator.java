package resistance.star.wars.socialnetwork.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EnumValidatorImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ReportAsSingleViolation
public @interface EnumValidator {

  Class<? extends Enum<?>> enumClazz();

  String message() default "Enum '{valueSent}' is invalid. Accepted values [{acceptedValues}]";
  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}