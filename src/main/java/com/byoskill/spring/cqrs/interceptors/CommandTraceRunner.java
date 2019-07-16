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

import com.byoskill.spring.cqrs.configuration.TraceConfiguration;
import com.byoskill.spring.cqrs.workflow.CommandExecutionContext;
import com.byoskill.spring.cqrs.workflow.CommandInterceptor;
import com.byoskill.spring.cqrs.workflow.CommandRunnerChain;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * This command listener provides a facility to log and serialize every actions
 * executed by the Gate in order to replay them in tests.
 *
 * @author sleroy
 */
public class CommandTraceRunner implements CommandInterceptor {

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandTraceRunner.class);
    /**
     * The configuration.
     */

    private final TraceConfiguration traceConfiguration;
    /**
     * The command trace.
     */
    private CommandTrace commandTrace = new CommandTrace();
    /**
     * The object mapper.
     */
    private ObjectMapper objectMapper;

    /**
     * Instantiates a new command trace serialization service.
     *
     * @param traceConfiguration the cqrs configuration
     */
    @Autowired
    public CommandTraceRunner(final TraceConfiguration traceConfiguration) {
        this.traceConfiguration = traceConfiguration;
        init();

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.byoskill.spring.cqrs.workflow.CommandInterceptor#execute(com.byoskill.
     * spring.cqrs.interceptors.api.CommandExecutionContext,
     * com.byoskill.spring.cqrs.workflow.CommandRunnerChain)
     */
    @Override
    public Object execute(final CommandExecutionContext context, final CommandRunnerChain chain)
            throws RuntimeException {
        Object result = null;
        try {
            result = chain.execute(context);

            if (traceConfiguration.isTracingEnabled()) {
                serializeTrace(TraceCommandExecution.success(context.getRawCommand(), result));
            }
        } catch (final Exception t) {
            if (traceConfiguration.isTracingEnabled()) {
                serializeTrace(TraceCommandExecution.failure(context.getRawCommand(), t));
            }
            throw t;
        }
        return result;
    }

    /**
     * Flush file.
     *
     * @throws JsonGenerationException the json generation exception
     * @throws JsonMappingException    the json mapping exception
     * @throws IOException             Signals that an I/O exception has occurred.
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
     * Inits the service.
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
     * Serialize as the trace the command being executed.
     *
     * @param _command the command being executed
     */
    private synchronized void serializeTrace(final TraceCommandExecution _command) {
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
