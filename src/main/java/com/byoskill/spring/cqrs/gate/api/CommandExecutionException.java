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
 * The Class CommandExecutionException is a wrapper exception for any exception occured inside the CQRS Module
 */
public class CommandExecutionException extends CqrsException {

    public CommandExecutionException(final Object _command, final Throwable _e) {
	super("Command " + _command + " has failed", _e);
    }

    public CommandExecutionException(final Throwable cause) {
	super(cause);
    }

}
