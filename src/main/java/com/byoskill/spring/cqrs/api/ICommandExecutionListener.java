/**
 * Copyright (C) 2017 Sylvain Leroy - BYOS Company All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the MIT license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the MIT license with
 * this file. If not, please write to: contact@sylvainleroy.com, or visit : https://sylvainleroy.com
 */
package com.byoskill.spring.cqrs.api;

/**
 * This interface defines how to implement a listener on the CQRS Module
 *
 * @author sleroy
 */
public interface ICommandExecutionListener {

    /**
     *  Invoked when command handling execution resulted in an error.
     *
     * @param _command the command
     * @param cause the cause
     */
    void onFailure(Object _command, Throwable cause);

    /**
     *  Invoked when command handling execution was successful.
     *
     * @param _command the command
     * @param result the result
     */
    void onSuccess(Object _command, Object result);
}