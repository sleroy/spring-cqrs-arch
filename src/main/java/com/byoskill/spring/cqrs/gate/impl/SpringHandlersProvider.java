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
package com.byoskill.spring.cqrs.gate.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.byoskill.spring.cqrs.api.CommandServiceProvider;
import com.byoskill.spring.cqrs.api.CommandServiceSpec;
import com.byoskill.spring.cqrs.gate.api.CommandHandlerNotFoundException;
import com.byoskill.spring.cqrs.gate.api.CqrsException;

/**
 * This class retrieves the appropriate {@link CommandServiceSpec} based on the
 * type of the command.
 *
 * @author sleroy
 *
 */
public class SpringHandlersProvider implements CommandServiceProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringHandlersProvider.class);

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

    private final ConfigurableListableBeanFactory beanFactory;

    private final Map<Class<?>, String> handlers = new HashMap<>();

    /**
     * Instantiates a new spring handlers provider.
     *
     * @param beanFactory
     *            the bean factory
     */
    @Autowired
    public SpringHandlersProvider(final ConfigurableListableBeanFactory beanFactory) {
	super();
	this.beanFactory = beanFactory;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.byoskill.spring.cqrs.api.HandlersProvider#getHandler(java.lang.
     * Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public CommandServiceSpec<?, ?> getService(final Object command) {
	final String beanName = handlers.get(command.getClass());
	if (beanName == null) {
	    throw new CommandHandlerNotFoundException(
		    "command handler not found. Command class is " + command.getClass());
	}
	final Object bean = beanFactory.getBean(beanName);
	Validate.notNull(bean);
	if (bean instanceof CommandServiceSpec) {
	    return (CommandServiceSpec) bean;
	}
	throw new UnsupportedOperationException("Unsupported type of command service " + bean);
    }

    @Override
    public void putCommand(final Object bean, final String beanName) {
	handlers.put(getCommandServiceType(bean), beanName);
    }

    /**
     * Gets the handled command type.
     *
     * @param bean
     *            the bean
     * @return the handled command type
     */
    private Class<?> getCommandServiceType(final Object bean) {
	Validate.notNull(bean);
	final Type[] genericInterfaces = bean.getClass().getGenericInterfaces();
	final ParameterizedType type = findByRawType(genericInterfaces, CommandServiceSpec.class);
	return (Class<?>) type.getActualTypeArguments()[0];
    }

}
