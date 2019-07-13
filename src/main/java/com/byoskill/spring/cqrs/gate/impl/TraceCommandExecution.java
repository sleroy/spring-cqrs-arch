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
 * The Class TraceCommandExecution contains the context to be executed. Mainly the
 * command and the result of the execution.
 */
public class TraceCommandExecution {

    private Object command;
    private ExecutionStatus executionStatus;
    private Throwable reason;
    private Object result;

    public TraceCommandExecution() {
        super();
    }

    public static TraceCommandExecution failure(final Object command, final Throwable executionResult) {

        final TraceCommandExecution traceCommandExecution = new TraceCommandExecution();
        traceCommandExecution.command = command;
        traceCommandExecution.result = executionResult;
        traceCommandExecution.executionStatus = ExecutionStatus.FAILURE;
        return traceCommandExecution;
    }

    public static TraceCommandExecution success(final Object command, final Object executionResult) {

        final TraceCommandExecution traceCommandExecution = new TraceCommandExecution();
        traceCommandExecution.command = command;
        traceCommandExecution.result = executionResult;
        traceCommandExecution.executionStatus = ExecutionStatus.SUCCESS;
        return traceCommandExecution;
    }

    public Object getCommand() {
        return command;
    }

    public void setCommand(final Object command) {
        this.command = command;
    }

    public ExecutionStatus getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(final ExecutionStatus executionStatus) {
        this.executionStatus = executionStatus;
    }

    public Throwable getReason() {
        return reason;
    }

    public void setReason(final Throwable reason) {
        this.reason = reason;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(final Object result) {
        this.result = result;
    }

}
