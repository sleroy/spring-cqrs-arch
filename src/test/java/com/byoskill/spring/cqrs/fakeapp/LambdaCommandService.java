package com.byoskill.spring.cqrs.fakeapp;

import com.byoskill.spring.cqrs.commands.CommandServiceSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LambdaCommandService {

    @Bean
    public CommandServiceSpec<Command1, Long> exampleLambdaService() {
        return (command) -> 0L;
    }

    public static class Command1 {

    }
}
