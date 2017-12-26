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
 * The Class CommandHandlerNotFoundException is thrown when a command handler has not been found.
 */
public class CommandHandlerNotFoundException extends CqrsException {

    /**
     * Instantiates a new command handler not found exception.
     *
     * @param _command the command
     */
    public CommandHandlerNotFoundException(final Object _command) {
	super("Could not execute the command : " + _command);
    }
}
