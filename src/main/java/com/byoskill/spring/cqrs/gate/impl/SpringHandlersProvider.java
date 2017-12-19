/*
 * BYOS All Right reserved
 * 2016-2017
 */
package com.byoskill.spring.cqrs.gate.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import com.byoskill.spring.cqrs.annotations.CommandHandler;
import com.byoskill.spring.cqrs.api.HandlersProvider;
import com.byoskill.spring.cqrs.api.ICommandHandler;

/**
 * This class retrieves the appropriate {@link CommandHandler} based on the type
 * of the command.
 *
 * @author sleroy
 *
 */
@Component
public class SpringHandlersProvider implements HandlersProvider {

    @Autowired
    private ConfigurableListableBeanFactory beanFactory;

    private final Map<Class<?>, String> handlers = new HashMap<>();

    private ParameterizedType findByRawType(final Type[] genericInterfaces, final Class<?> expectedRawType) {
	for (final Type type : genericInterfaces) {
	    if (type instanceof ParameterizedType) {
		final ParameterizedType parametrized = (ParameterizedType) type;
		if (expectedRawType.equals(parametrized.getRawType())) {
		    return parametrized;
		}
	    }
	}
	throw new RuntimeException();
    }

    private Class<?> getHandledCommandType(final Class<?> clazz) {
	final Type[] genericInterfaces = clazz.getGenericInterfaces();
	final ParameterizedType type = findByRawType(genericInterfaces, ICommandHandler.class);
	return (Class<?>) type.getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public ICommandHandler<Object, Object> getHandler(final Object command) {
	final String beanName = handlers.get(command.getClass());
	if (beanName == null) {
	    throw new RuntimeException("command handler not found. Command class is " + command.getClass());
	}
	final ICommandHandler<Object, Object> handler = beanFactory.getBean(beanName, ICommandHandler.class);
	return handler;
    }

    @PostConstruct
    public void onApplicationEvent() {
	handlers.clear();
	final String[] commandHandlersNames = beanFactory.getBeanNamesForType(ICommandHandler.class);
	for (final String beanName : commandHandlersNames) {
	    final BeanDefinition commandHandler = beanFactory.getBeanDefinition(beanName);
	    try {
		final Class<?> handlerClass = Class.forName(commandHandler.getBeanClassName());
		handlers.put(getHandledCommandType(handlerClass), beanName);
	    } catch (final ClassNotFoundException e) {
		throw new RuntimeException(e);
	    }
	}
    }
}
