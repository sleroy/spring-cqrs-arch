/**
 * Copyright (C) 2017-2018 Credifix
 */
package com.byoskill.spring.cqrs.api;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * The Class CommandNotValidException is thrown when an invalid command has been passed through the gate.
 */
public class CommandNotValidException extends ConstraintViolationException {

    /**
     * Instantiates a new command not valid exception.
     *
     * @param constraintViolations the constraint violations
     */
    public CommandNotValidException(final Set<? extends ConstraintViolation<?>> constraintViolations) {
	super(constraintViolations);
    }

    /**
     * Instantiates a new command not valid exception.
     *
     * @param message the message
     * @param constraintViolations the constraint violations
     */
    public CommandNotValidException(final String message,
	    final Set<? extends ConstraintViolation<?>> constraintViolations) {
	super(message, constraintViolations);

    }

    @Override
    public String toString() {
	return "CommandNotValidException [getConstraintViolations()=" + getConstraintViolations() + ", getMessage()="
		+ getMessage() + "]";
    }

}
