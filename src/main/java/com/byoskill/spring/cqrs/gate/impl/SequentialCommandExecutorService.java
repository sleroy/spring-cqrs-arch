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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.byoskill.spring.cqrs.api.HandlersProvider;
import com.byoskill.spring.cqrs.api.ICommandCallback;
import com.byoskill.spring.cqrs.api.ICommandExecutionListener;
import com.byoskill.spring.cqrs.api.ICommandHandler;
import com.byoskill.spring.cqrs.api.ICommandProfilingService;
import com.byoskill.spring.cqrs.gate.api.CommandExecutionException;
import com.byoskill.spring.cqrs.gate.api.CommandHandlerNotFoundException;
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
public class SequentialCommandExecutorService {

    @Autowired
    private CqrsConfiguration		configuration;

    @Autowired
    private HandlersProvider		handlersProvider;

    @Autowired
    private ICommandExecutionListener[]	listeners;

    @Autowired
    private ICommandProfilingService	profilingService;

    /**
     * Instantiates a new sequential command executor service.
     */
    public SequentialCommandExecutorService() {
	super();
    }

    /**
     * Instantiates a new sequential command executor service.
     *
     * @param _configuration
     *            the configuration
     */
    public SequentialCommandExecutorService(final CqrsConfiguration _configuration) {
	configuration = _configuration;
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
	if (!objectValidation.isValid(command)) {
	    throw new InvalidCommandException(command);
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
	    notifyListenersFailure(command, e);
	    throw new CommandExecutionException(e);
	}
	return result;
    }

    public void setConfiguration(final CqrsConfiguration _configuration) {
	configuration = _configuration;
    }

    public void setListeners(final ICommandExecutionListener[] _listeners) {
	listeners = _listeners;
    }

    private void notifyListenersFailure(final Object command, final Throwable e) {
	for (final ICommandExecutionListener commandExecutionListener : listeners) {
	    commandExecutionListener.onFailure(command, e);
	}
    }

    private void notifyListenersSuccess(final Object command, final Object result) {
	for (final ICommandExecutionListener commandExecutionListener : listeners) {
	    commandExecutionListener.onSuccess(command, result);
	}
    }
}