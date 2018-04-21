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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.byoskill.spring.cqrs.gate.conf.DefaultLoggingConfiguration;

@RunWith(MockitoJUnitRunner.class)
public class CommandLoggingServiceImplTest {

    private final CommandLoggingServiceImpl service = new CommandLoggingServiceImpl(new DefaultLoggingConfiguration());

    @Test
    public void testOnFailure() throws Exception {
	service.onFailure("gni", null);
    }

    @Test
    public void testOnSuccess() throws Exception {
	service.onSuccess("GNA", "NI");
    }

}
