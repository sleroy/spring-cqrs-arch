package com.byoskill.spring.cqrs.fakeapp.commands;

import jakarta.validation.constraints.NotEmpty;

public class DummyObjectCommand {
    @NotEmpty
    public
    String field;
}