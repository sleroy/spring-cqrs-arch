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
package com.byoskill.spring.cqrs.events.guava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.byoskill.spring.cqrs.annotations.EventHandler;

/**
 * This class defines a bean process that will map Spring beans with @Subscribe methods as event handlers.
 */
public class GuavaEventBusPostProcessor implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuavaEventBusPostProcessor.class);
    private final GuavaEventBusService eventBusService;

    /**
     * Instantiates a new command service post processor.
     *
     * @param eventBusService the command service provider
     */
    @Autowired
    public GuavaEventBusPostProcessor(final GuavaEventBusService eventBusService) {
        this.eventBusService = eventBusService;

    }

    /*
     *
     *
     * (non-Javadoc)
     *
     * @see org.springframework.beans.factory.config.BeanPostProcessor#
     * postProcessAfterInitialization(java.lang.Object, java.lang.String)
     */
    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        final Class<?> ultimateTargetClass = AopProxyUtils.ultimateTargetClass(bean);
        if (ultimateTargetClass.isAnnotationPresent(EventHandler.class)) {
            LOGGER.info("Registering an event handler {}->{}", beanName, bean);
            eventBusService.registerEventSuscriber(bean);
        }
        return bean;
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
}
