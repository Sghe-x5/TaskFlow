package com.example.taskflow.exception;

public class TaskNotFoundException extends RuntimeException {
    // Конструктор с сообщением
    public TaskNotFoundException(String message) {
        super(message);
    }
}
