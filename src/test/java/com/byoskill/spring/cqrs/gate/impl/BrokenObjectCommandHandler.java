package com.byoskill.spring.cqrs.gate.impl;

import com.byoskill.spring.cqrs.api.ICommandHandler;

public class BrokenObjectCommandHandler implements ICommandHandler<DummyObject2, Integer> {

    @Override
    public Integer handle(final DummyObject2 command) throws Exception {
	throw new RuntimeException();
    }

}