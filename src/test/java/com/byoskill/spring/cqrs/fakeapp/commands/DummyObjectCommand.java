package com.byoskill.spring.cqrs.fakeapp.commands;

import javax.validation.constraints.NotEmpty;

public class DummyObjectCommand {
    @NotEmpty
    public
    String field;
}