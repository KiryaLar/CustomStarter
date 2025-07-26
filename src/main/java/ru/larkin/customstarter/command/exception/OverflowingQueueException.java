package ru.larkin.customstarter.command.exception;

public class OverflowingQueueException extends RuntimeException {
    public OverflowingQueueException(String message) {
        super(message);
    }
}
