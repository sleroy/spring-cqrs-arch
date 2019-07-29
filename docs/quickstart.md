# Quickstart

## Bootstrap project

*NEW* : I am maintaining a small bootstrap project using Spring Boot to illustrate a generic configuration.

The project is there : https://github.com/sleroy/spring-cqrs-arch-bootstrap

## Step by step instructions

To use the framework : 

* Add the library to your classpath using maven or gradle.

If you are not declaring Guava in your own project : 

```
<dependency>
	<groupId>com.byoskill.spring.cqrs</groupId>
	<artifactId>spring-cqrs-module</artifactId>
	<version>${lib.cqrs.version}</version>
</dependency>
```

Otherwise exclude the bundled version to use your own :

```
<dependency>
	<groupId>com.byoskill.spring.cqrs</groupId>
	<artifactId>spring-cqrs-module</artifactId>
	<version>${lib.cqrs.version}</version>
	<exclusions>
		<exclusion>
			<groupId>com.google.guava</groupId>
			<artifactId>guava</artifactId>
		</exclusion>
	</exclusions>
</dependency>
```

### Configuration using Spring BOOT

* If you are using Spring boot, add the following code to your application :

```
@EnableCqrsModule
```

It will provide a default configuration. 

## Configuration

Each configuration class can be overloaded to change the CQRS module behaviour : 

```
   Class<?>[] customConfiguration() default { ImportDefaultCqrsConfiguration.class, ImportGuavaAsyncEventBusConfiguration.class,
	    ImportCommandServiceScanningConfiguration.class };
```


### ImportDefaultCqrsConfiguration

This code is declaring the default configuration for CQRS :

* `ImportDefaultCqrsConfiguration` this configuration class defines differents aspects of the CQRS module : 

  * The logging step : how each executed command will be logged
  * The tracing step : whether each command should be traced and stored on disk to produce integration tests
  * The throttling interface : if you need to throttle some commands like API Calls
  * The Fork join pool : use the default or a specific thread pool since the commands are executed asynchronously
  * `UncaughtExceptionHandler`: the exception handler to perform specific behaviour when a command is failing or throwing an exception.

### ImportGuavaAsyncEventBusConfiguration

Our CQRS Module allows the command services to throw events. By default, we are using a simple Guava EventBus to receive the events, propagate them and help the developer to define event handlers.

However production environments would most likely rely on AMQP, MQ Buses to propagate the events to other services. If this functionality is interesting you, I recommend you to overload this configuration.

### ImportCommandServiceScanningConfiguration

This configuration class allow you to define a command post processor after the service execution.

***
[www.byoskill.com](www.byoskill.com)

[Blog : www.sylvainleroy.com](www.sylvainleroy.com)
