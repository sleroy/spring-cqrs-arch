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
package com.byoskill.spring.cqrs.api;

/**
 * This interface defines how to implement a listener on the CQRS Module
 *
 * @author sleroy
 */
public interface CommandExecutionListener {

    /**
     * Begin execution of a command.
     *
     * @param context
     *            the command execution context
     */
    void beginExecution(CommandExecutionContext context);

    /**
     * Invoked when command handling execution resulted in an error.
     *
     * @param context
     *            the command execution context
     * @param cause
     *            the cause
     */
    void onFailure(CommandExecutionContext context, Throwable cause);

    /**
     * Invoked when command handling execution was successful.
     *
     * @param context
     *            the command execution context
     * @param result
     *            the result
     */
    void onSuccess(CommandExecutionContext context, Object result);
}