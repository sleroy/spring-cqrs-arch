/**
 * Copyright (C) 2017 Sylvain Leroy - BYOS Company All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the MIT license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the MIT license with
 * this file. If not, please write to: contact@sylvainleroy.com, or visit : https://sylvainleroy.com
 */
package com.byoskill.spring.cqrs.gate.impl;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.byoskill.spring.cqrs.gate.api.Gate;
import com.byoskill.spring.cqrs.gate.api.IEventBusService;

/**
 * This class defines the gate where the commands are dispatched for execution.
 *
 * @author sleroy
 *
 */
@Service
public class SpringGate implements Gate {

    private static final Logger		     LOGGER = LoggerFactory.getLogger(SpringGate.class);

    @Autowired
    private CommandExecutorService commandExecutorService;
    @Autowired
    private IEventBusService		     eventBusService;

    /**
     * Executes sequentially.
     */
    @Override
    public <R> R dispatch(final Object _command) {
	return commandExecutorService.run(_command);

    }

    /* (non-Javadoc)
     * @see com.byoskill.spring.cqrs.gate.api.Gate#dispatchAsync(java.lang.Object)
     */
    @Override
    public <R> CompletableFuture<R> dispatchAsync(final Object command) {
	return CompletableFuture.supplyAsync(() -> commandExecutorService.run(command));

    }

    /* (non-Javadoc)
     * @see com.byoskill.spring.cqrs.gate.api.Gate#dispatchEvent(java.lang.Object)
     */
    @Override
    public void dispatchEvent(final Object _event) {
	LOGGER.trace("Received event {}", _event);
	eventBusService.publishEvent(_event);
    }

}
