package com.byoskill.spring.cqrs.api;

import java.util.concurrent.Callable;

/**
 * This interface describes a wrapper over a command. The execution of the
 * command is done with this interface.
 * 
 * @author sleroy
 *        
 * @param <R>
 */
public interface ICommandCallback<R> extends Callable<R> {
	//
}
