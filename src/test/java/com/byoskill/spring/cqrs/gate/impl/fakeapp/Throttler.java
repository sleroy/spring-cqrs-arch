package com.byoskill.spring.cqrs.gate.impl.fakeapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.byoskill.spring.cqrs.api.IThrottlingInterface;
import com.google.common.util.concurrent.RateLimiter;

public class Throttler implements IThrottlingInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(Throttler.class);
    private final RateLimiter create;

    public Throttler() {
	create = RateLimiter.create(10);
    }

    @Override
    public void acquirePermit(final String name) {
	create.acquire();

    }
}
