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
package com.byoskill.spring.cqrs.executors.profiling;

import com.byoskill.spring.cqrs.api.LoggingConfiguration;
import com.byoskill.spring.cqrs.executors.api.CommandExecutionContext;
import com.byoskill.spring.cqrs.executors.api.CommandRunner;
import com.byoskill.spring.cqrs.executors.api.CommandRunnerChain;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class CommandProfilingService is handling command execution profiling.
 */
public class CommandProfilingRunner implements CommandRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandProfilingRunner.class);
    private final LoggingConfiguration configuration;

    @Autowired
    public CommandProfilingRunner(final LoggingConfiguration configuration) {
        super();
        this.configuration = configuration;
    }

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
        if (!configuration.isProfilingEnabled()) {
            return chain.execute(context);
        }

        Object res = null;
        final StopWatch stopWatch = new StopWatch();
        try {

            stopWatch.start();
            LOGGER.debug("[PROFILING][{}] started", context.getRawCommand());

            res = chain.execute(context);
        } finally {
            stopWatch.stop();
            LOGGER.info("[PROFILING][{}]={} ms", context.getRawCommand(), stopWatch.getTime());

        }
        return res;
    }

}
