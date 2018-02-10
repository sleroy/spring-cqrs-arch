package com.byoskill.spring.cqrs.gate.conf;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.byoskill.spring.cqrs.gate.impl.CqrsUncaughtExceptionhandler;

@Configuration
public class CqrsThreadPoolConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(CqrsThreadPoolConfiguration.class);

    @Autowired
    private CqrsConfiguration cqrsConfiguration;

    @Bean(name = "cqrs-executor")
    public ForkJoinPool cqrsThreadExecutor(final UncaughtExceptionHandler rejectionHandler) {
	final ForkJoinWorkerThreadFactory factory = pool -> {
	    final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
	    worker.setName("cqrs-command-" + worker.getPoolIndex());
	    return worker;
	};

	final ForkJoinPool forkJoinPool = new ForkJoinPool(cqrsConfiguration.getParallelism(), factory,
		rejectionHandler, true);
	return forkJoinPool;

    }



    @Bean
    public UncaughtExceptionHandler uncaughtExceptionhandler() {
	return new CqrsUncaughtExceptionhandler();
    }
}
