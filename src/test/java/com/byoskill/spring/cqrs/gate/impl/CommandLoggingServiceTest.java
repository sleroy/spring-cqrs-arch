package com.byoskill.spring.cqrs.gate.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.byoskill.spring.cqrs.gate.conf.CqrsConfiguration;
import com.byoskill.spring.cqrs.gate.impl.CommandLoggingService;

@RunWith(MockitoJUnitRunner.class)
public class CommandLoggingServiceTest {

	@Mock
	private final CqrsConfiguration mock = new CqrsConfiguration();

	@InjectMocks
	private final CommandLoggingService service = new CommandLoggingService();
	
	@Test
	public void testOnFailure() throws Exception {
		service.onFailure("gni", null);
	}
	
	@Test
	public void testOnSuccess() throws Exception {
		service.onSuccess("GNA", "NI");
	}
	
}
