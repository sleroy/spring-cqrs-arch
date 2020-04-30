# Changelog

* Release *1.1.2*: Fixed wrong Documentation of EventThrower interface. Now it is also easier to implement Command Throttling.

* Release *1.0.6*: Added a new feature allowing to filter (and preprocess) commands before sending them to the CommandExecutor.
* Release *1.0.5*: Fix bug in insertBefore() in the Workflow Customizer bean.
* Release *1.0.4*: Upgrade to Guava 26.0
* Release *1.0.3*: Reworked the EventBusService method for easier mocking.
* Release *1.0.2*:

Fixed bean scopes to force singleton scopes.
Better exception message for failed command validation.
Fixed a bug in the Guava Event bus service.
Added a new interface to throw events when a command is successful or failed.

```
public interface EventThrower {

    /**
     * Event on failure.
     *
     * @return the event that should be thrown (null does not send event)
     */
    Object eventOnFailure(Throwable failure);

    /**
     * Event on success.
     *
     * @param result
     *            the result
     * @return the event that should be thrown (null does not send event)
     */
    Object eventOnSuccess(Object result);
}
```

Added a new annotation to return an event rather than a value in a CommandServiceSpec

```
@CommandService
@ReturnEventOnSuccess
public class NotifyCrawlStatusCommandHandler
	implements CommandServiceSpec<NotifyCrawlStatusCommand, EventCrawlStatusUpdated> {
```

Allow trace configuration override.

* Release *1.0.1*: Fixed a bug in the insertAfter step of a Workflow

* Release *1.0.0* : brand new concept to execute a commmand using a so-called Workflow. 

* Release *0.8.0* : refactor the command execution listener to allow storing informations in the command runner.

* Release *0.7.0* : Better integration with Spring Boot

* 0.7.0 : New configuration system fully compatible with SpringBoot using the annotation @EnableCqrsModule

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

***
[www.byoskill.com](www.byoskill.com)
[www.sylvainleroy.com](Blog sylvainleroy.com)
