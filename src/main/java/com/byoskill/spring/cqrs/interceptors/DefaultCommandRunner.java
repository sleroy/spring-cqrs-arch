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

import com.byoskill.spring.cqrs.commands.CommandServiceSpec;
import com.byoskill.spring.cqrs.workflow.CommandExecutionContext;
import com.byoskill.spring.cqrs.workflow.CommandInterceptor;
import com.byoskill.spring.cqrs.workflow.CommandRunnerChain;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DefaultCommandRunner describes the default behaviour to execute a
 * command.
 */
public class DefaultCommandRunner implements CommandInterceptor {

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCommandRunner.class);

    /**
     * The command service handler.
     */
    private final CommandServiceSpec commandServiceHandler;

    /**
     * Instantiates a new default command runner.
     *
     * @param commandServiceHandler the command service handler
     */
    public DefaultCommandRunner(final CommandServiceSpec<?, ?> commandServiceHandler) {
        super();
        this.commandServiceHandler = commandServiceHandler;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.byoskill.spring.cqrs.workflow.CommandInterceptor#execute(com.byoskill.
     * spring.cqrs.interceptors.api.CommandExecutionContext,
     * com.byoskill.spring.cqrs.workflow.CommandRunnerChain)
     */
    @Override
    public Object execute(final CommandExecutionContext context, final CommandRunnerChain chain)
            throws RuntimeException {
        Validate.isTrue(chain == null);
        return commandServiceHandler.handle(context.getRawCommand());
    }

}
