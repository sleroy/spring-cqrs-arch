/*
 * Copyright (C) 2017 Sylvain Leroy - BYOS Company All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the MIT license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the MIT license with
 * this file. If not, please write to: , or visit :
 */
package com.byoskill.spring.cqrs.gate.api;

import javax.validation.ConstraintViolationException;

/**
 * The Class InvalidCommandException is thrown when an invalid command has been
 * sent.
 *
 * @author sleroy
 */
public class InvalidCommandException extends CqrsException {

    /**
     * Instantiates a new invalid command exception.
     *
     * @param _command
     *            the command
     * @param e
     *            the exception
     */
    public InvalidCommandException(final Object _command, final ConstraintViolationException e) {
	super("Command invalid : " + _command, e);
    }

    @Override
    public String toString() {
	return "InvalidCommandException [getCause()=" + getCause() + "]";
    }
}
