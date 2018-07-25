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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import javax.validation.Validation;
import javax.validation.constraints.NotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.byoskill.spring.cqrs.api.CommandServiceProvider;
import com.byoskill.spring.cqrs.api.CommandServiceSpec;
import com.byoskill.spring.cqrs.gate.conf.DefaultLoggingConfiguration;
import com.byoskill.spring.cqrs.utils.validation.ObjectValidation;
import com.byoskill.spring.cqrs.workflow.impl.CommandRunnerWorkflow;
import com.byoskill.spring.cqrs.workflow.impl.CommandRunnerWorkflowService;

public class CommandExecutorServiceImplTest {

    private static class InvalidObject {
	@NotNull
	String str;
    }

    private static final String COMMAND = "SALUT";

    private DefaultLoggingConfiguration	configuration;
    private CommandServiceProvider	handlersProvider;

    private CommandExecutorServiceImpl service;

    private final ObjectValidation validator = new ObjectValidation(
	    Validation.buildDefaultValidatorFactory().getValidator());

    ThreadPoolTaskExecutor tpool;

    private CommandRunnerWorkflowService workflowService;

    public void after() {

	tpool.destroy();
    }

    @Before
    public void before() {

	configuration = new DefaultLoggingConfiguration();
	handlersProvider = Mockito.mock(CommandServiceProvider.class);
	workflowService = mock(CommandRunnerWorkflowService.class);
	tpool = new ThreadPoolTaskExecutor();
	service = new CommandExecutorServiceImpl(configuration, handlersProvider, validator, workflowService,
		ForkJoinPool.commonPool());

	when(workflowService.getRunnerWorkflow()).thenReturn(new CommandRunnerWorkflow());

	tpool.initialize();

    }

    @Test
    public final void testRun() {
	configuration.setProfilingEnabled(false);
	Mockito.when(handlersProvider.getService(COMMAND))
		.thenReturn((CommandServiceSpec<String, String>) command -> command + " LA TERRE");

	assertEquals("SALUT LA TERRE", service.run(COMMAND, String.class).join());
    }

    @Test(expected = CompletionException.class)
    public final void testRun_invalid_command() {
	configuration.setProfilingEnabled(false);
	service.run(new InvalidObject(), Object.class).join();
    }

    @Test(expected = CompletionException.class)
    public final void testRun_withFailingHandler() {
	configuration.setProfilingEnabled(false);
	Mockito.when(handlersProvider.getService(COMMAND)).thenReturn((CommandServiceSpec<String, String>) command -> {
	    throw new UnsupportedOperationException();
	});
	assertNull(service.run(COMMAND, String.class).join());
    }

    @Test(expected = ExecutionException.class)
    public final void testRun_without_handler() throws InterruptedException, ExecutionException {
	configuration.setProfilingEnabled(false);
	service.run(COMMAND, Object.class).get();
    }
}
