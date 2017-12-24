package com.byoskill.spring.cqrs.gate.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.validation.constraints.NotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import com.byoskill.spring.cqrs.api.HandlersProvider;
import com.byoskill.spring.cqrs.api.ICommandExecutionListener;
import com.byoskill.spring.cqrs.api.ICommandHandler;
import com.byoskill.spring.cqrs.api.ICommandProfilingService;
import com.byoskill.spring.cqrs.gate.api.CommandExecutionException;
import com.byoskill.spring.cqrs.gate.api.CommandHandlerNotFoundException;
import com.byoskill.spring.cqrs.gate.api.InvalidCommandException;
import com.byoskill.spring.cqrs.gate.conf.CqrsConfiguration;


public class CommandExecutorServiceTest {

    private static class InvalidObject {
	@NotNull
	String str;
    }

    private static final String COMMAND = "SALUT";

    private final CqrsConfiguration configuration = new CqrsConfiguration();

    private final HandlersProvider handlersProvider = Mockito.mock(HandlersProvider.class);

    private final ICommandProfilingService profilingService = Mockito.mock(ICommandProfilingService.class);


    @InjectMocks
    private final CommandExecutorService service = new CommandExecutorService(
	    configuration,
	    handlersProvider,
	    null,
	    profilingService,
	    null
	    );

    @Before
    public void before() {
	service.setConfiguration(configuration);
	service.setListeners(new ICommandExecutionListener[0]);
    }

    @Test
    public final void testRun() {
	configuration.setProfilingEnabled(false);
	Mockito.when(handlersProvider.getHandler(COMMAND)).thenReturn((ICommandHandler<Object, Object>) _command -> _command + " LA TERRE");
	assertEquals("SALUT LA TERRE", service.run(COMMAND));
    }

    @Test(expected = InvalidCommandException.class)
    public final void testRun_invalid_command() {
	configuration.setProfilingEnabled(false);
	service.run(new InvalidObject());
    }

    @Test(expected = CommandExecutionException.class)
    public final void testRun_withFailingHandler() {
	configuration.setProfilingEnabled(false);
	final ICommandHandler<Object, Object> handler = _command -> {

	    throw new UnsupportedOperationException();
	};
	Mockito.when(handlersProvider.getHandler(COMMAND)).thenReturn(handler);
	assertNull(service.run(COMMAND));
    }

    @Test(expected = CommandHandlerNotFoundException.class)
    public final void testRun_without_handler() {
	configuration.setProfilingEnabled(false);
	service.run(COMMAND);
    }
}
