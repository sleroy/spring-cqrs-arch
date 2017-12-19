package com.byoskill.spring.cqrs.gate.impl;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.byoskill.spring.cqrs.gate.conf.CqrsConfiguration;

@RunWith(MockitoJUnitRunner.class)
public class CommandTraceSerializationServiceTest {

    static class FakeCommand {
	private String field;

	public FakeCommand(final String _field) {
	    super();
	    field = _field;
	}

	public String getField() {
	    return field;
	}

	public void setField(final String _field) {
	    field = _field;
	}
    }

    private CommandTraceSerializationService commandTraceSerializationService;

    @After
    public void after() {
	commandTraceSerializationService.shutdown();
    }

    @Before
    public void before() throws IOException {
	final CqrsConfiguration cqrsConfiguration = new CqrsConfiguration();
	cqrsConfiguration.setTracingEnabled(true);
	cqrsConfiguration.setTraceSize(1);
	cqrsConfiguration.setTraceFile(File.createTempFile("trace", "json"));
	commandTraceSerializationService = new CommandTraceSerializationService(
		cqrsConfiguration);

    }

    @Test
    public void testOnFailure() throws Exception {
	commandTraceSerializationService.onFailure(new FakeCommand("FIELDA"), new UnsupportedOperationException());
	Assert.assertFalse(commandTraceSerializationService.hasTraces());
    }

    @Test
    public void testOnSuccess() throws Exception {
	commandTraceSerializationService.onSuccess(new FakeCommand("FIELDA"), "RESULT");
	Assert.assertFalse(commandTraceSerializationService.hasTraces());
    }

}
