/*
 * Copyright (C) 2017 Sylvain Leroy - BYOSkill Company All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the MIT license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the MIT license with
 * this file. If not, please write to: sleroy at byoskill.com, or visit : www.byoskill.com
 *
 */
package com.byoskill.spring.cqrs.gate.impl;

import java.util.Optional;
import java.util.function.Supplier;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.byoskill.spring.cqrs.api.CommandExecutionContext;
import com.byoskill.spring.cqrs.api.CommandExecutionListener;
import com.byoskill.spring.cqrs.api.CommandServiceSpec;
import com.byoskill.spring.cqrs.api.RunnerState;
import com.byoskill.spring.cqrs.gate.api.CommandExceptionHandler;
import com.byoskill.spring.cqrs.gate.api.CommandHandlerNotFoundException;
import com.byoskill.spring.cqrs.gate.api.InvalidCommandException;
import com.byoskill.spring.cqrs.utils.validation.ObjectValidation;

public class CommandRunner<T, R> implements Supplier<R>, CommandExecutionContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandRunner.class);

    private T command;

    private Optional<CommandExceptionHandler> commandExceptionHandler;

    private final CommandServiceSpec<T, R> commandServiceSpec;

    private DefaultExceptionHandler defaultExceptionHandler;

    private final Class<?> expectedType;

    private CommandExecutionListener[] listeners;

    private final ObjectValidation objectValidation;

    private IProfiler profiler;

    private Runnable throttle = () -> {
    };

    private final RunnerState runnerState = new RunnerStateImpl();

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
    public CommandRunner(final CommandServiceSpec<T, R> commandHandler, final ObjectValidation objectValidation,
	    final Class<R> expectedType) {
	this.expectedType = expectedType;
	this.commandServiceSpec = commandHandler;
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
	    result = commandServiceSpec.handle(command);

	    // handle result
	    notifyListenersSuccess(result);
	    return (R) expectedType.cast(result);
	} catch (final Exception e) {
	    notifyListenersFailure(e, commandServiceSpec);

	} finally {
	    if (profiler != null) {
		profiler.end(command);
	    }
	    MDC.remove("command");
	}
	return result;
    }

    @Override
    public <T> T getCommand(final Class<T> impl) {
	return impl.cast(command);
    }

    @Override
    public Object getCommandHandler() {
	return commandServiceSpec;
    }

    @Override
    public Object getRawCommand() {
	return command;
    }

    @Override
    public RunnerState getRunnerState() {
	return runnerState;
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
    public void setCommandExceptionHandler(final Optional<CommandExceptionHandler> commandExceptionHandler) {
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
    public void setListeners(final CommandExecutionListener[] listeners) {
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
	return "CommandRunner [command=" + command + ", commandHandler=" + commandServiceSpec + ", expectedType="
		+ expectedType + "]";
    }

    private void notifyListenersBegin() {
	LOGGER.debug("Command {} being executed");
	for (final CommandExecutionListener commandExecutionListener : listeners) {
	    commandExecutionListener.beginExecution(this);
	}
    }

    /**
     * Notify listeners failure.
     *
     * @param e
     *            the e
     * @param commandHandler2
     *            the command handler 2
     */
    private void notifyListenersFailure(final Throwable e,
	    final CommandServiceSpec<T, R> commandHandler2) {
	LOGGER.debug("Command {} has failed => {}", command, e);
	for (final CommandExecutionListener commandExecutionListener : listeners) {
	    commandExecutionListener.onFailure(this, e);
	}
	// The command exception handler may wrap exceptions or rethrow it
	if (commandExceptionHandler.isPresent()) {

	    commandExceptionHandler.get().handleException(this, e);
	} else {
	    defaultExceptionHandler.handleException(this, e);
	}
    }

    /**
     * Notify listeners success.
     *
     * @param command
     *            the command
     */
    private void notifyListenersSuccess(final Object result) {
	LOGGER.debug("Command {} has been executed with success => {}", command, result);
	for (final CommandExecutionListener commandExecutionListener : listeners) {
	    commandExecutionListener.onSuccess(this, result);
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

	if (commandServiceSpec == null) {
	    throw new CommandHandlerNotFoundException(command);
	}

    }
}
