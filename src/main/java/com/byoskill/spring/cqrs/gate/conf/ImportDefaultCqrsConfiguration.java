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
package com.byoskill.spring.cqrs.gate.conf;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.byoskill.spring.cqrs.api.CqrsConfiguration;
import com.byoskill.spring.cqrs.api.LoggingConfiguration;
import com.byoskill.spring.cqrs.api.ThrottlingInterface;
import com.byoskill.spring.cqrs.api.TraceConfiguration;
import com.byoskill.spring.cqrs.gate.impl.CqrsUncaughtExceptionhandler;

@Configuration
public class ImportDefaultCqrsConfiguration implements CqrsConfiguration {

    public ImportDefaultCqrsConfiguration() {

    }

    @Bean
    @Override
    public ForkJoinPool getForkJoinPool() {
	final ForkJoinWorkerThreadFactory factory = pool -> {
	    final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
	    worker.setName("cqrs-command-" + worker.getPoolIndex());
	    return worker;
	};

	return new ForkJoinPool(Runtime.getRuntime().availableProcessors(), factory,
		uncaughtExceptionhandler(), true);

    }

    @Bean
    @Override
    public LoggingConfiguration getLoggingConfiguration() {

	return new DefaultLoggingConfiguration();
    }

    @Bean
    @Override
    public ThrottlingInterface getThrottlingInterface() {
	return new DefaultThrottlingInterface();
    }

    @Bean
    @Override
    public TraceConfiguration getTraceConfiguration() {
	return new DefaultTraceConfiguration();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.byoskill.spring.cqrs.api.CqrsConfiguration#uncaughtExceptionhandler()
     */
    public UncaughtExceptionHandler uncaughtExceptionhandler() {
	return new CqrsUncaughtExceptionhandler();
    }
}
