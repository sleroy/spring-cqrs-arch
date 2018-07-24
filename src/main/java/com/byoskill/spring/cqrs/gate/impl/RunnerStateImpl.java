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
package com.byoskill.spring.cqrs.gate.impl;

import java.util.HashMap;

import com.byoskill.spring.cqrs.api.RunnerState;

public class RunnerStateImpl extends HashMap<String, Object> implements RunnerState {

    @Override
    public <T> T getData(final String key) {
	return (T) get(key);
    }

    @Override
    public void setData(final String key, final Object value) {
	put(key, value);

    }
}
