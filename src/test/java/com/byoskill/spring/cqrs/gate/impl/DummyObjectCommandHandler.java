package com.byoskill.spring.cqrs.gate.impl;

import com.byoskill.spring.cqrs.api.ICommandHandler;

public class DummyObjectCommandHandler implements ICommandHandler<DummyObject, Integer> {

    @Override
    public Integer handle(final DummyObject command) throws Exception {

        return 1;
    }

}