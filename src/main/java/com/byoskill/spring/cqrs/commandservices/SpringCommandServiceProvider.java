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
package com.byoskill.spring.cqrs.commandservices;

import com.byoskill.spring.cqrs.commands.CommandServiceSpec;
import com.byoskill.spring.cqrs.utils.validation.InvalidCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.ResolvableType;
import org.springframework.core.type.MethodMetadata;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class retrieves the appropriate {@link CommandServiceSpec} based on the
 * type of the command.
 *
 * @author sleroy
 */
public class SpringCommandServiceProvider implements CommandServiceProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringCommandServiceProvider.class);
    private final ConfigurableListableBeanFactory beanFactory;
    private final Map<String, CommandServiceSpec<?, ?>> handlers = new ConcurrentHashMap<>(200);

    /**
     * Instantiates a new spring handlers provider.
     *
     * @param beanFactory the bean factory
     */
    @Autowired
    public SpringCommandServiceProvider(final ConfigurableListableBeanFactory beanFactory) {
        super();
        this.beanFactory = beanFactory;
    }


    @SuppressWarnings("unchecked")
    @Override
    public CommandServiceSpec<?, ?> getService(final Object command) {
        return findCommandServiceFromCommandObject(command);
    }

    private CommandServiceSpec<?, ?> findCommandServiceFromCommandObject(final Object command) {
        final CommandServiceSpec<?, ?> commandServiceSpec = handlers.get(command.getClass().getName());
        if (commandServiceSpec == null) {
            throw new CommandHandlerNotFoundException(
                    "command handler not found. Command class is " + command.getClass());
        }
        return commandServiceSpec;
    }

    @Override
    public void subscribe(final String commandName, final CommandServiceSpec<?, ?> commandHandler) {
        LOGGER.info("Subscribing the command {} to {}", commandName, commandHandler.getClass().getName());

            this.handlers.put(commandName, commandHandler);
    }


    @Override
    public CommandServiceSpec getServiceName(final String commandName) {
        final CommandServiceSpec<?, ?> commandServiceSpec = this.handlers.get(commandName);
        if (commandServiceSpec == null) {
            throw new CommandHandlerNotFoundException(
                    "command handler not found. Command name is " + commandName);
        }
        return commandServiceSpec;
    }

    @Override
    public Class<?> guessLambdaType(final Object bean, final String beanName) {
        final BeanDefinition beanDefinition = this.beanFactory.getBeanDefinition(beanName);
        if (beanDefinition == null) throw new CommandServiceSpecException("Could not guess the type of the command from the bean " + beanName);
        final ResolvableType resolvableType = beanDefinition.getResolvableType();
        final ResolvableType[] generics     = resolvableType.getGenerics();
        if (generics.length > 0) {
            return generics[0].getRawClass();
        } else {
            throw new CommandServiceSpecException("Could not guess the type of the command from the bean " + beanName);
        }
    }


}
