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

/**
 * The Interface RunnerState defines the state of the runner. A developer can
 * store some informations during the execution of a command by its command
 * handler as the transaction. Data are discarded when the
 */
public interface RunnerState {

    /**
     * Gets the data.
     *
     * @param <T>
     *            the generic type
     * @param key
     *            the key
     * @return the data
     */
    <T> T getData(String key);

    /**
     * Sets the data.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
    void setData(String key, Object value);
}
