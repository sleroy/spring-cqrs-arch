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

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.byoskill.spring.cqrs.gate.api.Gate;
import com.byoskill.spring.cqrs.gate.impl.DummyObjectCommandHandler;

@Configuration
public class TestConfiguration {
    @Bean
    public DummyObjectCommandHandler dummyValidationHandler(final Gate gate) {
	return new DummyObjectCommandHandler();
    }

    @Bean
    public StringCommandHandler exampleHandler(final Gate gate) {
	return new StringCommandHandler(gate);
    }

    @Bean
    public RandomErrorNumberHandler randomErrorHandler(final Gate gate) {
	return new RandomErrorNumberHandler(gate);
    }

    @Bean
    public RandomNumberHandler randomHandler(final Gate gate) {
	return new RandomNumberHandler(gate);
    }

    @Bean
    Validator validator() {
	return Validation.buildDefaultValidatorFactory().getValidator();
    }

}
