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
package com.byoskill.spring.cqrs.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.byoskill.spring.cqrs.gate.conf.CommandServiceScanningConfiguration;
import com.byoskill.spring.cqrs.gate.conf.CqrsImportationSelector;
import com.byoskill.spring.cqrs.gate.conf.DefaultCqrsConfiguration;
import com.byoskill.spring.cqrs.gate.conf.GuavaAsyncEventBusConfiguration;

/**
 * This interface defines that the CQRS module should be enabled.
 *
 * @author sleroy
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(CqrsImportationSelector.class)
public @interface EnableCqrsModule {
    Class<?>[] customConfiguration() default { DefaultCqrsConfiguration.class, GuavaAsyncEventBusConfiguration.class,
	    CommandServiceScanningConfiguration.class };
}