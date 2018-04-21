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
package com.byoskill.spring.cqrs.gate.impl.fakeapp;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import com.byoskill.spring.cqrs.api.CommandServiceSpec;
import com.byoskill.spring.cqrs.gate.api.Gate;

public class RandomErrorNumberHandler implements CommandServiceSpec<RandomErrorNumber, Integer> {

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