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

    /**
     * Sends a command asynchronously and consumes the result using a callback.
     *
     * @param command the command to be sent
     * @param callback the callback to be invoked when the command is processed
     */
    @Override
    public void sendAndConsume(final Object command, final Consumer<?> callback) {
        gate.dispatchAsync(command).thenAccept((Consumer) callback);
    }

    /**
     * Sends a command asynchronously, applies a function to the result, and returns the transformed value.
     *
     * @param command the command to be sent
     * @param callback the function to be applied to the result
     * @param <R> the type of the command result
     * @param <U> the type of the transformed result
     * @return the transformed result
     */
    @Override
    public <R, U> U sendAndApply(Object command, Function<R, U> callback) {
        final CompletableFuture<R> objectCompletableFuture = gate.dispatchAsync(command);
        try {
            return objectCompletableFuture.thenApply(callback).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new CqrsException(e);
        }
    }

    /**
     * Sends a command asynchronously and invokes a callback when the result is available.
     *
     * @param command the command to be sent
     * @param callback the callback to be invoked when the command is processed
     * @param <R> the type of the command result
     */
    @Override
    public <R> void sendWithCallBack(Object command, BiConsumer<? super R, ? super Throwable> callback) {
        final CompletableFuture<R> objectCompletableFuture = gate.dispatchAsync(command);
        objectCompletableFuture.whenComplete(callback);
    }

    /**
     * Sends a command asynchronously, invokes a callback when the result is available, and waits for the result.
     *
     * @param command the command to be sent
     * @param callback the callback to be invoked when the command is processed
     * @param <R> the type of the command result
     * @return the result of the command
     * @throws ExecutionException if the command execution encounters an error
     * @throws InterruptedException if the current thread is interrupted while waiting for the result
     */
    @Override
    public <R> R sendWithCallBackAndWait(Object command, BiConsumer<? super R, ? super Throwable> callback) throws ExecutionException, InterruptedException {
        final CompletableFuture<R> objectCompletableFuture = gate.dispatchAsync(command);
        return objectCompletableFuture.whenComplete(callback).get();
    }
    /**
     * Sends a command asynchronously and returns a future representing the result.
     *
     * @param command the command to be sent
     * @param expectedType the expected type of the result
     * @param <R> the type of the command result
     * @return a future representing the result of the command
     */

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
