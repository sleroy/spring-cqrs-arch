/**
 * Copyright (C) 2017-2018 Credifix
 */
package com.byoskill.spring.cqrs.gate.impl;

import java.util.Optional;
import java.util.function.Supplier;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.byoskill.spring.cqrs.api.ICommandExecutionListener;
import com.byoskill.spring.cqrs.api.ICommandHandler;
import com.byoskill.spring.cqrs.gate.api.CommandHandlerNotFoundException;
import com.byoskill.spring.cqrs.gate.api.ICommandExceptionContext;
import com.byoskill.spring.cqrs.gate.api.ICommandExceptionHandler;
import com.byoskill.spring.cqrs.gate.api.InvalidCommandException;
import com.byoskill.spring.cqrs.utils.validation.ObjectValidation;

public class CommandRunner<T, R> implements Supplier<R> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandRunner.class);

    private T command;

    private Optional<ICommandExceptionHandler> commandExceptionHandler;

    private final ICommandHandler<T, R> commandHandler;

    private DefaultExceptionHandler defaultExceptionHandler;

    private final Class<?> expectedType;

    private ICommandExecutionListener[] listeners;

    private final ObjectValidation objectValidation;

    private IProfiler profiler;

    private Runnable throttle = () -> {
    };

    /**
     * Instantiates a new command runner.
     *
     * @param commandHandler
     *            the command handler
     * @param objectValidation
     *            the object validation
     * @param expectedType
     *            the expected type
     */
    public CommandRunner(final ICommandHandler<T, R> commandHandler, final ObjectValidation objectValidation,
	    final Class<R> expectedType) {
	this.expectedType = expectedType;
	this.commandHandler = commandHandler;
	this.objectValidation = objectValidation;

    }

    @Override
    public R get() {
	validate(command);
	// You can add Your own capabilities here: dependency injection,
	// security, transaction management, logging, profiling, spying,
	// storing
	// commands, etc);

	R result = null;
	try {
	    MDC.put("command", command.getClass().getName());

	    // Throtting if necessary
	    throttle.run();

	    notifyListenersBegin();

	    // Decorate with profiling
	    if (profiler != null) {
		profiler.begin(command);
	    }

	    // Promise command handler (now argument is the returned type)
	    result = commandHandler.handle(command);

	    // handle result
	    notifyListenersSuccess(command, result);
	    return (R) expectedType.cast(result);
	} catch (final Exception e) {
	    notifyListenersFailure(command, e, commandHandler);

	} finally {
	    if (profiler != null) {
		profiler.end(command);
	    }
	    MDC.remove("command");
	}
	return result;
    }

    public void setCommand(final T command) {
	this.command = command;
    }

    /**
     * Sets the command exception handler.
     *
     * @param commandExceptionHandler
     *            the new command exception handler
     */
    public void setCommandExceptionHandler(final Optional<ICommandExceptionHandler> commandExceptionHandler) {
	this.commandExceptionHandler = commandExceptionHandler;

    }

    /**
     * Sets the default exception handler.
     *
     * @param defaultExceptionHandler
     *            the new default exception handler
     */
    public void setDefaultExceptionHandler(final DefaultExceptionHandler defaultExceptionHandler) {
	this.defaultExceptionHandler = defaultExceptionHandler;

    }

    /**
     * Sets the listeners.
     *
     * @param listeners
     *            the new listeners
     */
    public void setListeners(final ICommandExecutionListener[] listeners) {
	this.listeners = listeners;

    }

    /**
     * Sets the profiler.
     *
     * @param profiler
     *            the new profiler
     */
    public void setProfiler(final IProfiler profiler) {
	this.profiler = profiler;

    }

    /**
     * Adding the throttle mechanism
     *
     * @param throttle
     *            the throttle
     */
    public void throttle(final Runnable throttle) {
	this.throttle = throttle;

    }

    @Override
    public String toString() {
	return "CommandRunner [command=" + command + ", commandHandler=" + commandHandler + ", expectedType="
		+ expectedType + "]";
    }

    private void notifyListenersBegin() {
	LOGGER.debug("Command {} being executed");
	for (final ICommandExecutionListener commandExecutionListener : listeners) {
	    commandExecutionListener.beginExecution(command, commandHandler);
	}
    }

    /**
     * Notify listeners failure.
     *
     * @param command
     *            the command
     * @param e
     *            the e
     * @param commandHandler2
     *            the command handler 2
     */
    private void notifyListenersFailure(final T command, final Throwable e,
	    final ICommandHandler<T, R> commandHandler2) {
	LOGGER.debug("Command {} has failed => {}", command, e);
	final ICommandExceptionContext exceptionContext = new CommandExceptionContextImplementation(command, e,
		commandHandler);
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

    /**
     * Notify listeners success.
     *
     * @param command
     *            the command
     * @param result
     *            the result
     */
    private void notifyListenersSuccess(final Object command, final R result) {
	LOGGER.debug("Command {} has been executed with success => {}", command, result);
	for (final ICommandExecutionListener commandExecutionListener : listeners) {
	    commandExecutionListener.onSuccess(command, result);
	}
    }

    /**
     * Validate the command
     *
     * @param command
     *            the command
     */
    private void validate(final Object command) {
	LOGGER.debug("Validation of the command {}", command);
	try {
	    objectValidation.validate(command);
	} catch (final ConstraintViolationException e) {
	    throw new InvalidCommandException(command, e);
	}

	if (commandHandler == null) {
	    throw new CommandHandlerNotFoundException(command);
	}

    }
}
