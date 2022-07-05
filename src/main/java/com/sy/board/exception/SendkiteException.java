package com.sy.board.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class SendkiteException extends RuntimeException {

    public final Map<String, String> validation = new HashMap<>();

    public SendkiteException(String message) {
        super(message);
    }

    public SendkiteException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
