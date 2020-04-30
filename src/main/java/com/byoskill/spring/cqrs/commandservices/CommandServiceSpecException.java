package com.byoskill.spring.cqrs.commandservices;

import com.byoskill.spring.cqrs.commandgateway.CqrsException;

public class CommandServiceSpecException extends CqrsException {
    public CommandServiceSpecException(final String message) {
        super(message);
    }
}
