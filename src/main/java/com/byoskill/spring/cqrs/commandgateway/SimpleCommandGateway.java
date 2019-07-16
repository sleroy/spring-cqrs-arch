package com.byoskill.spring.cqrs.commandgateway;


import org.checkerframework.checker.units.qual.C;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class SimpleCommandGateway implements CommandGateway {

    private Gate gate;

    @Override
    public void send(final Object command) {
        gate.dispatchAsync(command);
    }

    @Override
    public void send(final Object command, final Consumer<?> callback) {
        gate.dispatchAsync(command).thenAccept((Consumer) callback);
    }

    @Override
    public <R, U> void send(final C command, final Function<R, U> callback) {
        final CompletableFuture<R> objectCompletableFuture = gate.dispatchAsync(command);
        objectCompletableFuture.thenApply(callback);
    }

    @Override
    public <R, U> void send(final C command, BiConsumer<? super R, ? super Throwable> callback) {
        final CompletableFuture<R> objectCompletableFuture = gate.dispatchAsync(command);
        objectCompletableFuture.whenComplete(callback);
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

}
