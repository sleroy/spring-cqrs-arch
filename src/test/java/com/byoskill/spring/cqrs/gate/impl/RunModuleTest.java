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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.byoskill.spring.cqrs.gate.api.CommandHandlerNotFoundException;
import com.byoskill.spring.cqrs.gate.api.Gate;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("guava_bus")
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
	final Object promise = springGate.dispatchAsync(new Properties()).join();
	Thread.sleep(5000);
    }

}
