/**
 * Copyright (C) 2017-2018 Credifix
 */
package com.byoskill.spring.cqrs.gate.impl;

import java.util.concurrent.CompletableFuture;

/**
 * The Interface IProfiler.
 */
public interface IProfiler {

    /**
     * Begin.
     *
     * @param command the command
     * @return the command
     */
    CompletableFuture<Object> begin(Object command);

    /**
     * End.
     *
     * @param command the command
     * @return the command
     */
    CompletableFuture<Object>  end(Object command);
}
