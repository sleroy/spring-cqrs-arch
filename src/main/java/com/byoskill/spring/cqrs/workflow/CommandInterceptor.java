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

/**
 * The Interface CommandInterceptor describes a component that will be used to
 * execute the command workflow. The workflow is chained as with
 * javax.servlet.Filter.
 */
public interface CommandInterceptor {

    /**
     * Execute the command.
     *
     * @param context the context
     * @param chain   the chain
     * @return the returned value.
     * @throws RuntimeException the runtime exception
     */
    Object execute(CommandExecutionContext context, CommandRunnerChain chain) throws RuntimeException;
}
