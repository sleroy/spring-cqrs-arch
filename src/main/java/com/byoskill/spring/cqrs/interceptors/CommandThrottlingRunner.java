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

import com.byoskill.spring.cqrs.annotations.Throttle;
import com.byoskill.spring.cqrs.configuration.DefaultThrottlingInterface;
import com.byoskill.spring.cqrs.throttling.ThrottlingInterface;
import com.byoskill.spring.cqrs.workflow.CommandExecutionContext;
import com.byoskill.spring.cqrs.workflow.CommandInterceptor;
import com.byoskill.spring.cqrs.workflow.CommandRunnerChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class CommandThrottlingRunner implements CommandInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandThrottlingRunner.class);
    private final ThrottlingInterface throttlingInterface;

    /**
     * Instantiates a new Command throttling runner.
     *
     * @param throttlingInterface the throttling interface
     */
    @Autowired
    public CommandThrottlingRunner(final Optional<ThrottlingInterface> throttlingInterface) {
        this.throttlingInterface = throttlingInterface.orElse(new DefaultThrottlingInterface());
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
        Object result = null;
        // Decorate with throttling
        final Throttle throttle = context.getAnnotation(Throttle.class);
        if (throttle != null) {
            LOGGER.debug("Requiring permit from rate limiter named {}", throttle.value());
            throttlingInterface.acquirePermit(throttle.value());
        }
        result = chain.execute(context);

        return result;
    }
}
