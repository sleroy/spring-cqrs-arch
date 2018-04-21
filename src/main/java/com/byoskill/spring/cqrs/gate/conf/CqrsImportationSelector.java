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

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import com.byoskill.spring.cqrs.annotations.EnableCqrsModule;

/**
 * The Class CqrsImportationSelector is selecting the configuration to load for
 * the CQRS Module.
 */
public class CqrsImportationSelector implements ImportSelector {
    @Override
    public String[] selectImports(final AnnotationMetadata importingClassMetadata) {
	final AnnotationAttributes attributes = AnnotationAttributes.fromMap(
		importingClassMetadata.getAnnotationAttributes(EnableCqrsModule.class.getName(), false));
	final Class<?>[] customConfiguration = attributes.getClassArray("customConfiguration");
	final String[] classNames = new String[customConfiguration.length + 1];

	for (int i = 0; i < customConfiguration.length; i++) {
	    classNames[i] = customConfiguration[i].getName();
	}
	classNames[classNames.length - 1] = CqrsInjectionConfiguration.class.getName();

	return classNames;
    }
}
