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
/**
 *
 */
package com.byoskill.spring.cqrs.api;

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
@FunctionalInterface
public interface CommandServiceSpec<C, R> {

    /**
     * Handle.
     *
     * @param command
     *            the command
     * @return the returned value
     * @throws RuntimeException
     *             the runtime exception
     */
    public R handle(C command) throws RuntimeException;
}
