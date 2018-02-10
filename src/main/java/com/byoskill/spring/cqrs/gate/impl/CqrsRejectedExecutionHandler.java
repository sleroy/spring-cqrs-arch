package com.byoskill.spring.cqrs.gate.impl;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CqrsRejectedExecutionHandler implements RejectedExecutionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CqrsRejectedExecutionHandler.class);

    @Override
    public void rejectedExecution(final Runnable worker, final ThreadPoolExecutor executor) {

	LOGGER.error("Worker {} has been rejected", worker.toString());

	try {
	    if (!executor.isShutdown()) {
		LOGGER.error("Retrying to Execute");

		executor.getQueue().put(worker);

	    }
	    LOGGER.warn(worker.toString() + " Execution Started");
	} catch (final Exception e) {
	    LOGGER.error("Failure to Re-execute the worker {} ", worker.toString(), e.getMessage(), e);
	}

    }
}
