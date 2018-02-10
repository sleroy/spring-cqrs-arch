package com.byoskill.spring.cqrs.gate.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import javax.validation.Validation;
import javax.validation.constraints.NotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.byoskill.spring.cqrs.api.HandlersProvider;
import com.byoskill.spring.cqrs.api.ICommandExecutionListener;
import com.byoskill.spring.cqrs.api.ICommandHandler;
import com.byoskill.spring.cqrs.api.ICommandProfilingService;
import com.byoskill.spring.cqrs.api.IThrottlingInterface;
import com.byoskill.spring.cqrs.gate.api.ICommandExceptionHandler;
import com.byoskill.spring.cqrs.gate.conf.CqrsConfiguration;
import com.byoskill.spring.cqrs.utils.validation.ObjectValidation;

public class CommandExecutorServiceTest {

    private static class InvalidObject {
	@NotNull
	String str;
    }

    private static final String COMMAND = "SALUT";

    private CqrsConfiguration configuration;
    private HandlersProvider  handlersProvider;

    private ICommandProfilingService profilingService;

    private CommandExecutorService service;

    private IThrottlingInterface throttlin;

    private final ObjectValidation validator = new ObjectValidation(
	    Validation.buildDefaultValidatorFactory().getValidator());

    ThreadPoolTaskExecutor tpool;

    public void after() {

	tpool.destroy();
    }

    @Before
    public void before() {

	configuration = new CqrsConfiguration();
	handlersProvider = Mockito.mock(HandlersProvider.class);
	profilingService = Mockito.mock(ICommandProfilingService.class);
	throttlin = Mockito.mock(IThrottlingInterface.class);
	tpool = new ThreadPoolTaskExecutor();
	service = new CommandExecutorService(configuration, handlersProvider, null, profilingService,
		Optional.<ICommandExceptionHandler>empty(), validator, throttlin, ForkJoinPool.commonPool());
	service.setConfiguration(configuration);
	service.setListeners(new ICommandExecutionListener[0]);
	tpool.initialize();

    }

    @Test
    public final void testRun() {
	configuration.setProfilingEnabled(false);
	Mockito.when(handlersProvider.getHandler(COMMAND)).thenReturn((ICommandHandler<String, String>) command -> command + " LA TERRE");

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
	Mockito.when(handlersProvider.getHandler(COMMAND)).thenReturn((ICommandHandler<String, String>) command -> {throw new UnsupportedOperationException();});
	assertNull(service.run(COMMAND, String.class).join());
    }

    @Test(expected = ExecutionException.class)
    public final void testRun_without_handler() throws InterruptedException, ExecutionException {
	configuration.setProfilingEnabled(false);
	service.run(COMMAND, Object.class).get();
    }
}
