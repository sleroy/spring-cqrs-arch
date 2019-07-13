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

import com.byoskill.spring.cqrs.gate.api.EventBusService;
import com.byoskill.spring.cqrs.gate.api.Gate;
import com.byoskill.spring.cqrs.gate.api.InvalidCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * This class defines the gate where the commands are dispatched for execution.
 *
 * @author sleroy
 */
public class SpringGate implements Gate {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringGate.class);

    private final CommandExecutorServiceImpl commandExecutorServiceImpl;
    private final EventBusService eventBusService;

    private final Optional<SpringGateFilters> springGateFilters;

    /**
     * Instantiates a new spring gate.
     *
     * @param commandExecutorServiceImpl the command executor service
     * @param eventBusService            the event bus service
     */
    @Autowired
    public SpringGate(final CommandExecutorServiceImpl commandExecutorServiceImpl,
                      final EventBusService eventBusService, final Optional<SpringGateFilters> springGateFilters) {
        super();
        this.commandExecutorServiceImpl = commandExecutorServiceImpl;
        this.eventBusService = eventBusService;
        this.springGateFilters = springGateFilters;
    }

    /**
     * Executes sequentially a command
     */
    @Override
    public <R> R dispatch(final Object _command) {

        return (R) commandExecutorServiceImpl.run(filterCommand(_command), Object.class).join();

    }

    @Override
    public <R> R dispatch(final Object command, final Class<R> returnType) {
        return returnType.cast(commandExecutorServiceImpl.run(filterCommand(command), returnType).join());
    }

    @Override
    public <R> List<R> dispatchAll(final List<?> commands, final Class<R> expectedReturnType) {

        final List<CompletableFuture<R>> futures = new ArrayList<>();
        for (final Object command : commands) {
            futures.add(this.dispatchAsync(command, expectedReturnType));
        }
        final CompletableFuture<R>[] array = futures.toArray(new CompletableFuture[0]);
        CompletableFuture.allOf(array).whenComplete((aVoid, thr) -> {
            if (thr == null) {
                LOGGER.debug("Execution of the tasks executed with success");
            } else {
                LOGGER.error("Execution of the tasks has failed : {}", thr.getMessage(), thr);
            }
        }).join();

        return futures.stream().map(future -> future.join()).collect(Collectors.toList());

    }

    /*
     * (non-Javadoc)
     *
     * @see com.byoskill.spring.cqrs.gate.api.Gate#dispatchAsync(java.lang.Object)
     */
    @Override
    public <R> CompletableFuture<R> dispatchAsync(final Object command) {
        return (CompletableFuture<R>) commandExecutorServiceImpl.run(filterCommand(command), Object.class);

    }

    /*
     * (non-Javadoc)
     *
     * @see com.byoskill.spring.cqrs.gate.api.Gate#dispatchAsync(java.lang.Object,
     * java.lang.Class)
     */
    @Override
    public <R> CompletableFuture<R> dispatchAsync(final Object command, final Class<R> expectedReturnType) {
        return commandExecutorServiceImpl.run(filterCommand(command), expectedReturnType);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.byoskill.spring.cqrs.gate.api.Gate#dispatchEvent(java.lang.Object)
     */
    @Override
    public void dispatchEvent(final Object _event) {
        LOGGER.trace("Received event {}", _event);
        eventBusService.publishEvent(_event);
    }

    private Object filterCommand(final Object _command) {
        if (springGateFilters.isPresent()) {
            final Optional<Object> filterCommand = springGateFilters.get().filterCommand(_command);
            if (filterCommand.isPresent()) {
                return filterCommand(_command);
            } else {
                throw new InvalidCommandException(_command,
                        new ConstraintViolationException("Command filtered", Collections.emptySet()));
            }
        } else {
            return _command;
        }
    }

}
