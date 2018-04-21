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
package com.byoskill.spring.cqrs.gate.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.byoskill.spring.cqrs.api.EventBusConfiguration;
import com.byoskill.spring.cqrs.api.LoggingConfiguration;
import com.byoskill.spring.cqrs.gate.api.EventBusService;
import com.byoskill.spring.cqrs.gate.events.guava.EventLoggerListener;
import com.byoskill.spring.cqrs.gate.events.guava.GuavaEventBusService;

@Configuration
public class GuavaEventBusConfiguration implements EventBusConfiguration {

    @Bean
    @Override
    public EventBusService eventBus() {
	return new GuavaEventBusService(false);
    }

    @Bean
    public EventLoggerListener eventLoggerListener(final LoggingConfiguration loggingConfiguration) {
	return new EventLoggerListener(loggingConfiguration);
    }

}
