package com.byoskill.spring.cqrs.gate.impl;

import org.junit.Test;

import com.byoskill.spring.cqrs.api.ICommandCallback;
import com.byoskill.spring.cqrs.gate.impl.CommandProfilingService.ProfilingCallBack;

public class ProfilingCallBackTest {
	
	@Test(expected = RuntimeException.class)
	
	public void onFailureWithException() throws Exception {
		final ProfilingCallBack<String> callBack = new ProfilingCallBack<String>("", new ICommandCallback<String>() {
			
			@Override
			public String call() throws Exception {
				throw new RuntimeException();
			}

		});
		
		callBack.call();
	}
	
	@Test
	public void onSuccessNoException() throws Exception {
		final ProfilingCallBack<String> callBack = new ProfilingCallBack<String>("", new ICommandCallback<String>() {
			
			@Override
			public String call() throws Exception {
				return null;
			}
			
		});
		
		callBack.call();
	}
}
