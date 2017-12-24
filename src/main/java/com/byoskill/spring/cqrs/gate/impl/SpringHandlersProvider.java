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

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import com.byoskill.spring.cqrs.annotations.CommandHandler;
import com.byoskill.spring.cqrs.api.HandlersProvider;
import com.byoskill.spring.cqrs.api.ICommandHandler;
import com.byoskill.spring.cqrs.gate.api.CommandHandlerNotFoundException;
import com.byoskill.spring.cqrs.gate.api.CqrsException;

/**
 * This class retrieves the appropriate {@link CommandHandler} based on the type
 * of the command.
 *
 * @author sleroy
 *
 */
@Component
public class SpringHandlersProvider implements HandlersProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringHandlersProvider.class);

    @Autowired
    private ConfigurableListableBeanFactory beanFactory;

    private final Map<Class<?>, String> handlers = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public ICommandHandler<Object, Object> getHandler(final Object command) {
	final String beanName = handlers.get(command.getClass());
	if (beanName == null) {
	    throw new CommandHandlerNotFoundException("command handler not found. Command class is " + command.getClass());
	}
	return beanFactory.getBean(beanName, ICommandHandler.class);
    }

    @PostConstruct
    public void onApplicationEvent() {
	handlers.clear();
	final String[] commandHandlersNames = beanFactory.getBeanNamesForType(ICommandHandler.class);
	for (final String beanName : commandHandlersNames) {
	    LOGGER.info("Loading the CommandHandler definition {}", beanName);
	    final ICommandHandler bean = beanFactory.getBean(beanName, ICommandHandler.class);
	    final Class<?> commandType = getHandledCommandType(bean);
	    LOGGER.info("CommandHandler is handling command of type {}", commandType);
	    handlers.put(commandType, beanName);

	}
    }

    private ParameterizedType findByRawType(final Type[] genericInterfaces, final Class<?> expectedRawType) {
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

    private Class<?> getHandledCommandType(final Object bean) {
	Validate.notNull(bean);
	final Type[] genericInterfaces = bean.getClass().getGenericInterfaces();
	final ParameterizedType type = findByRawType(genericInterfaces, ICommandHandler.class);
	return (Class<?>) type.getActualTypeArguments()[0];
    }
}
