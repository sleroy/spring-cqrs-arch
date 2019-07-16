package com.byoskill.spring.cqrs.fakeapp;

import com.byoskill.spring.cqrs.commands.CommandServiceSpec;
import com.byoskill.spring.cqrs.fakeapp.commands.DummyObject2Command;

public class BrokenObjectCommandHandler implements CommandServiceSpec<DummyObject2Command, Integer> {

    @Override
    public Integer handle(final DummyObject2Command command) {
	throw new RuntimeException();
    }

}