package com.byoskill.spring.cqrs.fakeapp.commands;

import jakarta.validation.constraints.NotEmpty;

public class DummyObject2Command {
    @NotEmpty
    public String field;
}