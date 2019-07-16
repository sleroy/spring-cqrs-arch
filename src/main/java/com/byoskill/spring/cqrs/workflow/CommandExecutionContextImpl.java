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
package com.byoskill.spring.cqrs.workflow;

import com.byoskill.spring.cqrs.commands.CommandServiceSpec;
import com.byoskill.spring.cqrs.workflow.CommandExecutionContext;

import java.lang.annotation.Annotation;

public class CommandExecutionContextImpl implements CommandExecutionContext {
    private final Object command;
    private final CommandServiceSpec handler;

    /**
     * Instantiates a new command execution context contains all references to the
     * command and its service.F
     *
     * @param handler the command handler
     * @param command the command
     */
    public CommandExecutionContextImpl(final CommandServiceSpec handler, final Object command) {
        this.handler = handler;
        this.command = command;
    }

    @Override
    public <A extends Annotation> A getAnnotation(final Class<A> annotationClass) {
        return command.getClass().getAnnotation(annotationClass);
    }

    @Override
    public <T> T getCommand(final Class<T> impl) {
        return impl.cast(command);
    }

    @Override
    public Object getRawCommand() {
        return command;
    }

    @Override
    public CommandServiceSpec handler() {
        return handler;
    }

}
