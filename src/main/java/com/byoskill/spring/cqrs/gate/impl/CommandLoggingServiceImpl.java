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
package com.byoskill.spring.cqrs.gate.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.byoskill.spring.cqrs.api.CommandExecutionContext;
import com.byoskill.spring.cqrs.api.CommandExecutionListener;
import com.byoskill.spring.cqrs.api.LoggingConfiguration;

public class CommandLoggingServiceImpl implements CommandExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandLoggingServiceImpl.class);

    private final LoggingConfiguration configuration;

    /**
     * Instantiates a new command logging service.
     *
     * @param configuration
     *            the configuration
     */
    @Autowired
    public CommandLoggingServiceImpl(final LoggingConfiguration configuration) {
	super();
	this.configuration = configuration;
    }

    @Override
    public void beginExecution(final CommandExecutionContext commandExecutionContext) {
	if (configuration.isLoggingEnabled()) {
	    LOGGER.info("Command to be executed : {} with {}", commandExecutionContext.getRawCommand(),
		    commandExecutionContext.getCommandHandler());
	}

    }

    @Override
    public void onFailure(final CommandExecutionContext context, final Throwable cause) {
	if (configuration.isLoggingEnabled()) {
	    final Object command = context.getRawCommand();

	    if (cause != null) {
		LOGGER.error("Command {} has failed with informations {} for the reason {}",
			command.getClass().getName(),
			command,
			cause);
	    } else {
		LOGGER.error("Command {} with informations {} has failed.", command.getClass().getName(), command);
	    }
	}

    }

    @Override
    public void onSuccess(final CommandExecutionContext commandExecutionContext, final Object result) {
	if (configuration.isLoggingEnabled()) {
	    LOGGER.info("Command has been executed with success {} with the result {}",
		    commandExecutionContext.getRawCommand(), result);
	}

    }

}
