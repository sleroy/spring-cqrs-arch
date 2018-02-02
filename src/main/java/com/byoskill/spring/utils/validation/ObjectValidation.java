/*
 * Copyright (C) 2017 Sylvain Leroy - BYOS Company All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the MIT license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the MIT license with
 * this file. If not, please write to: , or visit :
 */
package com.byoskill.spring.utils.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.stereotype.Component;

import com.byoskill.spring.cqrs.api.CommandNotValidException;

/**
 * This class is used to performs manual validation of pojo.
 *
 * @author sleroy
 *
 */
@Component
public class ObjectValidation {

    private final Validator validator;

    /**
     * Instantiates a new object validation.
     *
     * @param _validator
     *            the validator
     */
    public ObjectValidation(final Validator _validator) {
	super();
	validator = _validator;
    }

    /**
     * Checks if is valid.
     *
     * @param _object the object
     * @return true, if is valid
     */
    public boolean isValid(final Object _object) {
	final Set<ConstraintViolation<Object>> constraints = validator.validate(_object);
	return constraints.isEmpty();
    }

    /**
     * Validate.
     *
     * @param _object the object
     */
    public void validate(final Object _object) {
	final Set<ConstraintViolation<Object>> constraints = validator.validate(_object);
	if (!constraints.isEmpty()) {
	    throw new CommandNotValidException("Object " + _object + " + is not valid", constraints);
	}
    }
}
