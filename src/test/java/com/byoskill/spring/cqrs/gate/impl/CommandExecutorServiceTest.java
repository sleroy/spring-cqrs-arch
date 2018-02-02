package com.byoskill.spring.cqrs.gate.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import javax.validation.Validation;
import javax.validation.constraints.NotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.byoskill.spring.cqrs.api.HandlersProvider;
import com.byoskill.spring.cqrs.api.IAsyncCommandHandler;
import com.byoskill.spring.cqrs.api.ICommandExecutionListener;
import com.byoskill.spring.cqrs.api.ICommandProfilingService;
import com.byoskill.spring.cqrs.gate.api.ICommandExceptionHandler;
import com.byoskill.spring.cqrs.gate.conf.CqrsConfiguration;
import com.byoskill.spring.utils.validation.ObjectValidation;

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

    private final ObjectValidation validator = new ObjectValidation(
	    Validation.buildDefaultValidatorFactory().getValidator());

    @Before
    public void before() {

	configuration = new CqrsConfiguration();
	handlersProvider = Mockito.mock(HandlersProvider.class);
	profilingService = Mockito.mock(ICommandProfilingService.class);
	service = new CommandExecutorService(configuration, handlersProvider, null, profilingService,
		Optional.<ICommandExceptionHandler>empty(), validator);
	service.setConfiguration(configuration);
	service.setListeners(new ICommandExecutionListener[0]);

    }

    @Test
    public final void testRun() {
	configuration.setProfilingEnabled(false);
	Mockito.when(handlersProvider.getHandler(COMMAND)).thenReturn((IAsyncCommandHandler) command -> {

	    return CompletableFuture.supplyAsync(() -> command + " LA TERRE");
	});

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
	Mockito.when(handlersProvider.getHandler(COMMAND)).thenReturn((IAsyncCommandHandler) command -> {

	    return CompletableFuture.supplyAsync(() -> {
		throw new UnsupportedOperationException();
	    });
	});
	assertNull(service.run(COMMAND, String.class).join());
    }

    @Test(expected = ExecutionException.class)
    public final void testRun_without_handler() throws InterruptedException, ExecutionException {
	configuration.setProfilingEnabled(false);
	service.run(COMMAND, Object.class).get();
    }
}
