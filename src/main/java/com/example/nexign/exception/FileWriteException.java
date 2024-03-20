package com.example.nexign.exception;

/**
 * Exception thrown when an error occurs while writing to a file.
 */
public class FileWriteException extends RuntimeException {

    /**
     * Constructs a new FileWriteException with the specified detail message.
     *
     * @param message the detail message
     */
    public FileWriteException(String message) {
        super(message);
    }

}
