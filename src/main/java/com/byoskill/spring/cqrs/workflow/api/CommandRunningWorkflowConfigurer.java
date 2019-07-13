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
package com.byoskill.spring.cqrs.workflow.api;

import com.byoskill.spring.cqrs.executors.api.CommandRunner;
import com.byoskill.spring.cqrs.workflow.impl.CommandRunnerWorkflow;

/**
 * The Interface CommandRunningWorkflowConfigurer defines the tool to configure
 * the worflow used to execute a command. The pipeline is composed of
 * {@link CommandRunner} steps.
 */
public interface CommandRunningWorkflowConfigurer {

    /**
     * Configure workflow.
     *
     * @param defaultWorkflow the default workflow
     * @return the runner workflow with all steps
     */
    CommandRunnerWorkflow configureWorkflow(CommandRunnerWorkflow defaultWorkflow);

}
