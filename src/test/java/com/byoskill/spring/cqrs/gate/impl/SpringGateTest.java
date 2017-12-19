package com.byoskill.spring.cqrs.gate.impl;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.byoskill.spring.cqrs.gate.impl.SequentialCommandExecutorService;
import com.byoskill.spring.cqrs.gate.impl.SpringGate;

@RunWith(MockitoJUnitRunner.class)
public class SpringGateTest {
	private static final String COMMAND = "GNI";

	
	@Mock
	private SequentialCommandExecutorService sequentialCommandExecutorService;

	@InjectMocks
	private SpringGate springGate;

	@Test
	public void testDispatch() throws Exception {
		springGate.dispatch(COMMAND);
		verify(sequentialCommandExecutorService, Mockito.times(1)).run(COMMAND);
	}
	


}
