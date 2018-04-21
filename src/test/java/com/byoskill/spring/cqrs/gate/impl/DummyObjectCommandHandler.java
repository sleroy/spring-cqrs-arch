package com.byoskill.spring.cqrs.gate.impl;

import com.byoskill.spring.cqrs.api.CommandServiceSpec;

public class DummyObjectCommandHandler implements CommandServiceSpec<DummyObject, Integer> {

    @Override
    public Integer handle(final DummyObject command)  {

	return 1;
    }

}