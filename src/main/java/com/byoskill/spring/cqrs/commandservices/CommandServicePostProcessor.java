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

import com.byoskill.spring.cqrs.annotations.CommandHandler;
import com.byoskill.spring.cqrs.commandgateway.CqrsException;
import com.byoskill.spring.cqrs.commands.CommandServiceSpec;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.springframework.util.ClassUtils.getUserClass;

public class CommandServicePostProcessor implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandServicePostProcessor.class);
    private final CommandServiceProvider commandServiceProvider;

    /**
     * Instantiates a new command service post processor.
     *
     * @param commandServiceProvider the command service provider
     */
    @Autowired
    public CommandServicePostProcessor(final CommandServiceProvider commandServiceProvider) {
        this.commandServiceProvider = commandServiceProvider;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.beans.factory.config.BeanPostProcessor#
     * postProcessBeforeInitialization(java.lang.Object, java.lang.String)
     */
    @Override
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {

        return bean;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.beans.factory.config.BeanPostProcessor#
     * postProcessAfterInitialization(java.lang.Object, java.lang.String)
     */
    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        if (bean instanceof CommandServiceSpec) {
            LOGGER.info("Loading an command handler {}->{}", beanName, bean);
            final Class<?> commandServiceType = getCommandServiceType(bean);
            commandServiceProvider.subscribe(commandServiceType.getName(), (CommandServiceSpec<?, ?>) bean);
        } else {
            searchForCommandHandlerMethods(bean);
        }
        return bean;
    }

    private void searchForCommandHandlerMethods(final Object bean) {
        final Class<?> userClass = getUserClass(bean.getClass());
        final Method[] methods = userClass.getMethods();
        for (Method method : methods) {
            if (hasValidDefinition(method)) {
                final CommandHandler commandHandler = AnnotationUtils.findAnnotation(method, CommandHandler.class);
                if (commandHandler != null) {
                    LOGGER.info("Found command handler {} in the bean {}", method, userClass);
                    if (method.getParameterCount() == 0) {
                        final NoArgMethodCommandServiceSpec argMethodCommandServiceSpec = new NoArgMethodCommandServiceSpec(bean, method);
                        this.commandServiceProvider.subscribe(userClass.getSimpleName() + "." + method.getName(), argMethodCommandServiceSpec);
                    } else {
                        final Class<?> commandParameterType = getCommandParameterType(method);
                        final String commandParameterTypeName = commandParameterType.getName();
                        final MethodCommandServiceSpec methodCommandServiceSpec = new MethodCommandServiceSpec(bean, method, userClass, commandParameterType);
                        this.commandServiceProvider.subscribe(commandParameterTypeName, methodCommandServiceSpec);
                    }
                }
            }
        }
    }

    private Class<?> getCommandParameterType(final Method method) {
        return method.getParameterTypes()[0];
    }

    private boolean hasValidDefinition(final Method method) {
        return method.getParameterCount() <= 1 && Modifier.isPublic(method.getModifiers());
    }

    /**
     * Gets the handled command type.
     *
     * @param bean the bean
     *
     * @return the handled command type
     */
    private Class<?> getCommandServiceType(final Object bean) {
        Validate.notNull(bean);
        Class<?> userClass = getUserClass(bean);
        final Type[] genericInterfaces = userClass.getGenericInterfaces();
        final ParameterizedType type = findByRawType(genericInterfaces, CommandServiceSpec.class);
        return (Class<?>) type.getActualTypeArguments()[0];
    }

    private static ParameterizedType findByRawType(final Type[] genericInterfaces, final Class<?> expectedRawType) {
        for (final Type type : genericInterfaces) {
            if (type instanceof ParameterizedType) {
                final ParameterizedType parametrized = (ParameterizedType) type;
                if (expectedRawType.equals(parametrized.getRawType())) {
                    return parametrized;
                }
            }
        }
        throw new CqrsException("Invalid command handler definition, cannot retrieve the type of command handled");
    }
}
