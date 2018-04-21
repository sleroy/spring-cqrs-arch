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
package com.byoskill.spring.cqrs.gate.events.guava;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.PayloadApplicationEvent;

import com.byoskill.spring.cqrs.annotations.EventHandler;
import com.byoskill.spring.cqrs.gate.api.EventBusService;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

public class GuavaEventBusService implements ApplicationContextAware, EventBusService {

    private static final Logger	LOGGER = LoggerFactory.getLogger(GuavaEventBusService.class);
    private ApplicationContext	applicationContext;
    private EventBus		eventBus;

    private ExecutorService threadPoolTaskExecutor;

    /**
     * Instantiates a new guava event bus service.
     *
     * @param cqrsConfiguration
     *            the cqrs configuration
     */
    public GuavaEventBusService(final boolean asyncExecution) {
	if (asyncExecution) {
	    // The event bus should handle async event processing.
	    threadPoolTaskExecutor = Executors.newCachedThreadPool();
	    eventBus = new AsyncEventBus(threadPoolTaskExecutor);
	} else {
	    eventBus = new EventBus();
	}

    }

    @PreDestroy
    public void destroy() {
	eventBus = null;
	if (threadPoolTaskExecutor != null) {
	    threadPoolTaskExecutor.shutdown();
	}
    }

    private void lookingForEventSuscriberBeans() {
	final Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(EventHandler.class);
	for (final Entry<String, Object> beanEntry : beansWithAnnotation.entrySet()) {
	    final String beanId = beanEntry.getKey();
	    LOGGER.info("Registering an new event handler {}", beanId);
	    final Object bean = beanEntry.getValue();
	    registerEventSuscriber(bean);

	}
    }

    /* (non-Javadoc)
     * @see org.springframework.context.ApplicationEventPublisher#publishEvent(org.springframework.context.ApplicationEvent)
     */
    @Override
    public void publishEvent(final ApplicationEvent event) {
	eventBus.post(event);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.context.ApplicationEventPublisher#publishEvent(java.lang.
     * Object)
     */
    @Override
    public void publishEvent(final Object event) {
	eventBus.post(new PayloadApplicationEvent(new Exception().getStackTrace()[1], event));

    }

    /**
     * Register event suscriber.
     *
     * @param bean
     *            the bean
     */
    public void registerEventSuscriber(final Object bean) {
	eventBus.register(bean);
    }

    @Override
    public void setApplicationContext(final ApplicationContext _applicationContext) throws BeansException {
	applicationContext = _applicationContext;

	lookingForEventSuscriberBeans();

    }

}
