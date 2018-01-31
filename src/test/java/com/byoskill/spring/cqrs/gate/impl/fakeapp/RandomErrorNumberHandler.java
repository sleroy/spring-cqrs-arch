package com.byoskill.spring.cqrs.gate.impl.fakeapp;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import com.byoskill.spring.cqrs.annotations.CommandHandler;
import com.byoskill.spring.cqrs.api.ICommandHandler;
import com.byoskill.spring.cqrs.gate.api.Gate;

@CommandHandler
public class RandomErrorNumberHandler implements ICommandHandler<RandomErrorNumber, Integer> {

    private final Random random;

    @Autowired
    public RandomErrorNumberHandler(final Gate gate) {
	super();
	random = new Random();

    }

    @Override
    public Integer handle(final RandomErrorNumber command) {
	if (random.nextBoolean()) {
	    return random.nextInt();
	} else {
	    throw new RuntimeException("Random problem");
	}

    }

}