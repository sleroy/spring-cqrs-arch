package com.byoskill.spring.cqrs.gate.impl;

import java.lang.Thread.UncaughtExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CqrsUncaughtExceptionhandler implements UncaughtExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CqrsUncaughtExceptionhandler.class);

    @Override
    public void uncaughtException(final Thread worker, final Throwable e) {

	LOGGER.error("Worker {} has crashed", worker.getName());
	LOGGER.error("Worker crash reason : ", e);

    }
}
