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
package com.byoskill.spring.cqrs.throttling;

@FunctionalInterface
public interface ThrottlingInterface {

    /**
     * Acquire a permit before executing a command. Usually the rate limitation are identified with a unique identifier.
     *
     * Each time this mehod is invoked; a rate limiter may be invoked with its own limitations.
     *
     * @param name the name of the permit to access to the specific resource.
     */
    void acquirePermit(String name);

}
