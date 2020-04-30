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
package com.byoskill.spring.cqrs.commandgateway;

import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.byoskill.spring.cqrs.fakeapp.LambdaCommandService;
import com.byoskill.spring.cqrs.fakeapp.TestConfiguration;
import com.byoskill.spring.cqrs.commandservices.CommandServicePostProcessor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.byoskill.spring.cqrs.configuration.ImportCqrsInjectionConfiguration;
import com.byoskill.spring.cqrs.configuration.ImportDefaultCqrsConfiguration;
import com.byoskill.spring.cqrs.configuration.ImportGuavaEventBusConfiguration;
import com.byoskill.spring.cqrs.fakeapp.commands.RandomErrorNumberCommand;
import com.byoskill.spring.cqrs.fakeapp.commands.RandomNumberCommand;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class, ImportDefaultCqrsConfiguration.class,
        ImportGuavaEventBusConfiguration.class, ImportCqrsInjectionConfiguration.class, CommandServicePostProcessor.class,
        LambdaCommandService.class})

public class RealExampleIntegrationTest {

    @Autowired
    private Gate gate;

    @Test
    public void testGateDispatchAll() throws Exception {
        Assert.assertNotNull(gate.dispatch(new RandomNumberCommand(), Integer.class));

        final List<RandomNumberCommand> commands = IntStream.range(0, 20).mapToObj(it -> new RandomNumberCommand())
                .collect(Collectors.toList());
        final List<Integer> results = gate.dispatchAll(commands, Integer.class);
        Assert.assertEquals(20, results.size());
    }

    @Test(expected = CompletionException.class)
    public void testGateDispatchAllRandomErrors() throws Exception {
        Assert.assertNotNull(gate.dispatch(new RandomNumberCommand(), Integer.class));

        final List<RandomErrorNumberCommand> commands = IntStream.range(0, 20).mapToObj(it -> new RandomErrorNumberCommand())
                .collect(Collectors.toList());
        final List<Integer> results = gate.dispatchAll(commands, Integer.class);
        Assert.assertEquals(20, results.size());
    }

}
