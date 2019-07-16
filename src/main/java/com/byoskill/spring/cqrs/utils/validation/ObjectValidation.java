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
package com.byoskill.spring.cqrs.utils.validation;

import com.byoskill.spring.cqrs.commands.CommandNotValidException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * This class is used to performs manual validation of pojo.
 *
 * @author sleroy
 */
public class ObjectValidation {

    private final Validator validator;

    /**
     * Instantiates a new Object validation.
     */
    public ObjectValidation() {
        this(Validation.buildDefaultValidatorFactory().getValidator());
    }

    /**
     * Instantiates a new object validation.
     *
     * @param _validator the validator
     */
    @Autowired
    public ObjectValidation(final Validator _validator) {
        super();
        validator = _validator;
    }

    /**
     * Checks if is valid.
     *
     * @param _object the object
     *
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
            throw new CommandNotValidException("An object cannot be validated.\n Object : " + _object, constraints);
        }
    }
}
