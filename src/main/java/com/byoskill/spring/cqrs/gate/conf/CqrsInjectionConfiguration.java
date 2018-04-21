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

import java.util.Optional;
import java.util.concurrent.ForkJoinPool;

import javax.validation.Validator;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.byoskill.spring.cqrs.api.CommandExecutionListener;
import com.byoskill.spring.cqrs.api.CommandProfilingService;
import com.byoskill.spring.cqrs.api.CommandServiceProvider;
import com.byoskill.spring.cqrs.api.LoggingConfiguration;
import com.byoskill.spring.cqrs.api.ThrottlingInterface;
import com.byoskill.spring.cqrs.api.TraceConfiguration;
import com.byoskill.spring.cqrs.gate.api.CommandExceptionHandler;
import com.byoskill.spring.cqrs.gate.api.EventBusService;
import com.byoskill.spring.cqrs.gate.impl.CommandExecutorServiceImpl;
import com.byoskill.spring.cqrs.gate.impl.CommandLoggingServiceImpl;
import com.byoskill.spring.cqrs.gate.impl.CommandProfilingServiceImpl;
import com.byoskill.spring.cqrs.gate.impl.CommandTraceSerializationServiceImpl;
import com.byoskill.spring.cqrs.gate.impl.SpringGate;
import com.byoskill.spring.cqrs.gate.impl.SpringHandlersProvider;
import com.byoskill.spring.cqrs.utils.validation.ObjectValidation;

@Configuration
public class CqrsInjectionConfiguration {

    @Bean
    public CommandExecutorServiceImpl commandExecutorServiceImpl(final LoggingConfiguration configuration,
	    final CommandServiceProvider handlersProvider, final CommandExecutionListener[] listeners,
	    final CommandProfilingService profilingService,
	    final Optional<CommandExceptionHandler> commandExceptionHandler,
	    final ObjectValidation objectValidation, final ThrottlingInterface throttlingInterface,
	    final ForkJoinPool threadPoolTaskExecutor) {
	return new CommandExecutorServiceImpl(configuration, handlersProvider, listeners, profilingService,
		commandExceptionHandler, objectValidation, throttlingInterface, threadPoolTaskExecutor);
    }

    @Bean
    public CommandLoggingServiceImpl commandLoggingServiceImpl(final LoggingConfiguration configuration) {
	return new CommandLoggingServiceImpl(configuration);
    }

    @Bean
    public CommandProfilingService commandProfilingService(final LoggingConfiguration configuration) {
	return new CommandProfilingServiceImpl();
    }

    @Bean
    public CommandTraceSerializationServiceImpl commandTrace(final TraceConfiguration traceConfiguration) {
	return new CommandTraceSerializationServiceImpl(traceConfiguration);
    }

    @Bean
    public CommandServiceProvider getHandlersProvider(final ConfigurableListableBeanFactory beanFactory) {

	return new SpringHandlersProvider(beanFactory);
    }

    @Bean
    public ObjectValidation objectValidation(final Validator _validator) {
	return new ObjectValidation(_validator);
    }

    @Bean
    public SpringGate springGate(final CommandExecutorServiceImpl commandExecutorServiceImpl,
	    final EventBusService eventBusService) {
	return new SpringGate(commandExecutorServiceImpl, eventBusService);
    }
}
