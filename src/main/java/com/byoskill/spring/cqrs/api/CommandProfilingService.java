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

import com.byoskill.spring.cqrs.gate.impl.IProfiler;

/**
 * Default interface to implement a Profile service.
 * 
 * @author sleroy
 *
 */
@FunctionalInterface
public interface CommandProfilingService {

    /**
     * New profiler.
     *
     * @param handler
     *            the handler
     * @return the profiler
     */
    IProfiler newProfiler(Object handler);

}
