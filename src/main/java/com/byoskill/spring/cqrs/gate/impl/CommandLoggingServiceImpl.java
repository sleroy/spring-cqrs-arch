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

import com.byoskill.spring.cqrs.api.CommandExecutionListener;
import com.byoskill.spring.cqrs.api.LoggingConfiguration;
import com.byoskill.spring.cqrs.gate.api.CommandExceptionContext;

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
    public void beginExecution(final Object command, final Object commandHandler) {
	if (configuration.isLoggingEnabled()) {
	    LOGGER.info("Command to be executed : {} with {}", command, commandHandler);
	}

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.byoskill.spring.cqrs.api.ICommandExecutionListener#onFailure(java.lang.
     * Object, java.lang.Throwable)
     */
    @Override
    public void onFailure(final Object _command, final CommandExceptionContext _cause) {
	if (configuration.isLoggingEnabled()) {
	    if (_cause != null) {
		LOGGER.error("Command {} has failed with informations {} for the reason {}",
			_command.getClass().getName(),
			_command,
			_cause.getException());
	    } else {
		LOGGER.error("Command {} with informations {} has failed.", _command.getClass().getName(), _command);
	    }
	}

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.byoskill.spring.cqrs.api.ICommandExecutionListener#onSuccess(java.lang.
     * Object, java.lang.Object)
     */
    @Override
    public void onSuccess(final Object _command, final Object _result) {
	if (configuration.isLoggingEnabled()) {
	    LOGGER.info("Command has been executed with success {} with the result {}", _command, _result);
	}

    }

}
