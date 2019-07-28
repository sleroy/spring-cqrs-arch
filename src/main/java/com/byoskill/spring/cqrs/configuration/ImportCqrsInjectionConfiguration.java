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
package com.byoskill.spring.cqrs.configuration;

import com.byoskill.spring.cqrs.commandgateway.CommandGateway;
import com.byoskill.spring.cqrs.commandgateway.SimpleCommandGateway;
import com.byoskill.spring.cqrs.commandgateway.SpringGate;
import com.byoskill.spring.cqrs.commandservices.CommandServiceProvider;
import com.byoskill.spring.cqrs.commandservices.SpringCommandServiceProvider;
import com.byoskill.spring.cqrs.events.EventBusService;
import com.byoskill.spring.cqrs.filter.SpringGateFilters;
import com.byoskill.spring.cqrs.interceptors.*;
import com.byoskill.spring.cqrs.throttling.ThrottlingInterface;
import com.byoskill.spring.cqrs.utils.validation.ObjectValidation;
import com.byoskill.spring.cqrs.workflow.CommandExecutorServiceImpl;
import com.byoskill.spring.cqrs.workflow.CommandRunnerWorkflowService;
import com.byoskill.spring.cqrs.workflow.CommandRunningWorkflowConfigurer;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.validation.Validator;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Configuration
public class ImportCqrsInjectionConfiguration {

    /**
     * Command executor service impl.
     *
     * @param handlersProvider       the handlers provider
     * @param commandWorkflowService the command workflow service
     * @param threadPoolTaskExecutor the thread pool task executor
     * @return the command executor service impl
     */
    @Bean
    @Scope(value = SCOPE_SINGLETON)
    public CommandExecutorServiceImpl commandExecutorServiceImpl(final CommandServiceProvider handlersProvider,
                                                                 final CommandRunnerWorkflowService commandWorkflowService,
                                                                 final ForkJoinPool threadPoolTaskExecutor) {
        return new CommandExecutorServiceImpl(handlersProvider, commandWorkflowService,
                threadPoolTaskExecutor);
    }

    @Bean
    @Scope(value = SCOPE_SINGLETON)
    public CommandLoggingRunner commandLoggingRunner(final LoggingConfiguration configuration) {
        return new CommandLoggingRunner(configuration);
    }

    @Bean
    @Scope(value = SCOPE_SINGLETON)
    public CommandProfilingRunner commandProfilingRunner(final LoggingConfiguration loggingConfiguration) {
        return new CommandProfilingRunner(loggingConfiguration);
    }

    @Bean
    @Scope(value = SCOPE_SINGLETON)
    public CommandThrottlingRunner commandThrottling(final ThrottlingInterface throttlingInterface) {
        return new CommandThrottlingRunner(throttlingInterface);
    }

    @Bean
    @Scope(value = SCOPE_SINGLETON)
    public CommandTraceRunner commandTrace(final TraceConfiguration traceConfiguration) {
        return new CommandTraceRunner(traceConfiguration);
    }

    @Bean
    @Scope(value = SCOPE_SINGLETON)
    public CommandValidatingRunner commandValidating(final ObjectValidation objectValidation) {
        return new CommandValidatingRunner(objectValidation);
    }

    @Bean
    @Scope(value = SCOPE_SINGLETON)
    public DefaultExceptionHandlerRunner defaultExceptionHandlerRunner() {
        return new DefaultExceptionHandlerRunner();
    }

    @Bean
    @Scope(value = SCOPE_SINGLETON)
    public EventThrowerRunner eventThrower(final EventBusService eventBusService) {
        return new EventThrowerRunner(eventBusService);
    }

    @Bean
    @Scope(value = SCOPE_SINGLETON)
    public CommandRunnerWorkflowService getCommandRunnerWorkflowService(
            final DefaultExceptionHandlerRunner defaultExceptionHandlerRunner,
            final CommandLoggingRunner commandLoggingRunner, final CommandProfilingRunner commandProfilingRunner,
            final CommandThrottlingRunner commandThrottlingRunner, final CommandTraceRunner commandTraceRunner,
            final CommandValidatingRunner commandValidatingRunner,
            final Optional<CommandRunningWorkflowConfigurer> configurer, final EventThrowerRunner eventThrowerRunner) {
        return new CommandRunnerWorkflowService(defaultExceptionHandlerRunner, commandLoggingRunner,
                commandProfilingRunner, commandThrottlingRunner, commandTraceRunner, commandValidatingRunner,
                eventThrowerRunner, configurer);
    }

    @Bean
    @Scope(value = SCOPE_SINGLETON)
    public CommandServiceProvider getHandlersProvider(final ConfigurableListableBeanFactory beanFactory) {

        return new SpringCommandServiceProvider(beanFactory);
    }

    @Scope(value = SCOPE_SINGLETON)
    @Bean
    public ObjectValidation objectValidation(final Validator _validator) {
        return new ObjectValidation(_validator);
    }

    @Bean
    @Scope(value = SCOPE_SINGLETON)
    public SpringGate springGate(final CommandExecutorServiceImpl commandExecutorServiceImpl,
                                 final EventBusService eventBusService, final Optional<SpringGateFilters> springFilter) {
        return new SpringGate(commandExecutorServiceImpl, eventBusService, springFilter);
    }


    @Bean
    @Scope(SCOPE_SINGLETON)
    public CommandGateway commandGateway(SpringGate springGate) {
        return new SimpleCommandGateway(springGate);
    }
}
