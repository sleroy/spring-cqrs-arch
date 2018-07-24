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
package com.byoskill.spring.cqrs.api;

/**
 * The Interface CommandExecutionContext.
 */
public interface CommandExecutionContext {

    /**
     * Gets the command.
     *
     * @param <T>
     *            the generic type
     * @param impl
     *            the implementation class
     * @return the command
     */
    <T> T getCommand(Class<T> impl);

    /**
     * Gets the command handler.
     *
     * @return the command handler
     */
    Object getCommandHandler();

    /**
     * Gets the raw command.
     *
     * @return the raw command
     */
    Object getRawCommand();

    /**
     * Gets the runner state.
     *
     * @return the runner state
     */
    RunnerState getRunnerState();

}
