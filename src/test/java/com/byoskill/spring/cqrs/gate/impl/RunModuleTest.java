package com.byoskill.spring.cqrs.gate.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.validation.constraints.NotEmpty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.byoskill.spring.cqrs.api.ICommandHandler;
import com.byoskill.spring.cqrs.gate.api.CommandHandlerNotFoundException;
import com.byoskill.spring.cqrs.gate.api.Gate;
import com.byoskill.spring.cqrs.gate.api.InvalidCommandException;
import com.byoskill.spring.cqrs.gate.conf.CqrsConfiguration;

import junit.framework.Assert;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=RunModuleTest.class)
@Configuration
@ComponentScan("com.byoskill")
@ActiveProfiles("guava_bus")
public class RunModuleTest {

    public static class DummyObject {
	@NotEmpty
	String field;
    }

    public static class DummyObjectCommandHandler implements ICommandHandler<DummyObject, Integer> {

	@Override
	public Integer handle(final DummyObject command) throws Exception {

	    return 1;
	}

    }


    public static class StringCommandHandler implements ICommandHandler<String, Integer> {

	private final Gate gate;

	@Autowired
	public  StringCommandHandler(final Gate gate) {
	    super();
	    this.gate = gate;

	}

	@Override
	public Integer handle(final String command) throws Exception {
	    commands.add(command);
	    gate.dispatchEvent("EVENT_"+ command);
	    return commands.size();
	}

    }

    private static final List<String> commands = new ArrayList<>();

    @Autowired
    private Gate springGate;

    @Bean
    public CqrsConfiguration configuration() {
	return new CqrsConfiguration();
    }

    @Bean
    public DummyObjectCommandHandler dummyValidationHandler(final Gate gate) {
	return new DummyObjectCommandHandler();
    }

    @Bean
    public StringCommandHandler exampleHandler(final Gate gate) {
	return new StringCommandHandler(gate);
    }


    @Test
    public void test() throws InterruptedException, ExecutionException, TimeoutException {
	springGate.dispatch("A");
	springGate.dispatch("B");
	springGate.dispatch("C");
	Assert.assertEquals(4, springGate.dispatchAsync("D").get(3, TimeUnit.SECONDS));



    }
    @Test(expected=CommandHandlerNotFoundException.class)
    public void testCommandNotFound() {

	springGate.dispatch(12);
    }

    @Test(expected=InvalidCommandException.class)
    public void testInvalidation() {
	springGate.dispatch(new DummyObject());
    }

    @Test()
    public void testInvalidation_Success() {
	final DummyObject command = new DummyObject();
	command.field = "OK";
	springGate.dispatch(command);
    }
}
