package com.byoskill.spring.cqrs.gate.impl.fakeapp;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import com.byoskill.spring.cqrs.annotations.CommandHandler;
import com.byoskill.spring.cqrs.api.ICommandHandler;
import com.byoskill.spring.cqrs.gate.api.Gate;

@CommandHandler
public class RandomNumberHandler implements ICommandHandler<RandomNumber, Integer> {

    private final Random random;

    @Autowired
    public RandomNumberHandler(final Gate gate) {
	super();
	random = new Random();

    }

    @Override
    public Integer handle(final RandomNumber command) {
	return random.nextInt();
    }

}