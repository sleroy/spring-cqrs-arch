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
package com.byoskill.spring.cqrs.gate.api;

import com.byoskill.spring.cqrs.api.CommandExecutionContext;

@FunctionalInterface
public interface CommandExceptionHandler {

    /**
     * Handle exception.
     *
     * @param context
     *            the context where occured the exception.
     * @param exception
     *            the exception
     * @throws RuntimeException
     *             the runtime exception
     */
    public void handleException(CommandExecutionContext context, Throwable exception) throws RuntimeException;
}
