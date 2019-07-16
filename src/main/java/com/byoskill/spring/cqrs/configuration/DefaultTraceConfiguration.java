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
package com.byoskill.spring.cqrs.configuration;

import com.byoskill.spring.cqrs.configuration.TraceConfiguration;

import java.io.File;

public class DefaultTraceConfiguration implements TraceConfiguration {

    private File traceFile = new File("cqrs-commands.trace");

    private int traceSize = 10;

    private boolean enabled = false;

    public DefaultTraceConfiguration() {
        super();
    }

    public DefaultTraceConfiguration(final File traceFile, final int traceSize, final boolean enabled) {
        super();
        this.traceFile = traceFile;
        this.traceSize = traceSize;
        this.enabled = enabled;
    }

    @Override
    public File getTraceFile() {
        return traceFile;
    }

    @Override
    public int getTraceSize() {
        return traceSize;
    }

    @Override
    public boolean isTracingEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return "DefaultTraceConfiguration [traceFile=" + traceFile + ", traceSize=" + traceSize + ", enabled=" + enabled
                + "]";
    }

}
