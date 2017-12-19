/**
 * Copyright (C) 2017 Sylvain Leroy - BYOS Company All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the MIT license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the MIT license with
 * this file. If not, please write to: contact@sylvainleroy.com, or visit : https://sylvainleroy.com
 */
package com.byoskill.spring.cqrs.gate.events.guava;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.byoskill.spring.cqrs.annotations.EventHandler;
import com.byoskill.spring.cqrs.gate.api.IEventBusService;
import com.byoskill.spring.cqrs.gate.conf.CqrsConfiguration;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

@Service
@Profile("guava_bus")
public class GuavaEventBusService implements ApplicationContextAware, IEventBusService {

    private static final Logger	    LOGGER = LoggerFactory.getLogger(GuavaEventBusService.class);
    private ApplicationContext	    applicationContext;
    private final CqrsConfiguration cqrsConfiguration;
    private EventBus		    eventBus;

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * Instantiates a new guava event bus service.
     *
     * @param cqrsConfiguration
     *            the cqrs configuration
     */
    public GuavaEventBusService(final CqrsConfiguration cqrsConfiguration) {
	this.cqrsConfiguration = cqrsConfiguration;

    }

    @PreDestroy
    public void destroy() {
	eventBus = null;
	if (threadPoolTaskExecutor != null) {
	    threadPoolTaskExecutor.shutdown();
	}
    }

    @Override
    public void publishEvent(final ApplicationEvent event) {
	eventBus.post(event);

    }

    @Override
    public void publishEvent(final Object event) {
	eventBus.post(new PayloadApplicationEvent(new Exception().getStackTrace()[1], event));

    }

    /**
     * Register event suscriber.
     *
     * @param bean the bean
     */
    public void registerEventSuscriber(final Object bean) {
	eventBus.register(bean);
    }

    @Override
    public void setApplicationContext(final ApplicationContext _applicationContext) throws BeansException {
	applicationContext = _applicationContext;
	if (cqrsConfiguration.isAsyncEventQueries()) {
	    // The event bus should handle async event processing.
	    threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
	    threadPoolTaskExecutor.setCorePoolSize(cqrsConfiguration.getCorePoolSize());
	    threadPoolTaskExecutor.setMaxPoolSize(cqrsConfiguration.getMaxPoolSize());
	    threadPoolTaskExecutor.setQueueCapacity(cqrsConfiguration.getQueueCapacity());
	    threadPoolTaskExecutor.setKeepAliveSeconds(cqrsConfiguration.getKeepAliveSeconds());
	    threadPoolTaskExecutor.setThreadGroupName("cqrs-event-bus");
	    threadPoolTaskExecutor.setThreadNamePrefix("eventbus");
	    threadPoolTaskExecutor.initialize();
	    eventBus = new AsyncEventBus(threadPoolTaskExecutor);
	} else {
	    eventBus = new EventBus();
	}

	lookingForEventSuscriberBeans();

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

}
