package com.hradek.androidxml.exception;

/**
 * This exception is thrown when resource reader failed
 *
 * @author Ivo Hradek
 */
public class ResReaderException extends RuntimeException {
    /**
     * Creates a new instance of
     * <code>ResReaderException</code> without detail message.
     */
    public ResReaderException() { }


    /**
     * Constructs an instance of
     * <code>ResReaderException</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public ResReaderException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of
     * <code>ResReaderException</code> with the specified detail cause.
     *
     * @param cause the cause
     */
    public ResReaderException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance of
     * <code>ResReaderException</code> with the specified detail
     * message and cause.
     *
     * @param msg the detail message.
     * @param cause the cause
     */
    public ResReaderException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
