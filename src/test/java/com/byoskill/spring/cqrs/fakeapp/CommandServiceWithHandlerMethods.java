package com.byoskill.spring.cqrs.fakeapp;

import com.byoskill.spring.cqrs.annotations.CommandHandler;
import com.byoskill.spring.cqrs.annotations.CommandService;
import com.byoskill.spring.cqrs.fakeapp.commands.MethodWithReturnCommand;
import com.byoskill.spring.cqrs.fakeapp.commands.MethodWithoutReturnCommand;

@CommandService
public class CommandServiceWithHandlerMethods {

    public static final String HAS_BEEN_INVOKED = "HAS BEEN INVOKED";

    @CommandHandler
    public void handle(MethodWithoutReturnCommand command) {

    }


    @CommandHandler
    public Object handle(MethodWithReturnCommand command) {
        return HAS_BEEN_INVOKED;

    }

}
