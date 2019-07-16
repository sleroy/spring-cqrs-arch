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
package com.byoskill.spring.cqrs.interceptors;

import com.byoskill.spring.cqrs.workflow.CommandExecutionContext;
import com.byoskill.spring.cqrs.workflow.CommandInterceptor;
import com.byoskill.spring.cqrs.workflow.CommandRunnerChain;
import com.byoskill.spring.cqrs.utils.validation.InvalidCommandException;
import com.byoskill.spring.cqrs.utils.validation.ObjectValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;

/**
 * This command is handling the validation phase in the execution of a command.
 */
public class CommandValidatingRunner implements CommandInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandValidatingRunner.class);
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
