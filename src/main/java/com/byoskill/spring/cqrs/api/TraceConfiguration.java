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
package com.byoskill.spring.cqrs.api;

import java.io.File;

public interface TraceConfiguration {
    /**
     * Gets the trace file.
     *
     * @return the trace file
     */
    File getTraceFile();

    /**
     * Gets the trace size.
     *
     * @return the trace size
     */
    int getTraceSize();

    /**
     * Checks if is tracing enabled.
     *
     * @return true, if is tracing enabled
     */
    boolean isTracingEnabled();
}
