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
package com.byoskill.spring.cqrs.gate.conf;

import com.byoskill.spring.cqrs.api.ThrottlingInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultThrottlingInterface implements ThrottlingInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultThrottlingInterface.class);

    @Override
    public void acquirePermit(final String name) {
        LOGGER.warn("CQRS Default configuration does not provide support for @Throttle annotations");

    }

}
