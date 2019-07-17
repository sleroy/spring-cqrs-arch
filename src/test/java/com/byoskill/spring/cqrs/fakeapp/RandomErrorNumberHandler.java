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
package com.byoskill.spring.cqrs.fakeapp;

import java.util.Random;

import com.byoskill.spring.cqrs.fakeapp.commands.RandomErrorNumberCommand;
import org.springframework.beans.factory.annotation.Autowired;

import com.byoskill.spring.cqrs.commands.CommandServiceSpec;
import com.byoskill.spring.cqrs.commandgateway.Gate;

public class RandomErrorNumberHandler implements CommandServiceSpec<RandomErrorNumberCommand, Integer> {

    private final Random random;

    @Autowired
    public RandomErrorNumberHandler(final Gate gate) {
        super();
        random = new Random();

    }

    @Override
    public Integer handle(final RandomErrorNumberCommand command) {
        if (random.nextBoolean()) {
            return random.nextInt();
        } else {
            throw new RuntimeException("Random problem");
        }

    }

}