package com.byoskill.spring.cqrs.fakeapp.commands;

import javax.validation.constraints.NotEmpty;

public class DummyObject2Command {
    @NotEmpty
    public String field;
}