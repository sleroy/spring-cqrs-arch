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
package com.byoskill.spring.cqrs.api;

/**
 * The Interface HandlersProvider.
 */
public interface CommandServiceProvider {

    /**
     * Gets the service associated to this command.
     *
     * @param command
     *            the command
     * @return the command runner
     */
    public CommandServiceSpec getService(final Object command);

    /**
     * Put a command. It's relation is analyzed using the CommandServiceSpec
     * interface.
     *
     * @param bean
     *            the bean
     * @param beanName
     *            the bean name
     */
    public void putCommand(Object bean, String beanName);
}