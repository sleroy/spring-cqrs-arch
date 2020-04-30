## Exceptions 

The exceptions are well-defined in the module.

Here is the list of Exceptions that may be thrown by the CQRS Gate :

* `CommandExecutionException.java` : When the execution has failed, the default exception handler is wrapping the checked exception in this exception.
* `CommandHandlerNotFoundException.java` : When a command is sent through the gate but no handler has been found.
* `CqrsException.java` : Base exception class
* `InvalidCommandException.java` : Commands are validated using the JEE Api validation. If an invalid command hs been sent to the Gate, the Exception will be returned. The exception encloses a ConstraintsViolationException with the list of invalid properties.

The gate is provided with a default Exception handler with the following  behaviour :

* Any caught exception is wrapped inside a CommandExecutionException.

The behaviour can be changed using your own ICommandExceptionHandler.

## Listener on command execution

It is possible to implement one or more listeners to trap the execution of a command through the gate.

To do so, simply implement a new Spring bean implementing the interface ICommandExecutionListener.

```
@Bean
public class DemoBean implements ICommandExecutionListener {

}
```
