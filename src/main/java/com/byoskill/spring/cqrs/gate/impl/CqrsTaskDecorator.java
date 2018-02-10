package com.byoskill.spring.cqrs.gate.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskDecorator;

public class CqrsTaskDecorator implements TaskDecorator {
    private static final Logger LOGGER = LoggerFactory.getLogger(CqrsTaskDecorator.class);

    @Override
    public Runnable decorate(final Runnable runnable) {

	return runnable;
    }
}
