package com.byoskill.spring.cqrs.commandgateway;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Interface towards the Command Handling components of an application. This interface provides a friendlier API toward the command bus. The CommandGateway
 * allows for components dispatching commands to wait for the result.
 */
public interface CommandGateway {

    /**
     * Sends the given command and returns immediately, without waiting for the command to execute.
     *
     * @param command command
     */
    void send(Object command);

    /**
     * Sends the given command, and have the result of the command's execution reported to the given callback.
     *
     * @param command  command
     * @param callback the callback
     */
    void sendAndConsume(Object command, Consumer<?> callback);

    /**
     * Sends the given command and executes the call back if there was no exception.
     *
     * @param <R>      the returned type;
     * @param <U>      the processed type;
     * @param command  the command
     * @param callback the call back
     */
    <R, U> U sendAndApply(Object command, Function<R, U> callback);

    /**
     * Sends the given command and executes the call back if there was no exception.
     *
     * @param <R>      the returned type;
     * @param command  the command
     * @param callback the call back
     */
    <R> void sendWithCallBack(Object command, BiConsumer<? super R, ? super Throwable> callback);

    /**
     * Sends the given command and executes the call back if there was no exception.
     *
     * @param <R>      the returned type;
     * @param command  the command
     * @param callback the call back
     * @return the returned value from the callback
     */
    <R> R sendWithCallBackAndWait(Object command, BiConsumer<? super R, ? super Throwable> callback) throws ExecutionException, InterruptedException;

    /**
     * Sends the given command and executes the call back if there was no exception.
     *
     * @param <R>          the returned type;
     * @param command      the command
     * @param expectedType the expected type
     * @return the returned value from the callback
     */
    <R> CompletableFuture<R> send(Object command, Class<R> expectedType);

    /**
     * Sends the given command and wait for it to execute.
     *
     * @param command the command
     * @param <R>     the type of returned value
     * @return the returned value.
     */
    <R> R sendAndWait(Object command);

    /**
     * Sends the given command and wait for it to execute.
     *
     * @param command the command
     * @param timeout the timeout
     * @param unit    the unit
     * @param <R>     the type of returned value
     * @return the returned value.
     */
    <R> R sendAndWait(Object command, long timeout, TimeUnit unit);

    /**
     * Send a named command asynchronously without expecting result.
     *
     * @param commandName the command name
     */
    void sendNamedCommand(String commandName);

    /**
     * Send a named command and wait for its result.
     *
     * @param commandName the command name
     * @return the returned value from the callback
     */
    <R> R sendAndWaitNamedCommand(String commandName);

}
