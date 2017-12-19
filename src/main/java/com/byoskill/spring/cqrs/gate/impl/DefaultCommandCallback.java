package com.byoskill.spring.cqrs.gate.impl;

import com.byoskill.spring.cqrs.api.ICommandCallback;
import com.byoskill.spring.cqrs.api.ICommandHandler;

/**
 * Default callback to invoke sequentially a command.
 *
 * @author sleroy
 *        
 * @param <R>
 */
public class DefaultCommandCallback<R> implements ICommandCallback<R> {

	private final ICommandHandler<Object, Object>	handler;
	private final Object							command;

	public DefaultCommandCallback(final Object _command, final ICommandHandler<Object, Object> _handler) {
		command = _command;
		handler = _handler;
	}

	@Override
	public R call() throws Exception {

		return (R) handler.handle(command);
	}
	
}
