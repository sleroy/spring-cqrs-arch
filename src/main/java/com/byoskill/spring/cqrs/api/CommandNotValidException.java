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

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * The Class CommandNotValidException is thrown when an invalid command has been
 * passed through the gate.
 */
public class CommandNotValidException extends ConstraintViolationException {

    private static String toString(final Set<? extends ConstraintViolation<?>> constraintViolations) {
	return constraintViolations.stream()
		.map(cv -> cv == null ? "null" : cv.getPropertyPath() + ": " + cv.getMessage())
		.collect(Collectors.joining("\n"));
    }

    /**
     * Instantiates a new command not valid exception.
     *
     * @param constraintViolations
     *            the constraint violations
     */
    public CommandNotValidException(final Set<? extends ConstraintViolation<?>> constraintViolations) {
	super(constraintViolations);
    }

    /**
     * Instantiates a new command not valid exception.
     *
     * @param message
     *            the message
     * @param constraintViolations
     *            the constraint violations
     */
    public CommandNotValidException(final String message,
	    final Set<? extends ConstraintViolation<?>> constraintViolations) {
	super(message + "\nFailed validations : \n" + toString(constraintViolations), constraintViolations);

    }

    @Override
    public String toString() {
	return "CommandNotValidException [getConstraintViolations()=" + getConstraintViolations() + ", getMessage()="
		+ getMessage() + "]";
    }
}
