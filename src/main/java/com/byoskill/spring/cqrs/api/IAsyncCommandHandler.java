/*
 * Copyright (C) 2017 Sylvain Leroy - BYOS Company All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the MIT license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the MIT license with
 * this file. If not, please write to: , or visit :
 */
/**
 *
 */
package com.byoskill.spring.cqrs.api;

import java.util.concurrent.CompletableFuture;

import com.byoskill.spring.cqrs.annotations.CommandOptions;

/**
 * The Interface ICommandHandler is the interface to be implemented to add a new
 * command handler.
 *
 * @author Slawek
 * @author sleroy
 * @param <C>
 *            command
 * @param <R>
 *            result type - for asynchronous {@link CommandOptions}commands
 *            (asynchronous=true) should be {@link Void}
 */

public interface IAsyncCommandHandler<C, R> {

    /**
     * Handle.
     *
     * @param command            the command
     * @return the returned type
     * @throws RuntimeException the runtime exception
     */
    public CompletableFuture<R> handle(C command) throws RuntimeException;
}
