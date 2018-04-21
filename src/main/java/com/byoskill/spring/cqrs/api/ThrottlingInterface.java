/**
 * Copyright (C) 2017-2018 Credifix
 */
package com.byoskill.spring.cqrs.api;

public interface ThrottlingInterface {

    /**
     * Acquire permit.
     *
     * @param name the name
     */
    void acquirePermit(String name);

}
