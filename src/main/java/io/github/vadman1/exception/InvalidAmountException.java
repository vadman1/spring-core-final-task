package io.github.vadman1.exception;

public class InvalidAmountException extends IllegalArgumentException {
    public InvalidAmountException(String message) {
        super(message);
    }
}