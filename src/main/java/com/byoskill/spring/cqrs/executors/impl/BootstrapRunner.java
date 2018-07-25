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
package com.byoskill.spring.cqrs.executors.impl;

import com.byoskill.spring.cqrs.executors.api.CommandExecutionContext;
import com.byoskill.spring.cqrs.executors.api.CommandRunner;
import com.byoskill.spring.cqrs.executors.api.CommandRunnerChain;

public class BootstrapRunner implements CommandRunner {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.byoskill.spring.cqrs.executors.api.CommandRunner#execute(com.byoskill.
     * spring.cqrs.executors.api.CommandExecutionContext,
     * com.byoskill.spring.cqrs.executors.api.CommandRunnerChain)
     */
    @Override
    public Object execute(final CommandExecutionContext context, final CommandRunnerChain chain)
	    throws RuntimeException {
	return chain.execute(context);
    }
}
