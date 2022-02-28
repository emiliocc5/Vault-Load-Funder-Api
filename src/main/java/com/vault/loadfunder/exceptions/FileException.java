package com.vault.loadfunder.exceptions;

public class FileException extends RuntimeException {
    private String message;

    public FileException(String message, String message1) {
        super(message);
        this.message = message1;
    }
}
