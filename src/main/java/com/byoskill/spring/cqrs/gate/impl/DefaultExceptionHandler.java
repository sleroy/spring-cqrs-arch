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
package com.byoskill.spring.cqrs.gate.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.byoskill.spring.cqrs.api.CommandExecutionContext;
import com.byoskill.spring.cqrs.gate.api.CommandExceptionHandler;
import com.byoskill.spring.cqrs.gate.api.CommandExecutionException;

/**
 * The Class DefaultExceptionHandler.
 */
public class DefaultExceptionHandler implements CommandExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @Override
    public void handleException(final CommandExecutionContext context, final Throwable exception)
	    throws RuntimeException {
	LOGGER.error("Command={} returned an exception {}", context.getRawCommand(), exception);
	throw new CommandExecutionException(context.getRawCommand(), exception);

    }

}
