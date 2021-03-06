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

import com.byoskill.spring.cqrs.configuration.LoggingConfiguration;

/**
 * The type Default logging configuration defines the defqult configuration fo the logging and profiling command runners.
 */
public class DefaultLoggingConfiguration implements LoggingConfiguration {
    /**
     * The logging enabled.
     */
    private boolean loggingEnabled = true;
    private boolean profilingEnabled = true;

    @Override
    public boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    public void setLoggingEnabled(final boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    @Override
    public boolean isProfilingEnabled() {

        return profilingEnabled;
    }

    public void setProfilingEnabled(final boolean profilingEnabled) {
        this.profilingEnabled = profilingEnabled;
    }

}
