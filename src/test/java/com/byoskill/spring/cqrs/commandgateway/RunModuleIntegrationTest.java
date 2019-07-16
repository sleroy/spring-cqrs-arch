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

import java.util.Properties;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.byoskill.spring.cqrs.fakeapp.CommandServiceWithHandlerMethods;
import com.byoskill.spring.cqrs.fakeapp.TestConfiguration;
import com.byoskill.spring.cqrs.commandservices.CommandServicePostProcessor;
import com.byoskill.spring.cqrs.fakeapp.commands.DummyObjectCommand;
import com.byoskill.spring.cqrs.fakeapp.commands.MethodWithReturnCommand;
import com.byoskill.spring.cqrs.fakeapp.commands.MethodWithoutReturnCommand;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.byoskill.spring.cqrs.commandservices.CommandHandlerNotFoundException;
import com.byoskill.spring.cqrs.configuration.ImportCqrsInjectionConfiguration;
import com.byoskill.spring.cqrs.configuration.ImportDefaultCqrsConfiguration;
import com.byoskill.spring.cqrs.configuration.ImportGuavaAsyncEventBusConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class, ImportDefaultCqrsConfiguration.class,
        ImportGuavaAsyncEventBusConfiguration.class, ImportCqrsInjectionConfiguration.class, CommandServicePostProcessor.class})
public class RunModuleIntegrationTest {

    @Autowired
    private Gate gate;

    @Test
    public void test_happyPath() throws InterruptedException, ExecutionException, TimeoutException {
        gate.dispatch("A");
        gate.dispatch("B");
        gate.dispatch("C");
        Assert.assertEquals(4, gate.dispatchAsync("D").get(3, TimeUnit.SECONDS));

    }

    @Test(expected = CommandHandlerNotFoundException.class)
    public void testCommandNotFound() {

        gate.dispatch(12);
    }

    @Test(expected = CompletionException.class)
    public void testInvalidation() {
        gate.dispatch(new DummyObjectCommand());
    }

    @Test()
    public void testInvalidation_Success() {
        final DummyObjectCommand command = new DummyObjectCommand();
        command.field = "OK";
        gate.dispatch(command);
    }

    @Test(expected = CommandHandlerNotFoundException.class)
    public void testPromised_onFailure() throws InterruptedException, ExecutionException {
        gate.dispatchAsync(new Properties()).join();
        Thread.sleep(5000);
    }

    @Test
    public void testCommandWithHandlerMethods() {
        gate.dispatch(new MethodWithoutReturnCommand());
        Assert.assertEquals(CommandServiceWithHandlerMethods.HAS_BEEN_INVOKED, gate.dispatch(new MethodWithReturnCommand(), String.class));
    }
}
