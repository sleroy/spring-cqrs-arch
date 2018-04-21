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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.byoskill.spring.cqrs.gate.api.CommandExecutionException;
import com.byoskill.spring.cqrs.gate.api.CommandExceptionContext;
import com.byoskill.spring.cqrs.gate.api.CommandExceptionHandler;

/**
 * The Class DefaultExceptionHandler.
 */
public class DefaultExceptionHandler implements CommandExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @Override
    public void handleException(final CommandExceptionContext context) throws RuntimeException {

	LOGGER.error("Command={} returned an exception {}", context.getCommand(), context.getException());
	throw new CommandExecutionException(context.getCommand(), context.getException());

    }

}
