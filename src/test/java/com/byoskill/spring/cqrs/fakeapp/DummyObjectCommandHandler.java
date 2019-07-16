package com.byoskill.spring.cqrs.fakeapp;

import com.byoskill.spring.cqrs.commands.CommandServiceSpec;
import com.byoskill.spring.cqrs.fakeapp.commands.DummyObjectCommand;

public class DummyObjectCommandHandler implements CommandServiceSpec<DummyObjectCommand, Integer> {

    @Override
    public Integer handle(final DummyObjectCommand command)  {

	return 1;
    }

}