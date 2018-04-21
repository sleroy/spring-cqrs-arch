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
package com.byoskill.spring.cqrs.gate.impl;

import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.byoskill.spring.cqrs.api.CommandProfilingService;

/**
 * The Class CommandProfilingService is handling command execution profiling.
 */
public class CommandProfilingServiceImpl implements CommandProfilingService {

    public static final class ProfilerImpl implements IProfiler {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandProfilingServiceImpl.class);

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
	 * @see com.byoskill.spring.cqrs.gate.impl.IProfiler#begin(java.lang.Object)
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
	 * @see com.byoskill.spring.cqrs.gate.impl.IProfiler#end(java.lang.Object)
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
