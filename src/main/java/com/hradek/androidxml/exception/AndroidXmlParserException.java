package com.hradek.androidxml.exception;

/**
 * This exception is thrown when android parser failed
 *
 * @author Ivo Hradek
 */
public class AndroidXmlParserException extends RuntimeException {
    /**
     * Creates a new instance of
     * <code>AndroidXmlParserException</code> without detail message.
     */
    public AndroidXmlParserException() { }

    /**
     * Constructs an instance of
     * <code>AndroidXmlParserException</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public AndroidXmlParserException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of
     * <code>AndroidXmlParserException</code> with the specified detail cause.
     *
     * @param cause the cause
     */
    public AndroidXmlParserException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance of
     * <code>AndroidXmlParserException</code> with the specified detail
     * message and cause.
     *
     * @param msg the detail message.
     * @param cause the cause
     */
    public AndroidXmlParserException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
