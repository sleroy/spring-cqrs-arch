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

import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.byoskill.spring.cqrs.api.CommandExecutionContext;
import com.byoskill.spring.cqrs.gate.conf.DefaultLoggingConfiguration;

@RunWith(MockitoJUnitRunner.class)
public class CommandLoggingServiceImplTest {

    private final CommandLoggingServiceImpl service = new CommandLoggingServiceImpl(new DefaultLoggingConfiguration());

    @Test
    public void testOnFailure() throws Exception {
	final CommandExecutionContext mock = Mockito.mock(CommandExecutionContext.class);
	when(mock.getRawCommand()).thenReturn("EXAMPLE");
	service.onFailure(mock, new IOException());
    }

    @Test
    public void testOnSuccess() throws Exception {
	final CommandExecutionContext mock = Mockito.mock(CommandExecutionContext.class);
	when(mock.getRawCommand()).thenReturn("EXAMPLE");
	service.onSuccess(mock, "NI");
    }

}
