package ru.larkin.starter.command.exception;

public class OverflowingQueueException extends RuntimeException {
    public OverflowingQueueException(String message) {
        super(message);
    }
}
