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
package com.byoskill.spring.cqrs.executors.event;

import com.byoskill.spring.cqrs.annotations.ReturnEventOnSuccess;
import com.byoskill.spring.cqrs.api.EventThrower;
import com.byoskill.spring.cqrs.executors.api.CommandExecutionContext;
import com.byoskill.spring.cqrs.executors.api.CommandRunner;
import com.byoskill.spring.cqrs.executors.api.CommandRunnerChain;
import com.byoskill.spring.cqrs.gate.api.EventBusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This process identifies CommandService that requires to send events and  performs the required actions depending of their event throwing definitions.
 */
public class EventThrowerRunner implements CommandRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventThrowerRunner.class);
    private final EventBusService eventBusService;

    @Autowired
    public EventThrowerRunner(final EventBusService eventBusService) {
        this.eventBusService = eventBusService;
    }

    @Override
    public Object execute(final CommandExecutionContext context, final CommandRunnerChain chain)
            throws RuntimeException {

        Object res = null;
        final EventThrower handler = context.handler() instanceof EventThrower ? (EventThrower) context.handler()
                : null;
        try {
            res = chain.execute(context);
            // If the handler is defining an Event factorw method in case of success
            if (handler != null) {
                eventBusService.publishEvent(handler.eventOnSuccess(res));
            } else {
                // If the command handler has specified a @ReturnEventOnSuccess annotation; the value will be returned as an event.
                final Class<?> ultimateTargetClass = AopProxyUtils.ultimateTargetClass(context.handler());
                if (ultimateTargetClass.isAnnotationPresent(ReturnEventOnSuccess.class)) {
                    LOGGER.info("Command {} is sending the event {} as result", context.getRawCommand(), res);
                    eventBusService.publishEvent(res);
                    res = null;
                }
            }

        } catch (final Exception t) {
            if (handler != null) {
                LOGGER.warn("Execution of the command {} has failed, sending an event...", context.getRawCommand());
                eventBusService.publishEvent(handler.eventOnFailure(t));
            }
            throw t;
        }
        return res;
    }
}
