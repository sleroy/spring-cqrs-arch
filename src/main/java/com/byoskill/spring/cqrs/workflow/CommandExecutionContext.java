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

import java.lang.annotation.Annotation;


/**
 * The Interface CommandExecutionContext provides the execution context of a command inside the command execution workflow.
 */
public interface CommandExecutionContext {

    /**
     * Gets the annotation.
     *
     * @param <A>             the generic type
     * @param annotationClass the annotation class
     * @return the annotation
     */
    <A extends Annotation> A getAnnotation(Class<A> annotationClass);

    /**
     * Gets the command.
     *
     * @param <T>  the generic type
     * @param impl the implementation class
     * @return the command
     */
    <T> T getCommand(Class<T> impl);

    /**
     * Gets the raw command.
     *
     * @return the raw command
     */
    Object getRawCommand();

    /**
     * Handler.
     *
     * @return the command service spec
     */
    CommandServiceSpec handler();

}
