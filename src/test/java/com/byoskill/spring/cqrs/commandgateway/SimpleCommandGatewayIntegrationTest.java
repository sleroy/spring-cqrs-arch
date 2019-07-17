package com.byoskill.spring.cqrs.commandgateway;

import com.byoskill.spring.cqrs.commandservices.CommandHandlerNotFoundException;
import com.byoskill.spring.cqrs.commandservices.CommandServicePostProcessor;
import com.byoskill.spring.cqrs.configuration.ImportCqrsInjectionConfiguration;
import com.byoskill.spring.cqrs.configuration.ImportDefaultCqrsConfiguration;
import com.byoskill.spring.cqrs.configuration.ImportGuavaAsyncEventBusConfiguration;
import com.byoskill.spring.cqrs.fakeapp.TestConfiguration;
import com.byoskill.spring.cqrs.fakeapp.commands.DummyObject2Command;
import com.byoskill.spring.cqrs.fakeapp.commands.DummyObjectCommand;
import com.byoskill.spring.cqrs.fakeapp.commands.RandomErrorNumberCommand;
import com.byoskill.spring.cqrs.fakeapp.commands.RandomNumberCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.function.Function;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class, ImportDefaultCqrsConfiguration.class,
        ImportGuavaAsyncEventBusConfiguration.class, ImportCqrsInjectionConfiguration.class, CommandServicePostProcessor.class})
public class SimpleCommandGatewayIntegrationTest {

    @Autowired
    CommandGateway commandGateway;

    @Test
    public void send() {
        commandGateway.send(new DummyObjectCommand());

    }

    @Test(expected = CommandHandlerNotFoundException.class)
    public void sendConsumerInvalid() {
        commandGateway.sendAndConsume(new DummyObject2Command(), System.out::println);
    }

    @Test
    public void sendConsumerValid() {
        final RandomErrorNumberCommand command = new RandomErrorNumberCommand();
        commandGateway.sendAndConsume(command, System.out::println);
    }

    @Test
    public void sendFunctionValid() {
        final RandomNumberCommand command = new RandomNumberCommand();
        commandGateway.sendAndApply(command, (Function<Integer, Integer>) r -> r * 2);
    }
}