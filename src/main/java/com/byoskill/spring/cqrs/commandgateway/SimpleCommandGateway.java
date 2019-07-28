package com.byoskill.spring.cqrs.commandgateway;


import org.apache.commons.lang3.Validate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class SimpleCommandGateway implements CommandGateway {

    private Gate gate;

    /**
     * Instantiates a new Simple command gateway.
     *
     * @param gate the gate
     */
    public SimpleCommandGateway(final Gate gate) {
        this.gate = gate;
        Validate.notNull(gate);
    }

    @Override
    public void send(final Object command) {
        gate.dispatchAsync(command);
    }

    @Override
    public void sendAndConsume(final Object command, final Consumer<?> callback) {
        gate.dispatchAsync(command).thenAccept((Consumer) callback);
    }

    @Override
    public <R, U> U sendAndApply(Object command, Function<R, U> callback) {
        final CompletableFuture<R> objectCompletableFuture = gate.dispatchAsync(command);
        try {
            return objectCompletableFuture.thenApply(callback).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new CqrsException(e);
        }
    }

    @Override
    public <R> void sendWithCallBack(Object command, BiConsumer<? super R, ? super Throwable> callback) {
        final CompletableFuture<R> objectCompletableFuture = gate.dispatchAsync(command);
        objectCompletableFuture.whenComplete(callback);
    }

    @Override
    public <R> R sendWithCallBackAndWait(Object command, BiConsumer<? super R, ? super Throwable> callback) throws ExecutionException, InterruptedException {
        final CompletableFuture<R> objectCompletableFuture = gate.dispatchAsync(command);
        return objectCompletableFuture.whenComplete(callback).get();
    }

    @Override
    public <R> CompletableFuture<R> send(final Object command, Class<R> expectedType) {
        return gate.dispatchAsync(command, expectedType);
    }

    @Override
    public <R> R sendAndWait(final Object command) {
        return gate.dispatch(command);
    }

    @Override
    public <R> R sendAndWait(final Object command, final long timeout, final TimeUnit unit) {
        try {
            return (R) gate.dispatchAsync(command).get(timeout, unit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new CqrsException(e);
        }
    }

    @Override
    public void sendNamedCommand(final String commandName) {
        this.gate.dispatchAsyncNamedCommand(commandName);
    }

    @Override
    public <R> R sendAndWaitNamedCommand(final String commandName) {
        return this.gate.dispatchNamedCommand(commandName);
    }

}
