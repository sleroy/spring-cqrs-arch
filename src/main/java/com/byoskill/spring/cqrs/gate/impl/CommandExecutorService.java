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
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.byoskill.spring.cqrs.annotations.Throttle;
import com.byoskill.spring.cqrs.api.HandlersProvider;
import com.byoskill.spring.cqrs.api.ICommandExecutionListener;
import com.byoskill.spring.cqrs.api.ICommandHandler;
import com.byoskill.spring.cqrs.api.ICommandProfilingService;
import com.byoskill.spring.cqrs.api.IThrottlingInterface;
import com.byoskill.spring.cqrs.gate.api.ICommandExceptionHandler;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandExecutorService.class);

    private final Optional<ICommandExceptionHandler> commandExceptionHandler;

    private CqrsConfiguration configuration;

    private final DefaultExceptionHandler defaultExceptionHandler;

    private final HandlersProvider handlersProvider;

    private ICommandExecutionListener[] listeners;


    private final ObjectValidation objectValidation;

    private final ICommandProfilingService profilingService;

    private final ForkJoinPool threadPool;

    private final IThrottlingInterface throttlingInterface;

    /**
     * Instantiates a new sequential command executor service.
     *
     * @param configuration            the configuration
     * @param handlersProvider            the handlers provider
     * @param listeners            the listeners
     * @param profilingService            the profiling service
     * @param commandExceptionHandler            the command exception handler
     * @param objectValidation            the object validation
     * @param throttlingInterface            the throttling interface
     * @param threadPoolTaskExecutor the thread pool task executor
     */
    @Autowired
    public CommandExecutorService(final CqrsConfiguration configuration, final HandlersProvider handlersProvider,
	    final ICommandExecutionListener[] listeners, final ICommandProfilingService profilingService,
	    final Optional<ICommandExceptionHandler> commandExceptionHandler, final ObjectValidation objectValidation,
	    final IThrottlingInterface throttlingInterface,
	    @Qualifier("cqrs-executor") final ForkJoinPool threadPoolTaskExecutor) {
	super();
	this.configuration = configuration;
	this.handlersProvider = handlersProvider;
	this.listeners = listeners;
	this.profilingService = profilingService;
	this.commandExceptionHandler = commandExceptionHandler;
	this.throttlingInterface = throttlingInterface;
	threadPool = threadPoolTaskExecutor;
	defaultExceptionHandler = new DefaultExceptionHandler();
	this.objectValidation = objectValidation;

    }

    @PreDestroy
    public void destroy() {
	LOGGER.warn("Closing CQRS Thread pool");
	try {
	    threadPool.awaitTermination(1, TimeUnit.MINUTES);
	} catch (final InterruptedException e) {
	    LOGGER.error("One or more threads didn't finish correctly : {}", e.getMessage(), e);
	}
	threadPool.shutdown();
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
	final ICommandHandler<?, ?> handler = handlersProvider.getHandler(command);
	LOGGER.debug("Lauching the command {} with the expected type {}", command, expectedType);

	final CommandRunner commandRunner = new CommandRunner(handler, objectValidation, expectedType);
	commandRunner.setListeners(listeners);
	commandRunner.setCommandExceptionHandler(commandExceptionHandler);
	commandRunner.setDefaultExceptionHandler(defaultExceptionHandler);
	commandRunner.setCommand(command);

	// You can add Your own capabilities here: dependency injection,
	// security, transaction management, logging, profiling, spying,
	// storing
	// commands, etc);

	// Decorate with throttling
	final Throttle throttle = command.getClass().getAnnotation(Throttle.class);
	if (throttle != null) {
	    commandRunner.throttle(() -> {
		LOGGER.debug("Requiring permit from rate limiter named {}", throttle.value());
		throttlingInterface.acquirePermit(throttle.value());
	    });
	}

	// Decorate with profiling

	if (configuration.isProfilingEnabled()) {
	    IProfiler profiler = null;
	    profiler = profilingService.newProfiler(handler);
	    final IProfiler p = profiler; // Scopes
	    commandRunner.setProfiler(profiler);

	}
	return CompletableFuture.supplyAsync(commandRunner, threadPool);
    }

    public void setConfiguration(final CqrsConfiguration _configuration) {
	configuration = _configuration;
    }

    public void setListeners(final ICommandExecutionListener[] _listeners) {
	listeners = _listeners;
    }


}