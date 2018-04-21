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

/**
 * The Class CommandExecution contains the context to be executed. Mainly the
 * command and the result of the execution.
 */
public class CommandExecution {

    public static CommandExecution failure(final Object command, final Throwable executionResult) {

	final CommandExecution commandExecution = new CommandExecution();
	commandExecution.command = command;
	commandExecution.result = executionResult;
	commandExecution.executionStatus = ExecutionStatus.SUCCESS;
	return commandExecution;
    }

    public static CommandExecution success(final Object command, final Object executionResult) {

	final CommandExecution commandExecution = new CommandExecution();
	commandExecution.command = command;
	commandExecution.result = executionResult;
	commandExecution.executionStatus = ExecutionStatus.SUCCESS;
	return commandExecution;
    }

    private Object command;

    private ExecutionStatus executionStatus;

    private Throwable reason;

    private Object result;

    public CommandExecution() {
	super();
    }

    public Object getCommand() {
	return command;
    }

    public ExecutionStatus getExecutionStatus() {
	return executionStatus;
    }

    public Throwable getReason() {
	return reason;
    }

    public Object getResult() {
	return result;
    }

    public void setCommand(final Object command) {
	this.command = command;
    }

    public void setExecutionStatus(final ExecutionStatus executionStatus) {
	this.executionStatus = executionStatus;
    }

    public void setReason(final Throwable reason) {
	this.reason = reason;
    }

    public void setResult(final Object result) {
	this.result = result;
    }

}
