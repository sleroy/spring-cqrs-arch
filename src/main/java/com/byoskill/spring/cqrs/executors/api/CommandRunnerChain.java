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
package com.byoskill.spring.cqrs.executors.api;

/**
 * The Interface CommandRunnerChain defines the component that propagates the
 * execution to the next step of the command workflow.
 */
public interface CommandRunnerChain {
    /**
     * Execute the command.
     *
     * @param context the context
     * @return the returned value.
     * @throws RuntimeException the exception
     */
    Object execute(CommandExecutionContext context) throws RuntimeException;
}
