package org.matveyvs.validator;

public interface Validator<T> {
    ValidationResult isValid (T object);
}
