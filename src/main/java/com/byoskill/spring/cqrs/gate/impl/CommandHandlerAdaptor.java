package com.byoskill.spring.cqrs.gate.impl;

import org.apache.commons.lang3.Validate;
/**
 * Copyright (C) 2017-2018 Credifix
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.byoskill.spring.cqrs.api.IAsyncCommandHandler;
import com.byoskill.spring.cqrs.api.ICommandHandler;

/**
 * The Class CommandHandlerAdaptor adapts the command handler implementation to
 * be consumed as an async promise.
 */
@Component
public class CommandHandlerAdaptor {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandHandlerAdaptor.class);

    /**
     * Adapt.
     *
     * @param bean
     *            the bean
     * @return the i async command handler
     */
    public <T, R> IAsyncCommandHandler<T, R> adapt(final Object bean) {
	Validate.notNull(bean);
	if (bean instanceof IAsyncCommandHandler) {
	    return IAsyncCommandHandler.class.cast(bean);
	}
	if (bean instanceof ICommandHandler) {
	    return new AsyncCommandHandlerWrapper<>((ICommandHandler<T, R>) bean);
	}
	throw new UnsupportedOperationException("Unsupported type of command handler " + bean);
    }
}
