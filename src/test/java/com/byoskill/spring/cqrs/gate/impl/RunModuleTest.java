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

import java.util.Properties;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.byoskill.spring.cqrs.gate.api.CommandHandlerNotFoundException;
import com.byoskill.spring.cqrs.gate.api.Gate;
import com.byoskill.spring.cqrs.gate.conf.CqrsInjectionConfiguration;
import com.byoskill.spring.cqrs.gate.conf.DefaultCqrsConfiguration;
import com.byoskill.spring.cqrs.gate.conf.GuavaAsyncEventBusConfiguration;
import com.byoskill.spring.cqrs.gate.impl.fakeapp.TestConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class, DefaultCqrsConfiguration.class,
	GuavaAsyncEventBusConfiguration.class, CqrsInjectionConfiguration.class, CommandServicePostProcessor.class })
public class RunModuleTest {

    @Autowired
    private Gate springGate;

    @Test
    public void test_happyPath() throws InterruptedException, ExecutionException, TimeoutException {
	springGate.dispatch("A");
	springGate.dispatch("B");
	springGate.dispatch("C");
	Assert.assertEquals(4, springGate.dispatchAsync("D").get(3, TimeUnit.SECONDS));

    }

    @Test(expected = CommandHandlerNotFoundException.class)
    public void testCommandNotFound() {

	springGate.dispatch(12);
    }

    @Test(expected = CompletionException.class)
    public void testInvalidation() {
	springGate.dispatch(new DummyObject());
    }

    @Test()
    public void testInvalidation_Success() {
	final DummyObject command = new DummyObject();
	command.field = "OK";
	springGate.dispatch(command);
    }

    @Test(expected = CommandHandlerNotFoundException.class)
    public void testPromised_onFailure() throws InterruptedException, ExecutionException {
	springGate.dispatchAsync(new Properties()).join();
	Thread.sleep(5000);
    }

}
