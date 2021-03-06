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

import com.byoskill.spring.cqrs.workflow.CommandExecutionContext;
import com.byoskill.spring.cqrs.workflow.CommandInterceptor;
import com.byoskill.spring.cqrs.workflow.CommandRunnerChain;
import com.byoskill.spring.cqrs.workflow.CommandExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DefaultExceptionHandlerRunner defines the last command runner.
 */
public class DefaultExceptionHandlerRunner implements CommandInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandlerRunner.class);

    public DefaultExceptionHandlerRunner() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.byoskill.spring.cqrs.interceptors.impl.CommandInterceptor#execute(com.
     * byoskill.spring.cqrs.commandgateway.interceptors.impl.CommandExecutionContext,
     * com.byoskill.spring.cqrs.interceptors.impl.CommandRunnerChain)
     */
    @Override
    public Object execute(final CommandExecutionContext context, final CommandRunnerChain chain)
            throws RuntimeException {
        Object result = null;
        try {
            result = chain.execute(context);

        } catch (final Exception t) {
            LOGGER.error("Command={} returned an exception {}", context.getRawCommand(), t.getMessage(), t);
            throw new CommandExecutionException(context.getRawCommand(), t);

        }
        return result;
    }

}
