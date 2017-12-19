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
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * This class is used to performs manual validation of pojo.
 *
 * @author sleroy
 *
 */
public class ObjectValidation {

    public final static String URL_SAFE_PATTERN = "[\\w[-]?]*";

    private final Validator validator;

    /**
     * Instantiates a new object validation.
     */
    public ObjectValidation() {
	super();
	validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public boolean isValid(final Object _object) {
	final Set<ConstraintViolation<Object>> constraints = validator.validate(_object);
	return constraints.isEmpty();
    }

    public void validate(final Object _object) {
	final Set<ConstraintViolation<Object>> constraints = validator.validate(_object);
	if (!constraints.isEmpty()) {
	    throw new ConstraintViolationException("Object " + _object + " + is not valid", constraints);
	}
    }
}
