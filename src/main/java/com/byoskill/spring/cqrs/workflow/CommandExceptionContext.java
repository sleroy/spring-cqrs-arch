/**
 * Copyright (C) 2017 Sylvain Leroy - BYOS Company All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the MIT license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the MIT license with
 * this file. If not, please write to: contact@sylvainleroy.com, or visit : https://sylvainleroy.com
 */
package com.byoskill.spring.cqrs.workflow;

/**
 * This interface is defining the exception context.
 */
public interface CommandExceptionContext {

    /**
     * Gets the command.
     *
     * @return the command
     */
    Object getCommand();

    /**
     * Gets the exception.
     *
     * @return the exception
     */
    Throwable getException();

    /**
     * Gets the handler.
     *
     * @return the handler
     */
    Object getHandler();
}
