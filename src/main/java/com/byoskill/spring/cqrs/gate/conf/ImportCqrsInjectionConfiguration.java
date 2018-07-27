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
import org.springframework.context.annotation.Scope;

import com.byoskill.spring.cqrs.api.CommandServiceProvider;
import com.byoskill.spring.cqrs.api.LoggingConfiguration;
import com.byoskill.spring.cqrs.api.ThrottlingInterface;
import com.byoskill.spring.cqrs.api.TraceConfiguration;
import com.byoskill.spring.cqrs.executors.event.EventThrowerRunner;
import com.byoskill.spring.cqrs.executors.exception.DefaultExceptionHandlerRunner;
import com.byoskill.spring.cqrs.executors.logging.CommandLoggingRunner;
import com.byoskill.spring.cqrs.executors.profiling.CommandProfilingRunner;
import com.byoskill.spring.cqrs.executors.throttling.CommandThrottlingRunner;
import com.byoskill.spring.cqrs.executors.tracing.CommandTraceRunner;
import com.byoskill.spring.cqrs.executors.validating.CommandValidatingRunner;
import com.byoskill.spring.cqrs.gate.api.EventBusService;
import com.byoskill.spring.cqrs.gate.impl.CommandExecutorServiceImpl;
import com.byoskill.spring.cqrs.gate.impl.SpringGate;
import com.byoskill.spring.cqrs.gate.impl.SpringHandlersProvider;
import com.byoskill.spring.cqrs.utils.validation.ObjectValidation;
import com.byoskill.spring.cqrs.workflow.api.CommandRunningWorkflowConfigurer;
import com.byoskill.spring.cqrs.workflow.impl.CommandRunnerWorkflowService;

@Configuration
public class ImportCqrsInjectionConfiguration {

    /**
     * Command executor service impl.
     *
     * @param configuration
     *            the configuration
     * @param handlersProvider
     *            the handlers provider
     * @param objectValidation
     *            the object validation
     * @param commandWorkflowService
     *            the command workflow service
     * @param threadPoolTaskExecutor
     *            the thread pool task executor
     * @return the command executor service impl
     */
    @Bean
    @Scope(value = ConfigurableListableBeanFactory.SCOPE_SINGLETON)
    public CommandExecutorServiceImpl commandExecutorServiceImpl(final LoggingConfiguration configuration,
	    final CommandServiceProvider handlersProvider, final ObjectValidation objectValidation,
	    final CommandRunnerWorkflowService commandWorkflowService, final ForkJoinPool threadPoolTaskExecutor) {
	return new CommandExecutorServiceImpl(configuration, handlersProvider, objectValidation, commandWorkflowService,
		threadPoolTaskExecutor);
    }

    @Bean
    @Scope(value = ConfigurableListableBeanFactory.SCOPE_SINGLETON)
    public CommandLoggingRunner commandLoggingRunner(final LoggingConfiguration configuration) {
	return new CommandLoggingRunner(configuration);
    }

    @Bean
    @Scope(value = ConfigurableListableBeanFactory.SCOPE_SINGLETON)
    public CommandProfilingRunner commandProfilingRunner(final LoggingConfiguration loggingConfiguration) {
	return new CommandProfilingRunner(loggingConfiguration);
    }

    @Bean
    @Scope(value = ConfigurableListableBeanFactory.SCOPE_SINGLETON)
    public CommandThrottlingRunner commandThrottling(final ThrottlingInterface throttlingInterface) {
	return new CommandThrottlingRunner(throttlingInterface);
    }

    @Bean
    @Scope(value = ConfigurableListableBeanFactory.SCOPE_SINGLETON)
    public CommandTraceRunner commandTrace(final TraceConfiguration traceConfiguration) {
	return new CommandTraceRunner(traceConfiguration);
    }

    @Bean
    @Scope(value = ConfigurableListableBeanFactory.SCOPE_SINGLETON)
    public CommandValidatingRunner commandValidating(final ObjectValidation objectValidation) {
	return new CommandValidatingRunner(objectValidation);
    }

    @Bean
    @Scope(value = ConfigurableListableBeanFactory.SCOPE_SINGLETON)
    public DefaultExceptionHandlerRunner defaultExceptionHandlerRunner() {
	return new DefaultExceptionHandlerRunner();
    }

    @Bean
    @Scope(value = ConfigurableListableBeanFactory.SCOPE_SINGLETON)
    public EventThrowerRunner eventThrower(final EventBusService eventBusService) {
	return new EventThrowerRunner(eventBusService);
    }

    @Bean
    @Scope(value = ConfigurableListableBeanFactory.SCOPE_SINGLETON)
    public CommandRunnerWorkflowService getCommandRunnerWorkflowService(
	    final DefaultExceptionHandlerRunner defaultExceptionHandlerRunner,
	    final CommandLoggingRunner commandLoggingRunner,
	    final CommandProfilingRunner commandProfilingRunner,
	    final CommandThrottlingRunner commandThrottlingRunner,
	    final CommandTraceRunner commandTraceRunner,
	    final CommandValidatingRunner commandValidatingRunner,
	    final Optional<CommandRunningWorkflowConfigurer> configurer,
	    final EventThrowerRunner eventThrowerRunner) {
	return new CommandRunnerWorkflowService(defaultExceptionHandlerRunner, commandLoggingRunner,
		commandProfilingRunner, commandThrottlingRunner, commandTraceRunner, commandValidatingRunner,
		eventThrowerRunner, configurer);
    }

    @Bean
    @Scope(value = ConfigurableListableBeanFactory.SCOPE_SINGLETON)
    public CommandServiceProvider getHandlersProvider(final ConfigurableListableBeanFactory beanFactory) {

	return new SpringHandlersProvider(beanFactory);
    }

    @Scope(value = ConfigurableListableBeanFactory.SCOPE_SINGLETON)
    @Bean
    public ObjectValidation objectValidation(final Validator _validator) {
	return new ObjectValidation(_validator);
    }

    @Bean
    @Scope(value = ConfigurableListableBeanFactory.SCOPE_SINGLETON)
    public SpringGate springGate(final CommandExecutorServiceImpl commandExecutorServiceImpl,
	    final EventBusService eventBusService) {
	return new SpringGate(commandExecutorServiceImpl, eventBusService);
    }
}
