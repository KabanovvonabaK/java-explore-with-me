package ru.practicum.exceptions;

public class UnpublishedEventException extends RuntimeException {
    public UnpublishedEventException(String message) {
        super(message);
    }
}