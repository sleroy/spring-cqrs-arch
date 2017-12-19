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
 * Default interface to implement a Profile service.
 * @author sleroy
 *
 */
public interface ICommandProfilingService {

    /**
     * Decorate.
     *
     * @param <R> the generic type
     * @param _command the command
     * @param _callback the callback
     * @return the command callback
     */
    <R> ICommandCallback<R> decorate(Object _command, ICommandCallback<R> _callback);

}
