import com.merce.model.db.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

void main() {
  check(() -> User.create(null, null, null, null));
}

<T> void check(Supplier<T> objectSupplier) {
  try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
    Validator validator = factory.getValidator();
    T object = objectSupplier.get();
    Set<ConstraintViolation<T>> violations = validator.validate(object);
    System.out.println("Validation errors: " + violations.size());
    violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));
  }
}