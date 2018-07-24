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

import java.io.IOException;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.byoskill.spring.cqrs.api.CommandExecutionContext;
import com.byoskill.spring.cqrs.api.CommandExecutionListener;
import com.byoskill.spring.cqrs.api.TraceConfiguration;
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
public class CommandTraceSerializationServiceImpl implements CommandExecutionListener {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandTraceSerializationServiceImpl.class);

    /** The command trace. */
    private CommandTrace commandTrace = new CommandTrace();

    /** The configuration. */

    private final TraceConfiguration traceConfiguration;

    /** The object mapper. */
    private ObjectMapper objectMapper;

    /**
     * Instantiates a new command trace serialization service.
     *
     * @param traceConfiguration
     *            the cqrs configuration
     */
    @Autowired
    public CommandTraceSerializationServiceImpl(final TraceConfiguration traceConfiguration) {
	this.traceConfiguration = traceConfiguration;
	init();

    }

    @Override
    public void beginExecution(final CommandExecutionContext context) {
	// Nothing to do

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
	objectMapper.writeValue(traceConfiguration.getTraceFile(), commandTrace);
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

    @Override
    public void onFailure(final CommandExecutionContext context, final Throwable cause) {
	if (traceConfiguration.isTracingEnabled()) {
	    serializeTrace(CommandExecution.failure(context.getRawCommand(), cause));
	}

    }

    @Override
    public void onSuccess(final CommandExecutionContext context, final Object result) {
	if (traceConfiguration.isTracingEnabled()) {
	    serializeTrace(CommandExecution.success(context.getRawCommand(), result));
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
	    LOGGER.error("Error during the serialization of the command trace {} -> {}",
		    traceConfiguration.getTraceFile(),
		    e);
	}

    }

    /**
     * Inits the.
     */
    private final void init() {
	objectMapper = new ObjectMapper();
	try {
	    if (traceConfiguration.getTraceFile().createNewFile()) {
		final CommandTrace trace = new CommandTrace();
		objectMapper.writeValue(traceConfiguration.getTraceFile(), trace);
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
	    if (commandTrace.getCommands().size() >= traceConfiguration.getTraceSize()) {
		flushFile();
	    }
	} catch (final Exception e) {
	    LOGGER.error("Error during the serialization of the command {} -> {}", traceConfiguration.getTraceFile(),
		    _command, e);
	}
    }

}
