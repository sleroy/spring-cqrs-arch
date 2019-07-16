package com.byoskill.spring.cqrs.commandgateway;

import com.byoskill.spring.cqrs.callbacks.CommandCallback;

import java.util.concurrent.TimeUnit;

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
     * @param <R> the type of returned value
     */
    <R> void send(Object command, CommandCallback<R> callback);

    /**
     * Sends the given command and wait for it to execute.
     *
     * @param command the command
     * @param <R> the type of returned value
     *
     * @return the returned value.
     */
    <R> R sendAndWait(Object command);

    /**
     * Sends the given command and wait for it to execute.
     *
     * @param command the command
     * @param timeout the timeout
     * @param unit    the unit
     * @param <R> the type of returned value
     *
     * @return the returned value.
     */
    <R> R sendAndWait(Object command, long timeout, TimeUnit unit);

}
