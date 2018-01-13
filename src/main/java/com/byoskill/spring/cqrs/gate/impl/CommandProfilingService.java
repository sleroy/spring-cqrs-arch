/**
 * Copyright (C) 2017-2018 Credifix
 */
package com.byoskill.spring.cqrs.gate.impl;

import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.byoskill.spring.cqrs.api.ICommandProfilingService;

/**
 * The Class CommandProfilingService is handling command execution profiling.
 */
@Service
public class CommandProfilingService implements ICommandProfilingService {

    public static final class ProfilerImpl implements IProfiler {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandProfilingService.class);

	private final Object handler;

	private final StopWatch stopWatch;

	/**
	 * Instantiates a new profiling impl.
	 *
	 * @param handler
	 *            the handler
	 */
	public ProfilerImpl(final Object handler) {

	    this.handler = handler;
	    stopWatch = new StopWatch();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.byoskill.spring.cqrs.gate.impl.IProfiler#begin(java.lang.Object)
	 */
	@Override
	public CompletableFuture<Object> begin(final Object command) {
	    return CompletableFuture.supplyAsync(() -> {
		stopWatch.start();
		LOGGER.debug("[PROFILING][{}] started", handler);
		return command;
	    });

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.byoskill.spring.cqrs.gate.impl.IProfiler#end(java.lang.Object)
	 */
	@Override
	public CompletableFuture<Object> end(final Object result) {

	    return CompletableFuture.supplyAsync(() -> {
		stopWatch.stop();
		LOGGER.info("[PROFILING][{}]={} ms", handler, stopWatch.getTime());
		return result;
	    });
	}

    }

    @Override
    public IProfiler newProfiler(final Object handler) {

	return new ProfilerImpl(handler);
    }

}
