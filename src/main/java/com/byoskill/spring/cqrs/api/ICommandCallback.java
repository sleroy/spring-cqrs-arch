/**
 * Copyright (C) 2017-2018 Credifix
 */
package com.byoskill.spring.cqrs.api;

import java.util.concurrent.CompletableFuture;

/**
 * This interface describes a wrapper over a command. The execution of the
 * command is done with this interface.
 *
 * @author sleroy
 *
 * @param <R> returned type
 */
public interface ICommandCallback<R> {
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception
     *             if unable to compute a result
     */
    CompletableFuture<R> call() throws Exception;

}
