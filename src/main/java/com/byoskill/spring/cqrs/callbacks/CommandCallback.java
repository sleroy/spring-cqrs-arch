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
package com.byoskill.spring.cqrs.callbacks;

import java.util.concurrent.CompletableFuture;

/**
 * This interface describes a wrapper over a command. The execution of the
 * command is done with this interface.
 *
 * @author sleroy
 *
 * @param <R>
 *            returned type
 */
@FunctionalInterface
public interface CommandCallback<R> {
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception
     *             if unable to compute a result
     */
    CompletableFuture<R> call() throws Exception;

}
