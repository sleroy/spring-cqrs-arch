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
package com.byoskill.spring.cqrs.gate.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.byoskill.spring.cqrs.api.CommandServiceProvider;
import com.byoskill.spring.cqrs.gate.impl.CommandServicePostProcessor;

@Configuration
public class CommandServiceScanningConfiguration {

    @Bean
    public CommandServicePostProcessor processor(final CommandServiceProvider commandServiceProvider) {
	return new CommandServicePostProcessor(commandServiceProvider);
    }
}
