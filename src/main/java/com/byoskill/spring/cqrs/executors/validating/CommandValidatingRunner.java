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
package com.byoskill.spring.cqrs.executors.validating;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.byoskill.spring.cqrs.executors.api.CommandExecutionContext;
import com.byoskill.spring.cqrs.executors.api.CommandRunner;
import com.byoskill.spring.cqrs.executors.api.CommandRunnerChain;
import com.byoskill.spring.cqrs.gate.api.InvalidCommandException;
import com.byoskill.spring.cqrs.utils.validation.ObjectValidation;

public class CommandValidatingRunner implements CommandRunner {
    private static final Logger	   LOGGER = LoggerFactory.getLogger(CommandValidatingRunner.class);
    private final ObjectValidation objectValidation;

    @Autowired
    public CommandValidatingRunner(final ObjectValidation objectValidation) {
	this.objectValidation = objectValidation;

    }

    @Override
    public Object execute(final CommandExecutionContext context, final CommandRunnerChain chain)
	    throws RuntimeException {
	final Object command = context.getRawCommand();
	LOGGER.debug("Validation of the command {}", command);
	try {
	    objectValidation.validate(command);
	} catch (final ConstraintViolationException e) {
	    throw new InvalidCommandException(command, e);
	}

	return chain.execute(context);
    }

}
