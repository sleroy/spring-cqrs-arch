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

public class CommandRunnerChainBlock implements CommandRunnerChain {
    private final CommandInterceptor commandInterceptor;

    private final CommandRunnerChain chain;

    /**
     * Instantiates a new command runner chain block.
     *
     * @param commandInterceptor the command runner
     * @param chain         the chain
     */
    public CommandRunnerChainBlock(final CommandInterceptor commandInterceptor, final CommandRunnerChain chain) {
        this.commandInterceptor = commandInterceptor;
        this.chain = chain;
    }

    @Override
    public Object execute(final CommandExecutionContext context) throws RuntimeException {
        return commandInterceptor.execute(context, chain);
    }
}
