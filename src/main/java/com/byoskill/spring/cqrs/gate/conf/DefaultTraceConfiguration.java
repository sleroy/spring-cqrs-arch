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

import java.io.File;

import com.byoskill.spring.cqrs.api.TraceConfiguration;

public class DefaultTraceConfiguration implements TraceConfiguration {

    @Override
    public File getTraceFile() {
	return new File("cqrs-commands.trace");
    }

    @Override
    public int getTraceSize() {
	return 10;
    }

    @Override
    public boolean isTracingEnabled() {
	return false;
    }

}
