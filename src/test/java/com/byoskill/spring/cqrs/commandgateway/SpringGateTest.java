package com.byoskill.spring.cqrs.commandgateway;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.byoskill.spring.cqrs.workflow.CommandExecutorServiceImpl;
import com.byoskill.spring.cqrs.commandgateway.SpringGate;
import com.byoskill.spring.cqrs.filter.SpringGateFilters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.byoskill.spring.cqrs.events.EventBusService;

@RunWith(MockitoJUnitRunner.class)
public class SpringGateTest {
    private static final String COMMAND = "GNI";

    @Mock
    private CommandExecutorServiceImpl commandExecutorServiceImpl;

    @Mock
    private EventBusService eventBus;

    private final Optional<SpringGateFilters> spg = Optional.empty();

    private SpringGate springGate;

    @Before
    public void before() {
        springGate = new SpringGate(commandExecutorServiceImpl, eventBus, spg);
    }

    @Test
    public void testDispatch() throws Exception {
        when(commandExecutorServiceImpl.run(COMMAND, Object.class))
                .thenReturn(CompletableFuture.supplyAsync(() -> null));
        springGate.dispatch(COMMAND);
        verify(commandExecutorServiceImpl, Mockito.times(1)).run(COMMAND, Object.class);
    }

    @Test
    public void testDispatchAsync() throws Exception {
        when(commandExecutorServiceImpl.run(COMMAND, Object.class))
                .thenReturn(CompletableFuture.supplyAsync(() -> null));
        springGate.dispatchAsync(COMMAND);
        verify(commandExecutorServiceImpl, Mockito.times(1)).run(COMMAND, Object.class);
    }

    @Test
    public void testDispatchExpect() throws Exception {
        when(commandExecutorServiceImpl.run(COMMAND, String.class))
                .thenReturn(CompletableFuture.supplyAsync(() -> null));
        springGate.dispatch(COMMAND, String.class);
        verify(commandExecutorServiceImpl, Mockito.times(1)).run(COMMAND, String.class);
    }

    @Test
    public void testDispatchExpectAsync() throws Exception {
        when(commandExecutorServiceImpl.run(COMMAND, String.class))
                .thenReturn(CompletableFuture.supplyAsync(() -> null));
        springGate.dispatchAsync(COMMAND, String.class);
        verify(commandExecutorServiceImpl, Mockito.times(1)).run(COMMAND, String.class);
    }

}
