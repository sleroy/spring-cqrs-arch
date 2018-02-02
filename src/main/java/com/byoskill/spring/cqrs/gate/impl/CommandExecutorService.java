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
import java.util.concurrent.CompletableFuture;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.byoskill.spring.cqrs.api.HandlersProvider;
import com.byoskill.spring.cqrs.api.IAsyncCommandHandler;
import com.byoskill.spring.cqrs.api.ICommandExecutionListener;
import com.byoskill.spring.cqrs.api.ICommandProfilingService;
import com.byoskill.spring.cqrs.gate.api.CommandHandlerNotFoundException;
import com.byoskill.spring.cqrs.gate.api.ICommandExceptionContext;
import com.byoskill.spring.cqrs.gate.api.ICommandExceptionHandler;
import com.byoskill.spring.cqrs.gate.api.InvalidCommandException;
import com.byoskill.spring.cqrs.gate.conf.CqrsConfiguration;
import com.byoskill.spring.cqrs.utils.validation.ObjectValidation;

/**
 * This class prepares the command to be executed. It can override the default
 * command handler with a wrapper with enhanced functionalities. It Executes
 * SEQUENTIALLY the commands.
 *
 * @author Slawek
 */
@Service
public class CommandExecutorService {

    private final class ICommandExceptionContextImplementation implements ICommandExceptionContext {
	private final Object			 command;
	private final Throwable			 e;
	private final IAsyncCommandHandler<?, ?> handler;

	ICommandExceptionContextImplementation(final Object command, final Throwable e,
		final IAsyncCommandHandler<?, ?> handler) {
	    this.command = command;
	    this.e = e;
	    this.handler = handler;
	}

	@Override
	public Object getCommand() {
	    return command;
	}

	@Override
	public Throwable getException() {

	    return e;
	}

	@Override
	public Object getHandler() {
	    return handler;
	}
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandExecutorService.class);

    private final Optional<ICommandExceptionHandler> commandExceptionHandler;

    private CqrsConfiguration configuration;

    private final DefaultExceptionHandler defaultExceptionHandler;

    private final HandlersProvider handlersProvider;

    private ICommandExecutionListener[] listeners;

    private final ObjectValidation objectValidation;

    private final ICommandProfilingService profilingService;

    /**
     * Instantiates a new sequential command executor service.
     *
     * @param configuration            the configuration
     * @param handlersProvider            the handlers provider
     * @param listeners            the listeners
     * @param profilingService            the profiling service
     * @param commandExceptionHandler            the command exception handler
     * @param objectValidation the object validation
     */
    @Autowired
    public CommandExecutorService(final CqrsConfiguration configuration, final HandlersProvider handlersProvider,
	    final ICommandExecutionListener[] listeners, final ICommandProfilingService profilingService,
	    final Optional<ICommandExceptionHandler> commandExceptionHandler, final ObjectValidation objectValidation) {
	super();
	this.configuration = configuration;
	this.handlersProvider = handlersProvider;
	this.listeners = listeners;
	this.profilingService = profilingService;
	this.commandExceptionHandler = commandExceptionHandler;
	defaultExceptionHandler = new DefaultExceptionHandler();
	this.objectValidation = objectValidation;
    }

    /**
     * Executes a command in synchronous way.
     *
     * @param <R>
     *            the generic type
     * @param command
     *            the command
     * @param expectedType
     *            the expected type
     * @return the result of the command
     */
    public <R> CompletableFuture<R> run(final Object command, final Class<R> expectedType) {
	final IAsyncCommandHandler<Object, Object> handler = handlersProvider.getHandler(command);

	CompletableFuture<Object> promise = CompletableFuture.supplyAsync(() -> command);
	promise = promise.thenApply((c) -> {
	    commandValidation(c);

	    if (handler == null) {
		throw new CommandHandlerNotFoundException(command);
	    }
	    return c;
	});
	// You can add Your own capabilities here: dependency injection,
	// security, transaction management, logging, profiling, spying,
	// storing
	// commands, etc);

	// Decorate with profiling
	IProfiler profiler = null;
	if (configuration.isProfilingEnabled()) {
	    profiler = profilingService.newProfiler(handler);
	    final IProfiler p = profiler; // Scopes
	    promise = promise.thenCompose((c) -> p.begin(c));

	}
	final R result = null;
	// Promise chaining listeners begin
	promise = promise.thenCompose(c -> notifyListenersBegin(c, handler));
	// Promise command handler (now argument is the returned type)
	promise = promise.thenCompose(c -> handler.handle(c)); // handle
	// handle result
	promise.handle((r, e) -> {
	    if (e != null) {

		notifyListenersFailure(command, e, handler);
		return expectedType.cast(r);
	    } else {
		notifyListenersSuccess(command, r);
		return expectedType.cast(r);
	    }
	});

	return (CompletableFuture<R>) promise;
    }

    public void setConfiguration(final CqrsConfiguration _configuration) {
	configuration = _configuration;
    }

    public void setListeners(final ICommandExecutionListener[] _listeners) {
	listeners = _listeners;
    }

    private void commandValidation(final Object command) {

	LOGGER.debug("Validation of the command {}", command);
	try {
	    objectValidation.validate(command);
	} catch (final ConstraintViolationException e) {
	    throw new InvalidCommandException(command, e);
	}
    }

    private CompletableFuture<Object> notifyListenersBegin(final Object command, final Object commandHandler) {
	return CompletableFuture.supplyAsync(() -> {
	    for (final ICommandExecutionListener commandExecutionListener : listeners) {
		commandExecutionListener.beginExecution(command, commandHandler);
	    }
	    return command;
	});
    }

    private void notifyListenersFailure(final Object command, final Throwable e,
	    final IAsyncCommandHandler<?, ?> handler) {
	final ICommandExceptionContext exceptionContext = new ICommandExceptionContextImplementation(command, e,
		handler);
	for (final ICommandExecutionListener commandExecutionListener : listeners) {
	    commandExecutionListener.onFailure(command, exceptionContext);
	}
	// The command exception handler may wrap exceptions or rethrow it
	if (commandExceptionHandler.isPresent()) {

	    commandExceptionHandler.get().handleException(exceptionContext);
	} else {
	    defaultExceptionHandler.handleException(exceptionContext);
	}
    }

    private void notifyListenersSuccess(final Object command, final Object result) {
	for (final ICommandExecutionListener commandExecutionListener : listeners) {
	    commandExecutionListener.onSuccess(command, result);
	}
    }
}