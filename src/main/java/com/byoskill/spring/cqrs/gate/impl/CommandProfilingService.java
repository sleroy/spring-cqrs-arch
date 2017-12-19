package com.byoskill.spring.cqrs.gate.impl;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.byoskill.spring.cqrs.api.ICommandCallback;
import com.byoskill.spring.cqrs.api.ICommandProfilingService;

@Service
public class CommandProfilingService implements ICommandProfilingService {

    public static final class ProfilingCallBack<R> implements ICommandCallback<R> {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandProfilingService.class);

	private final ICommandCallback<R> callback;
	private final Object		  command;
	private final StopWatch		  stopWatch;

	public ProfilingCallBack(final Object _command, final ICommandCallback<R> _callback) {
	    command = _command;
	    callback = _callback;
	    stopWatch = new StopWatch();
	    stopWatch.start();

	}

	@Override
	public R call() throws Exception {
	    try {
		return callback.call();
	    } finally {
		stopWatch.stop();
		LOGGER.info("[PROFILING][{}]={} ms", command, stopWatch.getTime());
	    }
	}

    }

    @Override
    public <R> ICommandCallback<R> decorate(final Object _command, final ICommandCallback<R> _callback) {

	return new ProfilingCallBack<>(_command, _callback);
    }

}
