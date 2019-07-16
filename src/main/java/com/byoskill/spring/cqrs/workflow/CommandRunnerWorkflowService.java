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
package com.byoskill.spring.cqrs.workflow;

import com.byoskill.spring.cqrs.events.EventThrowerRunner;
import com.byoskill.spring.cqrs.interceptors.DefaultExceptionHandlerRunner;
import com.byoskill.spring.cqrs.interceptors.CommandLoggingRunner;
import com.byoskill.spring.cqrs.interceptors.CommandProfilingRunner;
import com.byoskill.spring.cqrs.interceptors.CommandThrottlingRunner;
import com.byoskill.spring.cqrs.interceptors.CommandTraceRunner;
import com.byoskill.spring.cqrs.interceptors.CommandValidatingRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Optional;

public class CommandRunnerWorkflowService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CommandRunnerWorkflowService.class);

    protected final CommandInterceptor defaultExceptionHandlerRunner;
    protected final CommandLoggingRunner commandLoggingRunner;
    protected final CommandProfilingRunner commandProfilingRunner;
    protected final CommandThrottlingRunner commandThrottlingRunner;
    protected final CommandTraceRunner commandTraceRunner;
    protected final CommandValidatingRunner commandValidatingRunner;
    private final Optional<CommandRunningWorkflowConfigurer> configurer;

    private final CommandRunnerWorkflow defaultWorkflow;
    private CommandRunnerWorkflow runnerWorkflow;

    @Autowired
    public CommandRunnerWorkflowService(
            final DefaultExceptionHandlerRunner defaultExceptionHandlerRunner,
            final CommandLoggingRunner commandLoggingRunner,
            final CommandProfilingRunner commandProfilingRunner,
            final CommandThrottlingRunner commandThrottlingRunner,
            final CommandTraceRunner commandTraceRunner,
            final CommandValidatingRunner commandValidatingRunner,
            final EventThrowerRunner eventThrowerRunner,
            final Optional<CommandRunningWorkflowConfigurer> configurer) {
        super();
        this.defaultExceptionHandlerRunner = defaultExceptionHandlerRunner;
        this.commandLoggingRunner = commandLoggingRunner;
        this.commandProfilingRunner = commandProfilingRunner;
        this.commandThrottlingRunner = commandThrottlingRunner;
        this.commandTraceRunner = commandTraceRunner;
        this.commandValidatingRunner = commandValidatingRunner;
        this.configurer = configurer;
        defaultWorkflow = new CommandRunnerWorkflow().addSteps(
                commandLoggingRunner,
                commandThrottlingRunner,
                commandValidatingRunner,
                commandProfilingRunner,
                commandTraceRunner,
                eventThrowerRunner,
                defaultExceptionHandlerRunner);

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
