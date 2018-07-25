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
package com.byoskill.spring.cqrs.workflow.impl;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.byoskill.spring.cqrs.executors.api.CommandRunner;
import com.byoskill.spring.cqrs.executors.exception.DefaultExceptionHandlerRunner;
import com.byoskill.spring.cqrs.executors.logging.CommandLoggingRunner;
import com.byoskill.spring.cqrs.executors.profiling.CommandProfilingRunner;
import com.byoskill.spring.cqrs.executors.throttling.CommandThrottlingRunner;
import com.byoskill.spring.cqrs.executors.tracing.CommandTraceRunner;
import com.byoskill.spring.cqrs.executors.validating.CommandValidatingRunner;
import com.byoskill.spring.cqrs.workflow.api.CommandRunningWorkflowConfigurer;

public class CommandRunnerWorkflowService {

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(CommandRunnerWorkflowService.class);

    protected final CommandRunner			     defaultExceptionHandlerRunner;
    protected final CommandLoggingRunner		     commandLoggingRunner;
    protected final CommandProfilingRunner		     commandProfilingRunner;
    protected final CommandThrottlingRunner		     commandThrottlingRunner;
    protected final CommandTraceRunner			     commandTraceRunner;
    protected final CommandValidatingRunner		     commandValidatingRunner;
    private final Optional<CommandRunningWorkflowConfigurer> configurer;

    private final CommandRunnerWorkflow	defaultWorkflow;
    private CommandRunnerWorkflow	runnerWorkflow;

    /**
     * Instantiates a new command running workflow.
     *
     * @param defaultExceptionHandlerRunner
     *            the default exception handler runner
     * @param commandLoggingRunner
     *            the command logging runner
     * @param commandProfilingRunner
     *            the command profiling runner
     * @param commandThrottlingRunner
     *            the command throttling runner
     * @param commandTraceRunner
     *            the command trace runner
     * @param commandValidatingRunner
     *            the command validating runner
     * @param configurer
     *            the configurer
     */
    @Autowired
    public CommandRunnerWorkflowService(final DefaultExceptionHandlerRunner defaultExceptionHandlerRunner,
	    final CommandLoggingRunner commandLoggingRunner, final CommandProfilingRunner commandProfilingRunner,
	    final CommandThrottlingRunner commandThrottlingRunner, final CommandTraceRunner commandTraceRunner,
	    final CommandValidatingRunner commandValidatingRunner,
	    final Optional<CommandRunningWorkflowConfigurer> configurer) {
	super();
	this.defaultExceptionHandlerRunner = defaultExceptionHandlerRunner;
	this.commandLoggingRunner = commandLoggingRunner;
	this.commandProfilingRunner = commandProfilingRunner;
	this.commandThrottlingRunner = commandThrottlingRunner;
	this.commandTraceRunner = commandTraceRunner;
	this.commandValidatingRunner = commandValidatingRunner;
	this.configurer = configurer;
	defaultWorkflow = new CommandRunnerWorkflow().addSteps(commandLoggingRunner, commandThrottlingRunner,
		commandValidatingRunner,
		commandProfilingRunner, commandTraceRunner, defaultExceptionHandlerRunner);

    }

    /**
     * Gets the runner workflow.
     *
     * @return the runner workflow
     */
    public CommandRunnerWorkflow getRunnerWorkflow() {
	return runnerWorkflow;
    }

    /**
     * Gets the workflow.
     *
     */
    @PostConstruct
    public void initializeWorkflow() {

	if (configurer.isPresent()) {
	    LOGGER.info("Initializing a custom running workflow");
	    runnerWorkflow = configurer.get().configureWorkflow(defaultWorkflow);
	} else {
	    LOGGER.info("Initializing a default workflow");
	    runnerWorkflow = defaultWorkflow;
	}
    }

}
