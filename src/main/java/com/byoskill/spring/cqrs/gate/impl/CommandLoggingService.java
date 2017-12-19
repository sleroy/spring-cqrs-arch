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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.byoskill.spring.cqrs.api.ICommandExecutionListener;
import com.byoskill.spring.cqrs.gate.conf.CqrsConfiguration;

@Service
public class CommandLoggingService implements ICommandExecutionListener {

    private static final Logger	LOGGER = LoggerFactory.getLogger(CommandLoggingService.class);

    @Autowired
    private CqrsConfiguration	configuration;

    /* (non-Javadoc)
     * @see com.byoskill.spring.cqrs.api.ICommandExecutionListener#onFailure(java.lang.Object, java.lang.Throwable)
     */
    @Override
    public void onFailure(final Object _command, final Throwable _cause) {
	if (configuration.isLoggingEnabled()) {
	    LOGGER.error("Command has failed {} for the reason {}", _command, _cause);
	}

    }

    /* (non-Javadoc)
     * @see com.byoskill.spring.cqrs.api.ICommandExecutionListener#onSuccess(java.lang.Object, java.lang.Object)
     */
    @Override
    public void onSuccess(final Object _command, final Object _result) {
	if (configuration.isLoggingEnabled()) {
	    LOGGER.info("Command has been executed with success {} with the result {}", _command, _result);
	}

    }

}
