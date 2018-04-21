package com.byoskill.spring.cqrs.gate.impl;

import com.byoskill.spring.cqrs.api.CommandServiceSpec;

public class BrokenObjectCommandHandler implements CommandServiceSpec<DummyObject2, Integer> {

    @Override
    public Integer handle(final DummyObject2 command) {
	throw new RuntimeException();
    }

}