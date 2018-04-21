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

import com.byoskill.spring.cqrs.api.LoggingConfiguration;

public class DefaultLoggingConfiguration implements LoggingConfiguration {
    /** The logging enabled. */
    private boolean loggingEnabled   = true;
    private boolean profilingEnabled = true;

    @Override
    public boolean isLoggingEnabled() {
	return loggingEnabled;
    }

    @Override
    public boolean isProfilingEnabled() {

	return profilingEnabled;
    }

    public void setLoggingEnabled(final boolean loggingEnabled) {
	this.loggingEnabled = loggingEnabled;
    }

    public void setProfilingEnabled(final boolean profilingEnabled) {
	this.profilingEnabled = profilingEnabled;
    }

}
