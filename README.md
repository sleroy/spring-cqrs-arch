# spring-cqrs-arch

[ ![Download](https://api.bintray.com/packages/sleroy/maven/spring-cqrs-module/images/download.svg) ](https://bintray.com/sleroy/maven/spring-cqrs-module/_latestVersion)

[![Coverage Status](https://coveralls.io/repos/github/sleroy/spring-cqrs-arch/badge.svg?branch=master)](https://coveralls.io/github/sleroy/spring-cqrs-arch?branch=master)

[![Build Status](https://travis-ci.org/sleroy/spring-cqrs-arch.svg?branch=master)](https://travis-ci.org/sleroy/spring-cqrs-arch)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/199a12aa6e404478b78b648688fa894c)](https://www.codacy.com/app/sleroy/spring-cqrs-arch?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=sleroy/spring-cqrs-arch&amp;utm_campaign=Badge_Grade)

# Changelog

* 0.6.2 : Second Refactoring adding a grace period of 30 seconds before brute-force closing.

* 0.6.1 : Refactoring to fix a long closing issue with CQRS Command Executor

* 0.6.0 : 

- Added Throttling on Commands if necessary using @Throttle annotation
- Added its own ThreadPoolExecutor
- Refactored the Command execution to use less promises (and avoid thread exhaustion on java 8)

* 0.5.0 : Fixing important bugs
  
* 0.4.2 : Fixing bug.

* 0.4.1 : Now the Object validator is compatible with Spring custom validators.

* 0.4.0 : Added a new method to dispatch all commands

* 0.3.1 : Fixed exception message for constraint violations.

* 0.3.0 : Rewrote the engine to be completely asynchronous using CompletableFuture.

* 0.2.4 :

- Upgrade of the Gate to specify the expected return type.

* 0.2.3 :

- Added unit tests for spring gate promises
- Better logging with onFailure (added stacktraces)  

* 0.2.2 :

- Bug fix with dependency scanning
- Better exception management for constraint violations

# Purpose

This module is an implementation of a software architecture model inspired from the CQRS ( [link from Fowler](https://martinfowler.com/bliki/CQRS.html) ) model.

![CQRS Architecture](https://martinfowler.com/bliki/images/cqrs/cqrs.png)

The idea is the following : the software functionalities are split between the read and the write accesses made by the user.

## Advantages

The strongest advantages of this architecture are - according my experience - :

* **Evolutive architecture** : this sofware architecture model enforces several OOP architecture principles (design patterns, transverse functionalities)
* **Easy testing** : the module and the architecture allows the developer to do unit-testing or component-based testing of its code. Furthermore this implementation is also providing out of the box : logging, profiling and tracing (file export) to push the debugging further
* **Spring compatible** : this architecture is compatible with IOC, Java and Spring
* **Microservice compatible** : this module can be used to implement a monolith and split it as microservices later, or directly as microservices if you implement a Bus.

## Cleancode

Some bad smells often found in Spring / Java 
Web applications are avoided with this model.

By instance : 
* **God / Mother classes** : Some years ago, it was quite common to find Java classes also known as Services containing quite a bunch of methods, a big grape of autowired services and poorly tested. These classes were highly fragile, hard to mock and test. By creating a class per command and therefore by usecase, the java classes are fine-grained,easier to mock and test.
* **Excessive parameters** The developer has to encapsulate the data into an Command object to pass it through the gate. This way, the OOP is enforced into the Core of this model.

Implementation model for an web application using CQRS principles.

## Scope

This module is offering basically the Command architecture principles, and a way to send events.

For the query part, I recommend a progressive approach :

- Use the same database and the same database access technology (with ORM)
- Use another database access technology (without ORM or NoSQL)

![Module architecture design](schema.png)

# How to use

## Configuration

The module requires to define a Bean to start it (*CqrsConfiguration*).

The easiest way is to create a Configuration bean and to declare the configuration inside :

```
@Configuration
public class CqrsModuleConfiguration {
 public CqrsConfiguration configure() {
	 	CqrsConfiguration configuration = new CqrsConfiguration();
		// Configure there
		return configuration;
 }
}
```
## How to begin

To conceive an application using CQRS, you need to think that way :

* **What are my use cases** : define your use-cases expressed as operation s(READ or Write) or eventually a sequence flow diagram per use-case
* **Distinguish your write access** : for example create a new user, edit his phone number. Avoid as much as possible generic and poor business meaning operations as CRUD (Create, Update, Delete). Think about what are you trying to update ? His personal details ? Are you toggling the email configuration flag ? Obviously all the se operations are a write and could be written as a big Update method. Cqrs is enforcing the DDD approach. Meaning is a rule to write better application toward functional design rather than technical design and to increase the productivity.
* Start by writing your commands : that is the easiest part :

### Write a command

A command is a basic Pojo :

```
public class CreateUserCommand {
	public String email;
	public String password;
}
```

*Every* command is tested using the Validation API and usually the Hibernate validator API.

```
public class CreateUserCommand {
	@NonEmpty
	@Email
	public String email;
	@NonEmpty
	@Size(minimum=8)
	public String password;
}
```

It means the gate will validate the command before executing them.

If you pay attention, you should be able to convert your request body or payload directly as a Command.

### Send the command

To send a command, you need to send the object through the gate.

To do so, inject the Gate dependency with @Autowired

```
@RestController
public class MyController {
		@Autowired
		private Gate gate;
}
```

You have two possibilities to send a command :

* a synchronous way : `gate.dispatch(command);`
* an asynchronous way : `gate.dispatchAsync(command);`

The methods are returning the results of the command execution.

### Write a command handler

To write a command handler, you need to implement a Spring bean defining the interface *ICommandHandler*.

The interface *ICommandHandler* is taking two type parameters :
* the first one is the command handled : exemple CreateNewCommand
* the second is the returned type of the command handler: the result produced by the execution of the command.

example : 


```
@CommandHandler
public class CreateNewUserCommandHandler implements ICommandHandler<CreateNewUserCommand, Integer> {
@Override
	public Integer handle(final CreateNwUserCommand command) throws Exception {

	    return 1;
	}
}
```

We also recommend to send events to notify the change in the repository. The gate is offering such proxy. The default event bus is in Guava but can be implemented using the Spring applicationEvent or more complex implementations.


```
@CommandHandler
public class CreateNewUserCommandHandler implements ICommandHandler<CreateNewUserCommand, Integer> {

@Autowired
private Gate gate;

@Override
	public Integer handle(final CreateNewUserCommand command) throws Exception {
			gate.dispatchEvent(new EventNewUserCreated());
	    return 1;
	}
}
```

## Spring Profiles

Here are the following profiles to enable features in the module :
```
@Profile("guava_bus")
```

## Exception Handling

Here is the list of Exception that may be thrown by the CQRS Gate :

* `CommandExecutionException.java` : When the execution has failed, the default exception handler is wrapping the checked exception in this exception.
* `CommandHandlerNotFoundException.java` : When a command is sent through the gate but no handler has been found.
* `CqrsException.java` : Base exception class
* `InvalidCommandException.java` : Commands are validated using the JEE Api validation. If an invalid command hs been sent to the Gate, the Exception will be returned. The exception encloses a ConstraintsViolationException with the list of invalid properties.

The gate is provided with a default Exception handler with the following  behaviour :

* Any caught exception is wrapped inside a CommandExecutionException.

The behaviour can be changed using your own ICommandExceptionHandler.

## Listener on command execution

It is possible to implement a listener (or many) to trap the execution of a command through the gate.

To do so, simply implement a new Spring bean implementing the interface ICommandExecutionListener.

```
@Bean
public class DemoBean implements ICommandExecutionListener {

}
```

## Transactions

Transaction can be implemented in two ways :

* Creating an ICommandExecutionListener and trigger the transaction as a wrapper over 
  the execution of a transaction
* Directly on the CommandHandler by adding the appropriate @Transaction

# Throttling

Commands can be decorated with an annotationn @Throttle. This annotation takes a parameter, the name of the Rate limiter. Therefore you can defined named policy that limits the number of command executed for a given period of time.

```
@Throttle(value="CRM_API")
public class CreateUserCommand {
}
```

To implement the rate limiter functionality, you have to provide a Bean, configured with Spring.

Here is a basic implementation using Guava RateLimiter : 

```
@Service
public class ThrottlingService implements IThrottlingInterface {

    private final RateLimiter THROTTLER = RateLimiter.create(5.0);

    @Override
    public void acquirePermit(final String name) {
	if (name.equals("CRM_API")) {
	    THROTTLER.acquire();
	}

    }
}
```



## References

* [Clarified CQRS](http://udidahan.com/2009/12/09/clarified-cqrs/)
* [Mailing list](https://groups.google.com/forum/#!forum/dddcqrs)
* [Article from M. Fowler](https://martinfowler.com/bliki/CQRS.html)
* [CQRS Article]()  
