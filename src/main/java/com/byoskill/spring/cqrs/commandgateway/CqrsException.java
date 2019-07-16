/**
 *
 */
package com.byoskill.spring.cqrs.commandgateway;

/**
 * This exception is a wrapper for exceptions produced inside the domain (pojo,
 * dao, services)
 *
 * @author sleroy
 *
 */
public class CqrsException extends RuntimeException {

    private static final String DOMAIN_EXCEPTION = "CQRS Exception : ";

    /**
     * Domain exception.
     */
    public CqrsException() {
        this("");
    }

    /**
     * Instantiates a new cqrs exception.
     *
     * @param _message the message
     */
    public CqrsException(final String _message) {
        this(_message, null);
    }

    /**
     * Instantiates a new cqrs exception.
     *
     * @param _message the message
     * @param _cause the cause
     */
    public CqrsException(final String _message, final Throwable _cause) {
        super(DOMAIN_EXCEPTION + _message, _cause);
    }

    public CqrsException(final Throwable cause) {
        this(cause.getMessage(), cause);
    }

}
