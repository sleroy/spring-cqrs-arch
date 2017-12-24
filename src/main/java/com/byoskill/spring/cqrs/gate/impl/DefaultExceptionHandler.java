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

import com.byoskill.spring.cqrs.gate.api.CommandExecutionException;
import com.byoskill.spring.cqrs.gate.api.ICommandExceptionContext;
import com.byoskill.spring.cqrs.gate.api.ICommandExceptionHandler;

/**
 * The Class DefaultExceptionHandler.
 */
public class DefaultExceptionHandler implements ICommandExceptionHandler {

    @Override
    public void handleException(final ICommandExceptionContext context)throws RuntimeException {
	throw new CommandExecutionException(context.getCommand(), context.getException());

    }

}
