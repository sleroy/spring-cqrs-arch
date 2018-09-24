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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.byoskill.spring.cqrs.executors.api.CommandRunner;
import com.byoskill.spring.cqrs.executors.api.CommandRunnerChain;
import com.byoskill.spring.cqrs.executors.impl.DefaultCommandRunner;

public class CommandRunnerWorkflow {

    private List<CommandRunner> runnerSteps = new ArrayList<>();

    public CommandRunnerWorkflow() {
        super();
    }

    /**
     * Instantiates a new command runner workflow.
     *
     * @param runnerSteps the runner steps
     */
    public CommandRunnerWorkflow(final List<CommandRunner> runnerSteps) {
        this.runnerSteps = Collections.unmodifiableList(runnerSteps);
    }

    /**
     * Adds the step.
     *
     * @param commandRunner the command runner
     * @return the command runner workflow
     */
    public CommandRunnerWorkflow addStep(final CommandRunner commandRunner) {
        final ArrayList<CommandRunner> newWorkflow = new ArrayList<>();
        newWorkflow.addAll(runnerSteps);
        newWorkflow.add(commandRunner);
        return new CommandRunnerWorkflow(newWorkflow);
    }

    /**
     * Adds the steps.
     *
     * @param runners the runners
     * @return the command runner workflow
     */
    public CommandRunnerWorkflow addSteps(final CommandRunner... runners) {
        final ArrayList<CommandRunner> newWorkflow = new ArrayList<>();
        newWorkflow.addAll(runnerSteps);
        for (final CommandRunner runner : runners) {
            newWorkflow.add(runner);
        }

        return new CommandRunnerWorkflow(newWorkflow);
    }

    private CommandRunnerChainBlock buildChain(final ArrayList<CommandRunner> arrayList, final int pos) {
        if (arrayList.size() - 1 == pos) {
            return new CommandRunnerChainBlock(arrayList.get(pos), null);
        }
        return new CommandRunnerChainBlock(arrayList.get(pos), buildChain(arrayList, pos + 1));
    }

    /**
     * Builds the chain.
     *
     * @param defaultCommandRunner the default command runner
     * @return the command runner chain
     */
    public CommandRunnerChain buildChain(final DefaultCommandRunner defaultCommandRunner) {
        final ArrayList<CommandRunner> newWorkflow = new ArrayList<>(runnerSteps.size() + 1);
        newWorkflow.addAll(runnerSteps);
        newWorkflow.add(defaultCommandRunner);
        final CommandRunnerChainBlock commandRunnerChainBlock = buildChain(newWorkflow, 0);
        return commandRunnerChainBlock;
    }

    /**
     * Gets the runner steps.
     *
     * @return the runner steps
     */
    public List<CommandRunner> getRunnerSteps() {
        return runnerSteps;
    }

    /**
     * Inserts a step after.
     *
     * @param commandRunner the command runner
     * @param afterStep     the after step
     * @return the command runner workflow
     */
    public CommandRunnerWorkflow insertAfter(final CommandRunner commandRunner, final Class<?> afterStep) {
        final ArrayList<CommandRunner> newWorkflow = new ArrayList<>();
        for (int i = 0; i < runnerSteps.size(); i++) {
            newWorkflow.add(runnerSteps.get(i));
            if (afterStep.isAssignableFrom(runnerSteps.get(i).getClass())) {
                newWorkflow.add(commandRunner);
            }
        }

        return new CommandRunnerWorkflow(newWorkflow);
    }

    /**
     * Inserts a step before.
     *
     * @param commandRunner the command runner
     * @param beforeStep    the before step
     * @return the command runner workflow
     */
    public CommandRunnerWorkflow insertBefore(final CommandRunner commandRunner, final Class<?> beforeStep) {
        final ArrayList<CommandRunner> newWorkflow = new ArrayList<>();
        for (int i = 0; i < runnerSteps.size(); i++) {
            if (beforeStep.isAssignableFrom(runnerSteps.get(i).getClass())) {
                newWorkflow.add(commandRunner);
            }
            newWorkflow.add(runnerSteps.get(i));
        }

        return new CommandRunnerWorkflow(newWorkflow);
    }

}
