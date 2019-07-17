package com.byoskill.spring.cqrs.commandservices;

import com.byoskill.spring.cqrs.commands.CommandServiceSpec;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodCommandServiceSpec implements CommandServiceSpec<Object, Object> {
    private final Object bean;
    private final Method method;
    private final Class<?> userClass;
    private final Class<?> commandParameterType;

    /**
     * Instantiates a new Method command service spec.
     *
     * @param bean                 the bean
     * @param method               the method
     * @param userClass            the user class
     * @param commandParameterType the command parameter type
     */
    public MethodCommandServiceSpec(final Object bean, final Method method, final Class<?> userClass, final Class<?> commandParameterType) {
        this.bean = bean;
        this.method = method;
        this.userClass = userClass;
        this.commandParameterType = commandParameterType;
        Validate.isTrue(method.getParameterCount() == 1);
    }

    @Override
    public Object handle(final Object command) throws RuntimeException {
        try {
            return method.invoke(bean, command);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new CommandMethodHandlerInvokationException(e);
        }
    }
}
