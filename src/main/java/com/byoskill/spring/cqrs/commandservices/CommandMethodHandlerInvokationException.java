package com.byoskill.spring.cqrs.commandservices;

import com.byoskill.spring.cqrs.commandgateway.CqrsException;

public class CommandMethodHandlerInvokationException extends CqrsException {
    /**
     * Instantiates a new Command method handler invokation exception.
     *
     * @param e the related exception.
     */
    public CommandMethodHandlerInvokationException(final Exception e) {
        super("Invokation of the command handler has failed", e);
    }
}
