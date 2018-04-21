/**
 * Copyright (C) 2017 Sylvain Leroy - BYOS Company All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the MIT license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the MIT license with
 * this file. If not, please write to: contact@sylvainleroy.com, or visit : https://sylvainleroy.com
 */
package com.byoskill.spring.cqrs.api;

import com.byoskill.spring.cqrs.gate.impl.IProfiler;

/**
 * Default interface to implement a Profile service.
 * @author sleroy
 *
 */
public interface CommandProfilingService {


    /**
     * New profiler.
     *
     * @param handler the handler
     * @return the profiler
     */
    IProfiler newProfiler(Object handler);

}
