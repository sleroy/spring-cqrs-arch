/**
 * Copyright (C) 2017 Sylvain Leroy - BYOS Company All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the MIT license, which unfortunately won't be
 * written for another century.
 * <p>
 * You should have received a copy of the MIT license with
 * this file. If not, please write to: contact@sylvainleroy.com, or visit : https://sylvainleroy.com
 */
package com.byoskill.spring.cqrs.gate.api;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The gate is responsible to execute the commands passed by the application. During the command execution; several
 * steps may interven as logging, tracing; profiling; exception handling; ecvent handling etc.
 * A command will be wrapped inside a {@link com.byoskill.spring.cqrs.executors.api.CommandRunnerChain},
 * this pipeline defining a list of tasks to be executed before and after the proper execution of the command;
 *
 * A command may be executed synchronously or asynchronously.
 * The command handler is defined using {@link com.byoskill.spring.cqrs.annotations.CommandService} and {@link com.byoskill.spring.cqrs.api.CommandServiceSpec}
 *
 * @author Slawek
 * @author sleroy
 */
public interface Gate {

    /**
     * Dispatch a command and executes it sequentially.
     *
     * @param <R>     the generic type
     * @param command the command.
     * @return the result of the command.
     */
    public <R> R dispatch(Object command);

    /**
     * Dispatch a command and executes it sequentially.
     *
     * @param <R>        the generic type
     * @param command    the command.
     * @param returnType the expected return type
     * @return the result of the command.
     */
    public <R> R dispatch(Object command, Class<R> returnType);

    /**
     * Dispatch a list of command asynchronously.
     *
     * @param <R>                the generic type
     * @param commands           the commands
     * @param expectedReturnType the expected return type
     * @return the result of the command.
     */
    public <R> List<R> dispatchAll(List<?> commands, Class<R> expectedReturnType);


    /**
     * Dispatch a command and executes it asynchronously.
     *
     * @param <R>     the generic type
     * @param command the command.
     * @return the result of the command.
     */
    public <R> CompletableFuture<R> dispatchAsync(Object command);

    /**
     * Dispatch a command and executes it asynchronously.
     *
     * @param <R>                the generic type
     * @param command            the command.
     * @param expectedReturnType the expected return type
     * @return the result of the command.
     */
    public <R> CompletableFuture<R> dispatchAsync(Object command, Class<R> expectedReturnType);

    /**
     * Dispatches an event and executes it asynchronously.
     *
     * @param _event the event.
     */
    public void dispatchEvent(Object _event);
}