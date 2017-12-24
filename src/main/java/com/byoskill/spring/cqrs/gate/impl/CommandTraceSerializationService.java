/**
 * Copyright (C) 2017 Sylvain Leroy - BYOS Company All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the MIT license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the MIT license with
 * this file. If not, please write to: contact@sylvainleroy.com, or visit : https://sylvainleroy.com
 */

package com.byoskill.spring.cqrs.gate.impl;

import java.io.IOException;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.byoskill.spring.cqrs.api.ICommandExecutionListener;
import com.byoskill.spring.cqrs.gate.api.ICommandExceptionContext;
import com.byoskill.spring.cqrs.gate.conf.CqrsConfiguration;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This command listener provides a facility to log and serialize every actions
 * executed by the Gate in order to replay them in tests.
 *
 * @author sleroy
 *
 */
@Service
public class CommandTraceSerializationService implements ICommandExecutionListener {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandTraceSerializationService.class);

    /** The command trace. */
    private CommandTrace commandTrace = new CommandTrace();

    /** The configuration. */
    @Autowired
    private final CqrsConfiguration configuration;

    /** The object mapper. */
    private ObjectMapper objectMapper;

    /**
     * Instantiates a new command trace serialization service.
     *
     * @param cqrsConfiguration
     *            the cqrs configuration
     */
    public CommandTraceSerializationService(final CqrsConfiguration cqrsConfiguration) {
	configuration = cqrsConfiguration;
	init();

    }

    /**
     * Flush file.
     *
     * @throws JsonGenerationException
     *             the json generation exception
     * @throws JsonMappingException
     *             the json mapping exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public synchronized void flushFile() throws Exception {
	objectMapper.writeValue(configuration.getTraceFile(), commandTrace);
	commandTrace = new CommandTrace();
    }

    /**
     * Checks for traces.
     *
     * @return true, if successful
     */
    public boolean hasTraces() {
	return !commandTrace.commands.isEmpty();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.byoskill.spring.cqrs.api.ICommandExecutionListener#onFailure(java.
     * lang.Object, java.lang.Throwable)
     */
    @Override
    public void onFailure(final Object command, final ICommandExceptionContext cause) {
	if (configuration.isTracingEnabled()) {
	    serializeTrace(CommandExecution.failure( command, cause.getException()));
	}

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.byoskill.spring.cqrs.api.ICommandExecutionListener#onSuccess(java.
     * lang.Object, java.lang.Object)
     */
    @Override
    public void onSuccess(final Object command, final Object executionResult) {
	if (configuration.isTracingEnabled()) {
	    serializeTrace(CommandExecution.success( command,  executionResult));
	}

    }

    /**
     * Shutdown.
     */
    @PreDestroy
    public void shutdown() {
	try {
	    flushFile();
	} catch (final Exception e) {
	    LOGGER.error("Error during the serialization of the command trace {} -> {}", configuration.getTraceFile(),
		    e);
	}

    }

    /**
     * Inits the.
     */
    private final void init() {
	objectMapper = new ObjectMapper();
	try {
	    if (configuration.getTraceFile().createNewFile()) {
		final CommandTrace trace = new CommandTrace();
		objectMapper.writeValue(configuration.getTraceFile(), trace);
	    }
	} catch (final IOException e) {
	    LOGGER.error("Could not create the trace, already existing -> {}", e);
	}
    }

    /**
     * Serialize trace.
     *
     * @param _command
     *            the command
     */
    private synchronized void serializeTrace(final CommandExecution _command) {
	try {
	    commandTrace.addCommand(_command);
	    if (commandTrace.getCommands().size() >= configuration.getTraceSize()) {
		flushFile();
	    }
	} catch (final Exception e) {
	    LOGGER.error("Error during the serialization of the command {} -> {}", configuration.getTraceFile(),
		    _command, e);
	}
    }

}
