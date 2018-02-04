/**
 * Copyright (C) 2017-2018 Credifix
 */
package com.byoskill.spring.cqrs.api;

public interface IThrottlingInterface {

    /**
     * Acquire permit.
     *
     * @param name the name
     */
    void acquirePermit(String name);

}
