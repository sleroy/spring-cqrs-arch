/**
 * Copyright (C) 2017 Sylvain Leroy - BYOS Company All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the MIT license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the MIT license with
 * this file. If not, please write to: contact@sylvainleroy.com, or visit : https://sylvainleroy.com
 */
package com.byoskill.spring.cqrs.gate.api;

/**
 * This exception is thrown when the callback is failing.
 *
 * @author sleroy
 *
 */
public class CallBackException extends CqrsException {

    /**
     * Instantiates a new call back exception.
     *
     * @param _tCallBackException the t call back exception
     * @param _e the e
     */
    public CallBackException(final Throwable _tCallBackException, final Throwable _e) {
	super(_tCallBackException.getMessage(), _e);
    }

}
