/**
 * Copyright (C) 2017 Sylvain Leroy - BYOS Company All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the MIT license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the MIT license with
 * this file. If not, please write to: contact@sylvainleroy.com, or visit : https://sylvainleroy.com
 */
package com.byoskill.spring.cqrs.gate.impl;

import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.byoskill.spring.cqrs.api.HandlersProvider;
import com.byoskill.spring.cqrs.api.ICommandCallback;
import com.byoskill.spring.cqrs.api.ICommandExecutionListener;
import com.byoskill.spring.cqrs.api.ICommandHandler;
import com.byoskill.spring.cqrs.api.ICommandProfilingService;
import com.byoskill.spring.cqrs.gate.api.CommandHandlerNotFoundException;
import com.byoskill.spring.cqrs.gate.api.ICommandExceptionContext;
import com.byoskill.spring.cqrs.gate.api.ICommandExceptionHandler;
import com.byoskill.spring.cqrs.gate.api.InvalidCommandException;
import com.byoskill.spring.cqrs.gate.conf.CqrsConfiguration;
import com.byoskill.spring.utils.validation.ObjectValidation;

/**
 * This class prepares the command to be executed. It can override the default
 * command handler with a wrapper with enhanced functionalities. It Executes
 * SEQUENTIALLY the commands.
 *
 * @author Slawek
 */
@Service
public class CommandExecutorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandExecutorService.class);

    private final Optional<ICommandExceptionHandler> commandExceptionHandler;

    private CqrsConfiguration configuration;

    private final DefaultExceptionHandler defaultExceptionHandler;

    private final HandlersProvider handlersProvider;

    private ICommandExecutionListener[] listeners;

    private final ICommandProfilingService profilingService;

    /**
     * Instantiates a new sequential command executor service.
     *
     * @param configuration
     *            the configuration
     * @param handlersProvider
     *            the handlers provider
     * @param listeners
     *            the listeners
     * @param profilingService
     *            the profiling service
     * @param commandExceptionHandler
     *            the command exception handler
     */
    @Autowired
    public CommandExecutorService(final CqrsConfiguration configuration,
	    final HandlersProvider handlersProvider, final ICommandExecutionListener[] listeners,
	    final ICommandProfilingService profilingService,
	    final Optional<ICommandExceptionHandler> commandExceptionHandler) {
	super();
	this.configuration = configuration;
	this.handlersProvider = handlersProvider;
	this.listeners = listeners;
	this.profilingService = profilingService;
	this.commandExceptionHandler = commandExceptionHandler;
	defaultExceptionHandler = new DefaultExceptionHandler();
    }

    /**
     * Executes a command in synchronous way.
     *
     * @param <R>
     *            the generic type
     * @param command
     *            the command
     * @return the result of the command
     */
    public <R> R run(final Object command) {
	final ObjectValidation objectValidation = new ObjectValidation();
	LOGGER.debug("Validation of the command {}", command);
	try {
	    objectValidation.validate(command);
	} catch (final ConstraintViolationException e) {
	    throw new InvalidCommandException(command, e);
	}

	final ICommandHandler<Object, Object> handler = handlersProvider.getHandler(command);
	if (handler == null) {
	    throw new CommandHandlerNotFoundException(command);
	}
	// You can add Your own capabilities here: dependency injection,
	// security, transaction management, logging, profiling, spying,
	// storing
	// commands, etc

	ICommandCallback<R> callback = new DefaultCommandCallback<>(command, handler);
	// Decorate with profiling
	if (configuration.isProfilingEnabled()) {
	    callback = profilingService.decorate(command, callback);
	}
	R result = null;
	try {

	    result = callback.call();

	    // Notify listeners
	    notifyListenersSuccess(command, result);

	} catch (final Exception e) {
	    final ICommandExceptionContext exceptionContext = new ICommandExceptionContext() {

		@Override
		public Object getCommand() {
		    return command;
		}

		@Override
		public Exception getException() {

		    return e;
		}

		@Override
		public Object getHandler() {
		    return handler;
		}
	    };
	    notifyListenersFailure(command, exceptionContext);
	    if (commandExceptionHandler.isPresent()) {

		commandExceptionHandler.get().handleException(exceptionContext);
	    } else {
		defaultExceptionHandler.handleException(exceptionContext);
	    }
	}
	return result;
    }

    public void setConfiguration(final CqrsConfiguration _configuration) {
	configuration = _configuration;
    }

    public void setListeners(final ICommandExecutionListener[] _listeners) {
	listeners = _listeners;
    }

    private void notifyListenersFailure(final Object command, final ICommandExceptionContext exceptionContext) {
	for (final ICommandExecutionListener commandExecutionListener : listeners) {
	    commandExecutionListener.onFailure(command, exceptionContext);
	}
    }

    private void notifyListenersSuccess(final Object command, final Object result) {
	for (final ICommandExecutionListener commandExecutionListener : listeners) {
	    commandExecutionListener.onSuccess(command, result);
	}
    }
}