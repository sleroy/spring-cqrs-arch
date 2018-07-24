/*
 * Copyright (C) 2017 Sylvain Leroy - BYOSkill Company All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the MIT license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the MIT license with
 * this file. If not, please write to: sleroy at byoskill.com, or visit : www.byoskill.com
 *
 */
package com.byoskill.spring.cqrs.gate.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;

import com.byoskill.spring.cqrs.annotations.EventHandler;
import com.byoskill.spring.cqrs.api.CommandExecutionContext;
import com.byoskill.spring.cqrs.api.CommandExecutionListener;
import com.google.common.eventbus.Subscribe;

/**
 * The Class CqrsDebugger defines the component that observes any notifications
 * from the Event bus and the Gate.
 */

@EventHandler
@Profile({ "debug_cqrs" })
public class CqrsDebugger implements CommandExecutionListener {

    /**
     * The Class CommandResult.
     */
    private static class CommandResult {

	/** The cause. */
	public Throwable cause;

	/** The command. */
	public Object command;

	/** The result. */
	public Object result;

	/**
	 * Instantiates a new command result.
	 *
	 * @param _command
	 *            the command
	 * @param result
	 *            the result
	 */
	public CommandResult(final Object _command, final Object result) {
	    command = _command;
	    this.result = result;
	}

	/**
	 * Instantiates a new command result.
	 *
	 * @param _command
	 *            the command
	 * @param _cause
	 *            the cause
	 */
	public CommandResult(final Object _command, final Throwable _cause) {
	    command = _command;
	    cause = _cause;
	}

	/**
	 * Gets the cause.
	 *
	 * @return the cause
	 */
	public Throwable getCause() {
	    return cause;
	}

	/**
	 * Gets the command.
	 *
	 * @return the command
	 */
	public Object getCommand() {
	    return command;
	}

	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	public Object getResult() {
	    return result;
	}

	/**
	 * Checks if is command of.
	 *
	 * @param _command
	 *            the command
	 * @return true, if is command of
	 */
	public boolean isCommandOf(final Class<?> _command) {
	    return _command != null && _command.isInstance(command);
	}

	/**
	 * Sets the cause.
	 *
	 * @param cause
	 *            the cause to set
	 */
	public void setCause(final Throwable cause) {
	    this.cause = cause;
	}

	/**
	 * Sets the command.
	 *
	 * @param command
	 *            the command to set
	 */
	public void setCommand(final Object command) {
	    this.command = command;
	}

	/**
	 * Sets the result.
	 *
	 * @param result
	 *            the result to set
	 */
	public void setResult(final Object result) {
	    this.result = result;
	}
    }

    /** The commands. */
    private final List<CommandResult> commands = new ArrayList<>();

    private final List<Object> events = new ArrayList<>();

    @Override
    public void beginExecution(final CommandExecutionContext context) {
	//

    }

    /**
     * Clear all the informations.
     */
    public void clearAll() {

	clearCommands();
	clearEvents();
    }

    /**
     * Clear commands.
     */
    public void clearCommands() {
	commands.clear();
    }

    /**
     * Clear events.
     */
    public void clearEvents() {
	events.clear();
    }

    /**
     * Count commands.
     *
     * @param class1
     *            the class 1
     * @return the long
     */
    public long countCommands(final Class<?> class1) {

	return commands.stream().filter(it -> it.isCommandOf(class1)).count();
    }

    /**
     * Count events.
     *
     * @param eventClass
     *            the class of the event
     * @return the long
     */
    public long countEvents(final Class<?> eventClass) {

	return events.stream().filter(it -> eventClass.isInstance(it)).count();
    }

    /**
     * Count events.
     *
     * @param eventClass
     *            the class of the event
     * @return the long
     */
    public List<Object> filterEvents(final Class<?> eventClass) {

	return events.stream().filter(it -> eventClass.isInstance(it)).collect(Collectors.toList());
    }

    /**
     * Gets the size.
     *
     * @return the size
     */
    public int getSize() {

	return commands.size();
    }

    @Override
    public void onFailure(final CommandExecutionContext context, final Throwable cause) {
	commands.add(new CommandResult(context.getRawCommand(), cause));

    }

    @Override
    public void onSuccess(final CommandExecutionContext context, final Object result) {
	commands.add(new CommandResult(context.getRawCommand(), result));

    }

    @Subscribe
    public void suscribeEventListener(final Object event) {
	events.add(event);
    }
}
