package com.byoskill.spring.cqrs.commandservices;

import com.byoskill.spring.cqrs.commands.CommandServiceSpec;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NoArgMethodCommandServiceSpec implements CommandServiceSpec<Object, Object> {
    private final Object bean;
    private final Method method;

    /**
     * Instantiates a new Method command service spec.
     *
     * @param bean   the bean
     * @param method the method
     */
    public NoArgMethodCommandServiceSpec(final Object bean, final Method method) {
        this.bean = bean;
        this.method = method;
        Validate.isTrue(method.getParameterCount() == 0);
    }

    @Override
    public Object handle(final Object command) throws RuntimeException {
        try {
            return method.invoke(bean);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new CommandMethodHandlerInvokationException(e);
        }
    }
}
