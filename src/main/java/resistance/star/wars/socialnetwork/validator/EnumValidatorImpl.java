package resistance.star.wars.socialnetwork.validator;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, String> {
  private List<String> acceptedValues;

  @Override
  public void initialize(EnumValidator annotation) {
    acceptedValues = Stream.of(annotation.enumClazz().getEnumConstants())
        .map(Enum::name)
        .collect(Collectors.toList());
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    HibernateConstraintValidatorContext hibernateConstraintValidatorContext = context.unwrap( HibernateConstraintValidatorContext.class );
    hibernateConstraintValidatorContext.addMessageParameter("acceptedValues", String.join(",", acceptedValues));
    hibernateConstraintValidatorContext.addMessageParameter("valueSent", value);
    return acceptedValues.contains(value.toUpperCase());
  }
}
