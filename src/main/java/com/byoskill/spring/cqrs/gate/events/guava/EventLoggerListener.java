/**
 * Copyright (C) 2017 Sylvain Leroy - BYOS Company All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the MIT license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the MIT license with
 * this file. If not, please write to: contact@sylvainleroy.com, or visit : https://sylvainleroy.com
 */
package com.byoskill.spring.cqrs.gate.events.guava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.byoskill.spring.cqrs.annotations.EventHandler;
import com.byoskill.spring.cqrs.gate.conf.CqrsConfiguration;
import com.google.common.eventbus.Subscribe;

@Service
@EventHandler
@Profile("guava_bus")
public class EventLoggerListener {

    private static final Logger	    LOGGER = LoggerFactory.getLogger(EventLoggerListener.class);
    private final CqrsConfiguration configuration;

    /**
     * Instantiates a new event logger listener.
     *
     * @param configuration
     *            the configuration
     */
    @Autowired
    public EventLoggerListener(final CqrsConfiguration configuration) {
	super();
	this.configuration = configuration;
    }

    /**
     * Triggers when a messsage is received.
     *
     * @param _eventMessage
     *            the event message
     */
    @Subscribe
    public void suscribe(final Object _eventMessage) {
	if (configuration.isLoggingEnabled()) {
	    LOGGER.info("Event sent : {} -> {}", _eventMessage.getClass(), _eventMessage);
	} else {
	    LOGGER.trace("Event sent : {} -> {}", _eventMessage.getClass(), _eventMessage);
	}
    }

}
