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

import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.byoskill.spring.cqrs.gate.api.Gate;
import com.byoskill.spring.cqrs.gate.conf.ImportCqrsInjectionConfiguration;
import com.byoskill.spring.cqrs.gate.conf.ImportDefaultCqrsConfiguration;
import com.byoskill.spring.cqrs.gate.conf.ImportGuavaEventBusConfiguration;
import com.byoskill.spring.cqrs.gate.impl.fakeapp.RandomErrorNumber;
import com.byoskill.spring.cqrs.gate.impl.fakeapp.RandomNumber;
import com.byoskill.spring.cqrs.gate.impl.fakeapp.TestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class, ImportDefaultCqrsConfiguration.class,
	ImportGuavaEventBusConfiguration.class, ImportCqrsInjectionConfiguration.class, CommandServicePostProcessor.class })

public class RealExampleTest {

    @Autowired
    private Gate gate;

    @Test
    public void testGateDispatchAll() throws Exception {
	Assert.assertNotNull(gate.dispatch(new RandomNumber(), Integer.class));

	final List<RandomNumber> commands = IntStream.range(0, 20).mapToObj(it -> new RandomNumber())
		.collect(Collectors.toList());
	final List<Integer> results = gate.dispatchAll(commands, Integer.class);
	Assert.assertEquals(20, results.size());
    }

    @Test(expected = CompletionException.class)
    public void testGateDispatchAllRandomErrors() throws Exception {
	Assert.assertNotNull(gate.dispatch(new RandomNumber(), Integer.class));

	final List<RandomErrorNumber> commands = IntStream.range(0, 20).mapToObj(it -> new RandomErrorNumber())
		.collect(Collectors.toList());
	final List<Integer> results = gate.dispatchAll(commands, Integer.class);
	Assert.assertEquals(20, results.size());
    }

}
