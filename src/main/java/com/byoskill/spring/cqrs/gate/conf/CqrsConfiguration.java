/**
 * Copyright (C) 2017 Sylvain Leroy - BYOS Company All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the MIT license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the MIT license with
 * this file. If not, please write to: contact@sylvainleroy.com, or visit : https://sylvainleroy.com
 */
package com.byoskill.spring.cqrs.gate.conf;

import java.io.File;

/**
 * Configuration of the CQRS Module
 *
 * @author sleroy
 *
 */

public class CqrsConfiguration {

    /** The async event queries. */
    private boolean asyncEventQueries = false;

    /** The core pool size. */
    private int	    corePoolSize      = 2;


    /** The history capacity. */
    private int	    historyCapacity  = 3;

    /** The keep alive seconds. */
    private int	    keepAliveSeconds = 60;

    /** The logging enabled. */
    private boolean loggingEnabled   = true;

    /** The max pool size. */
    private int	    maxPoolSize	     = 10;

    /** The profiling enabled. */
    private boolean profilingEnabled = true;

    /** The queue capacity. */
    private int	    queueCapacity    = 25;

    /** The trace file. */
    private File    traceFile	     = new File("command.trace");

    private int traceSize = 100;

    /** The tracing enabled. */
    private boolean tracingEnabled   = false;

    public boolean getAllowCoreTimeout() {
	return false;
    }

    public int getAwaitTerminationSeconds() {
	return 0;
    }

    /**
     * Gets the core pool size.
     *
     * @return the core pool size
     */
    public int getCorePoolSize() {

	return corePoolSize;
    }

    /**
     * Gets the history capacity.
     *
     * @return the history capacity
     */
    public int getHistoryCapacity() {
	return historyCapacity;
    }

    /**
     * Gets the keep alive seconds.
     *
     * @return the keep alive seconds
     */
    public int getKeepAliveSeconds() {
	return keepAliveSeconds;
    }

    /**
     * Gets the max pool size.
     *
     * @return the max pool size
     */
    public int getMaxPoolSize() {
	return maxPoolSize;
    }


    public int getParallelism() {
	return 8;
    }

    /**
     * Gets the queue capacity.
     *
     * @return the queue capacity
     */
    public int getQueueCapacity() {
	return queueCapacity;
    }

    public int getThreadPriority() {

	return Thread.NORM_PRIORITY;
    }

    /**
     * Gets the trace file.
     *
     * @return the trace file
     */
    public File getTraceFile() {
	return traceFile;
    }

    public int getTraceSize() {
	return traceSize;
    }


    /**
     * Checks if is async event queries.
     *
     * @return the asyncEventQueries
     */
    public boolean isAsyncEventQueries() {
	return asyncEventQueries;
    }

    public boolean isAwaitingOnShutdown() {
	return false;
    }

    public boolean isDaemon() {
	return false;
    }

    /**
     * Checks if is logging enabled.
     *
     * @return true, if is logging enabled
     */
    public boolean isLoggingEnabled() {
	return loggingEnabled;
    }

    /**
     * Checks if is profiling enabled.
     *
     * @return true, if is profiling enabled
     */
    public boolean isProfilingEnabled() {
	return profilingEnabled;
    }

    /**
     * Checks if is tracing enabled.
     *
     * @return true, if is tracing enabled
     */
    public boolean isTracingEnabled() {
	return tracingEnabled;
    }


    /**
     * Sets the async event queries.
     *
     * @param asyncEventQueries            the asyncEventQueries to set
     */
    public void setAsyncEventQueries(final boolean asyncEventQueries) {
	this.asyncEventQueries = asyncEventQueries;
    }

    /**
     * Sets the core pool size.
     *
     * @param corePoolSize the new core pool size
     */
    public void setCorePoolSize(final int corePoolSize) {
	this.corePoolSize = corePoolSize;
    }

    /**
     * Sets the history capacity.
     *
     * @param _historyCapacity the new history capacity
     */
    public void setHistoryCapacity(final int _historyCapacity) {
	historyCapacity = _historyCapacity;
    }

    /**
     * Sets the keep alive seconds.
     *
     * @param keepAliveSeconds the new keep alive seconds
     */
    public void setKeepAliveSeconds(final int keepAliveSeconds) {
	this.keepAliveSeconds = keepAliveSeconds;
    }

    /**
     * Sets the logging enabled.
     *
     * @param _loggingEnabled the new logging enabled
     */
    public void setLoggingEnabled(final boolean _loggingEnabled) {
	loggingEnabled = _loggingEnabled;
    }

    /**
     * Sets the max pool size.
     *
     * @param _maxPoolSize the new max pool size
     */
    public void setMaxPoolSize(final int _maxPoolSize) {
	maxPoolSize = _maxPoolSize;
    }

    /**
     * Sets the profiling enabled.
     *
     * @param _profilingEnabled the new profiling enabled
     */
    public void setProfilingEnabled(final boolean _profilingEnabled) {
	profilingEnabled = _profilingEnabled;
    }

    /**
     * Sets the queue capacity.
     *
     * @param _queueCapacity the new queue capacity
     */
    public void setQueueCapacity(final int _queueCapacity) {
	queueCapacity = _queueCapacity;
    }

    /**
     * Sets the trace file.
     *
     * @param _logFile the new trace file
     */
    public void setTraceFile(final File _logFile) {
	traceFile = _logFile;
    }

    public void setTraceSize(final int traceSize) {
	this.traceSize = traceSize;
    }

    /**
     * Sets the tracing enabled.
     *
     * @param _logCommands the new tracing enabled
     */
    public void setTracingEnabled(final boolean _logCommands) {
	tracingEnabled = _logCommands;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "CqrsConfiguration [loggingEnabled=" + loggingEnabled + ", profilingEnabled=" + profilingEnabled
		+ ", historyCapacity=" + historyCapacity + ", corePoolSize=" + corePoolSize
		+ ", maxPoolSize=" + maxPoolSize + ", queueCapacity=" + queueCapacity + ", logCommands="
		+ tracingEnabled + ", logFile=" + traceFile + "]";
    }

}
