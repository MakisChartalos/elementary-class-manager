package gr.aueb.cf.elementaryclassmanager.validator;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Utility class for validating Data Transfer Objects (DTOs) using Jakarta Bean Validation.
 * This class provides a thread-safe mechanism to retrieve a singleton instance of the Validator.
 */
public class ValidatorUtil {

    private static Validator validator; // Singleton instance of Validator

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ValidatorUtil() {}

    /**
     * Synchronized method to get the Validator instance.
     * If the Validator instance is not already initialized, it creates a new one.
     *
     * @return the singleton Validator instance
     */
    private static synchronized Validator getValidator() {
        if (validator == null) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }
        return validator;
    }

    /**
     * Validates the given DTO and returns a list of error messages if there are any violations.
     *
     * @param dto the DTO to validate
     * @param <T> the type of the DTO
     * @return a list of error messages; empty list if there are no violations
     */
    public static <T> List<String> validateDTO(T dto) {
        Set<ConstraintViolation<T>> violations = getValidator().validate(dto);
        List<String> errors = new ArrayList<>();

        if (!violations.isEmpty()) {
            for (ConstraintViolation<T> violation : violations) {
                errors.add(violation.getMessage());
            }
        }
        return errors;
    }
}
