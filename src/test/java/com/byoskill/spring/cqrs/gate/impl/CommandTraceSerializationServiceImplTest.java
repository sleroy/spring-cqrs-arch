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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.channels.UnsupportedAddressTypeException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.byoskill.spring.cqrs.api.CommandExecutionContext;
import com.byoskill.spring.cqrs.api.TraceConfiguration;

@RunWith(MockitoJUnitRunner.class)
public class CommandTraceSerializationServiceImplTest {

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

    private CommandTraceSerializationServiceImpl commandTraceSerializationServiceImpl;

    @After
    public void after() {
	commandTraceSerializationServiceImpl.shutdown();
    }

    @Before
    public void before() throws IOException {
	final TraceConfiguration cqrsConfiguration = new TraceConfiguration() {

	    @Override
	    public File getTraceFile() {
		try {
		    return File.createTempFile("trace", "json");
		} catch (final IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		return null;
	    }

	    @Override
	    public int getTraceSize() {
		return 1;
	    }

	    @Override
	    public boolean isTracingEnabled() {
		return true;
	    }

	};
	commandTraceSerializationServiceImpl = new CommandTraceSerializationServiceImpl(
		cqrsConfiguration);

    }

    @Test
    public void testOnFailure() throws Exception {
	final FakeCommand fakeCommand = new FakeCommand("FIELDA");
	final CommandExecutionContext commandExecutionContext = mock(CommandExecutionContext.class);
	when(commandExecutionContext.getRawCommand()).thenReturn(fakeCommand);
	commandTraceSerializationServiceImpl.onFailure(commandExecutionContext, new UnsupportedAddressTypeException());
	Assert.assertFalse(commandTraceSerializationServiceImpl.hasTraces());
    }

    @Test
    public void testOnSuccess() throws Exception {
	final FakeCommand fakeCommand = new FakeCommand("FIELDA");
	final CommandExecutionContext commandExecutionContext = mock(CommandExecutionContext.class);
	when(commandExecutionContext.getRawCommand()).thenReturn(fakeCommand);
	commandTraceSerializationServiceImpl.onSuccess(commandExecutionContext, "RESULT");
	Assert.assertFalse(commandTraceSerializationServiceImpl.hasTraces());
    }

}
