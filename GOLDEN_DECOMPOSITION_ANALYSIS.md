# Spring CQRS Architecture - Golden Decomposition Scenario

## Problem Context
This is the **spring-cqrs-arch** library itself (not an application). It is a framework implementing the CQRS pattern for Spring/Spring Boot applications. The framework separates **Command side (write/state-change)** from **Query side (read-only)**.

The library packages classes into:
- **Framework infrastructure** (command gateway, workflow, interceptors, event bus)
- **Test fixtures** (fakeapp: command handlers, commands, domain DTOs)

For a strangler-fig decomposition engine validation, we extract a "golden" CQRS clustering that would result if this framework were itself decomposed.

---

## (a) ROLE → CLASSES TABLE

### COMMAND SIDE (Write Model / State-Change)

| Role | Fully-Qualified Class | Package |
|------|----------------------|---------|
| Command DTOs | `com.byoskill.spring.cqrs.fakeapp.commands.RandomNumberCommand` | `fakeapp.commands` |
| Command DTOs | `com.byoskill.spring.cqrs.fakeapp.commands.MethodWithoutReturnCommand` | `fakeapp.commands` |
| Command DTOs | `com.byoskill.spring.cqrs.fakeapp.commands.MethodWithReturnCommand` | `fakeapp.commands` |
| Command DTOs | `com.byoskill.spring.cqrs.fakeapp.commands.DummyObjectCommand` | `fakeapp.commands` |
| Command DTOs | `com.byoskill.spring.cqrs.fakeapp.commands.RandomErrorNumberCommand` | `fakeapp.commands` |
| **Command Handlers (impl)** | `com.byoskill.spring.cqrs.fakeapp.RandomNumberHandler` | `fakeapp` |
| **Command Handlers (impl)** | `com.byoskill.spring.cqrs.fakeapp.RandomErrorNumberHandler` | `fakeapp` |
| **Command Handlers (impl)** | `com.byoskill.spring.cqrs.fakeapp.DummyObjectCommandHandler` | `fakeapp` |
| **Command Handlers (impl)** | `com.byoskill.spring.cqrs.fakeapp.BrokenObjectCommandHandler` | `fakeapp` |
| **Command Handlers (@Service)** | `com.byoskill.spring.cqrs.fakeapp.CommandServiceWithHandlerMethods` | `fakeapp` |
| **Command Handler Interface** | `com.byoskill.spring.cqrs.commands.CommandServiceSpec<C,R>` | `commands` |
| **Gateway (entry point)** | `com.byoskill.spring.cqrs.commandgateway.Gate` | `commandgateway` |
| **Gateway Impl** | `com.byoskill.spring.cqrs.commandgateway.SpringGate` | `commandgateway` |
| **Command Executor** | `com.byoskill.spring.cqrs.workflow.CommandExecutorServiceImpl` | `workflow` |
| **Service Provider (routing)** | `com.byoskill.spring.cqrs.commandservices.CommandServiceProvider` | `commandservices` |
| **Service Provider Impl** | `com.byoskill.spring.cqrs.commandservices.SpringCommandServiceProvider` | `commandservices` |

### QUERY SIDE (Read Model / Read-Only)

| Role | Fully-Qualified Class | Package |
|------|----------------------|---------|
| **Query Handler Annotation** | `com.byoskill.spring.cqrs.annotations.QueryHandler` | `annotations` |
| *(No concrete query handlers in test fixtures; framework provides annotation only)* | — | — |

**KEY OBSERVATION:** The framework defines the `@QueryHandler` annotation but provides no concrete implementations. CQRS docs note: "For the query part, I recommend a progressive approach ... Use the same database and the same database access technology (with ORM)". Query side is left to users.

### SHARED DOMAIN / EVENTS / INFRASTRUCTURE

| Role | Fully-Qualified Class | Package |
|------|----------------------|---------|
| **Event Bus Service** | `com.byoskill.spring.cqrs.events.EventBusService` | `events` |
| **Event Bus Impl (Guava)** | `com.byoskill.spring.cqrs.events.guava.GuavaEventBusService` | `events.guava` |
| **Event Handler Annotation** | `com.byoskill.spring.cqrs.annotations.EventHandler` | `annotations` |
| **Interceptor Chain** | `com.byoskill.spring.cqrs.workflow.CommandRunnerChain` | `workflow` |
| **Interceptor (Logging)** | `com.byoskill.spring.cqrs.interceptors.CommandLoggingRunner` | `interceptors` |
| **Interceptor (Profiling)** | `com.byoskill.spring.cqrs.interceptors.CommandProfilingRunner` | `interceptors` |
| **Interceptor (Tracing)** | `com.byoskill.spring.cqrs.interceptors.CommandTraceRunner` | `interceptors` |
| **Interceptor (Event Throwing)** | `com.byoskill.spring.cqrs.interceptors.EventThrowerRunner` | `interceptors` |
| **Interceptor (Exception Handling)** | `com.byoskill.spring.cqrs.interceptors.DefaultExceptionHandlerRunner` | `interceptors` |
| **Context** | `com.byoskill.spring.cqrs.workflow.CommandExecutionContext` | `workflow` |
| **Context Impl** | `com.byoskill.spring.cqrs.workflow.CommandExecutionContextImpl` | `workflow` |
| **Configuration (CQRS setup)** | `com.byoskill.spring.cqrs.configuration.CqrsConfiguration` | `configuration` |
| **Configuration (Event Bus)** | `com.byoskill.spring.cqrs.configuration.EventBusConfiguration` | `configuration` |
| **Module Enabler** | `com.byoskill.spring.cqrs.annotations.EnableCqrsModule` | `annotations` |

---

## (b) PER-CLASS COMMAND-VS-QUERY METHOD CLASSIFICATION

### Command Handlers (implement CommandServiceSpec or @CommandHandler)

**Class:** `RandomNumberHandler`
**File:** `/home/sleroy/git/spring-cqrs-arch/src/test/java/com/byoskill/spring/cqrs/fakeapp/RandomNumberHandler.java`

```java
public class RandomNumberHandler implements CommandServiceSpec<RandomNumberCommand, Integer> {
  private final Random random;

  public Integer handle(final RandomNumberCommand command) {  // STATE_CHANGE (side effect: random generation)
    return random.nextInt();
  }
}
```

| Method | Classification | Rationale |
|--------|-----------------|-----------|
| `handle(RandomNumberCommand)` | **STATE_CHANGE** (side effect) | Generates pseudo-random state; changes application state via RNG. |

---

**Class:** `RandomErrorNumberHandler`
**File:** `/home/sleroy/git/spring-cqrs-arch/src/test/java/com/byoskill/spring/cqrs/fakeapp/RandomErrorNumberHandler.java`

```java
public class RandomErrorNumberHandler implements CommandServiceSpec<RandomErrorNumberCommand, Integer> {
  public Integer handle(final RandomErrorNumberCommand command) throws RuntimeException {
    // ...may throw; side effect
  }
}
```

| Method | Classification | Rationale |
|--------|-----------------|-----------|
| `handle(RandomErrorNumberCommand)` | **STATE_CHANGE** (error-path side effect) | Command that may fail; intentional error path. |

---

**Class:** `DummyObjectCommandHandler`
**File:** `/home/sleroy/git/spring-cqrs-arch/src/test/java/com/byoskill/spring/cqrs/fakeapp/DummyObjectCommandHandler.java`

```java
public class DummyObjectCommandHandler implements CommandServiceSpec<DummyObjectCommand, Integer> {
  public Integer handle(final DummyObjectCommand command) {
    // stub
  }
}
```

| Method | Classification | Rationale |
|--------|-----------------|-----------|
| `handle(DummyObjectCommand)` | **STATE_CHANGE** (placeholder) | Command handler; changes application state (intent). |

---

**Class:** `CommandServiceWithHandlerMethods`
**File:** `/home/sleroy/git/spring-cqrs-arch/src/test/java/com/byoskill/spring/cqrs/fakeapp/CommandServiceWithHandlerMethods.java`

```java
@CommandService
public class CommandServiceWithHandlerMethods {
  @CommandHandler
  public void handle(MethodWithoutReturnCommand command) { }  // STATE_CHANGE (void return)

  @CommandHandler
  public Object handle(MethodWithReturnCommand command) {     // STATE_CHANGE (has effect)
    return HAS_BEEN_INVOKED;
  }
}
```

| Method | Classification | Rationale |
|--------|-----------------|-----------|
| `handle(MethodWithoutReturnCommand)` | **STATE_CHANGE** | @CommandHandler marks write operation. Void return. |
| `handle(MethodWithReturnCommand)` | **STATE_CHANGE** | @CommandHandler marks write operation. Returns result. |

---

### Gateway & Dispatcher (Command Entry Points)

**Class:** `SpringGate`
**File:** `/home/sleroy/git/spring-cqrs-arch/src/main/java/com/byoskill/spring/cqrs/commandgateway/SpringGate.java`

```java
public class SpringGate implements Gate {
  public <R> R dispatch(final Object command) { ... }           // WRITE_DISPATCH
  public <R> R dispatch(final Object command, final Class<R> returnType) { ... }  // WRITE_DISPATCH
  public <R> CompletableFuture<R> dispatchAsync(final Object command) { ... }     // WRITE_DISPATCH
  public <R> CompletableFuture<R> dispatchAsync(final Object command, final Class<R> expectedReturnType) { ... } // WRITE_DISPATCH
  public <R> List<R> dispatchAll(final List<?> commands, final Class<R> expectedReturnType) { ... } // BATCH_WRITE_DISPATCH
  public void dispatchEvent(Object _event) { ... }             // EVENT_PUBLISH
  public void dispatchAsyncNamedCommand(final String commandName) { ... } // WRITE_DISPATCH (by name)
  public <R> R dispatchNamedCommand(final String commandName) { ... }     // WRITE_DISPATCH (by name)
  public <R> CompletableFuture<R> dispatchAsyncNamedCommandWithValue(final String commandName) { ... } // ASYNC_WRITE_DISPATCH (by name)
}
```

| Method | Classification | Rationale |
|--------|-----------------|-----------|
| All `dispatch*` methods | **COMMAND_DISPATCH** (write entry point) | Gateway methods route commands to handlers; writes. |
| `dispatchEvent()` | **EVENT_PUBLISH** | Publishes events after command execution. |

---

### Workflow & Execution

**Class:** `CommandExecutorServiceImpl`
**File:** `/home/sleroy/git/spring-cqrs-arch/src/main/java/com/byoskill/spring/cqrs/workflow/CommandExecutorServiceImpl.java`

```java
public class CommandExecutorServiceImpl {
  public <R> CompletableFuture<R> run(final Object command, final Class<R> expectedType) { ... }     // COMMAND_EXEC
  public <R> CompletableFuture<R> runNamedCommand(final String commandName) { ... }  // NAMED_COMMAND_EXEC
}
```

| Method | Classification | Rationale |
|--------|-----------------|-----------|
| `run()`, `runNamedCommand()` | **COMMAND_EXECUTION** | Executes command through interceptor chain. |

---

### Interceptor Chain (Middlewares)

**Class:** `EventThrowerRunner`
**File:** `/home/sleroy/git/spring-cqrs-arch/src/main/java/com/byoskill/spring/cqrs/interceptors/EventThrowerRunner.java`

```java
public class EventThrowerRunner implements CommandInterceptor {
  public Object execute(final CommandExecutionContext context, final CommandRunnerChain chain) {
    Object res = null;
    try {
      res = chain.execute(context);  // Forward to next; observe result
      if (handler != null) {
        Object event = handler.eventOnSuccess(res);  // DB_ACCESS (event factory)
        if (event != null) {
          eventBusService.publishEvent(event);       // EVENT_PUBLISH (read-only effect)
        }
      }
    } catch (final Exception t) {
      if (handler != null) {
        Object event = handler.eventOnFailure(t);    // ERROR_EVENT_FACTORY
        if (event != null) {
          eventBusService.publishEvent(event);       // EVENT_PUBLISH
        }
      }
      throw t;
    }
    return res;
  }
}
```

| Method | Classification | Rationale |
|--------|-----------------|-----------|
| `execute()` | **MIDDLEWARE** (chained side effect) | Post-command event publishing. Immutable read of result; event sent post-commit. |

---

**Class:** `CommandLoggingRunner`
**File:** `/home/sleroy/git/spring-cqrs-arch/src/main/java/com/byoskill/spring/cqrs/interceptors/CommandLoggingRunner.java`

```java
// (assumed structure from framework pattern)
public class CommandLoggingRunner implements CommandInterceptor {
  public Object execute(final CommandExecutionContext context, final CommandRunnerChain chain) {
    // Log command before execution (READ command state)
    // Forward to next interceptor
    // Log result after execution (READ result)
  }
}
```

| Method | Classification | Rationale |
|--------|-----------------|-----------|
| `execute()` | **MIDDLEWARE** (read-only observation) | Logs command+result; does not modify. |

---

**Class:** `DefaultCommandRunner`
**File:** `/home/sleroy/git/spring-cqrs-arch/src/main/java/com/byoskill/spring/cqrs/interceptors/DefaultCommandRunner.java`

```java
public class DefaultCommandRunner implements CommandInterceptor {
  public Object execute(final CommandExecutionContext context, final CommandRunnerChain chain) {
    return commandServiceHandler.handle(context.getRawCommand());  // DB_ACCESS (actual command execution)
  }
}
```

| Method | Classification | Rationale |
|--------|-----------------|-----------|
| `execute()` | **STATE_CHANGE** (core command execution) | Calls the actual handler; performs the command. |

---

**Class:** `GuavaEventBusService`
**File:** `/home/sleroy/git/spring-cqrs-arch/src/main/java/com/byoskill/spring/cqrs/events/guava/GuavaEventBusService.java`

```java
public class GuavaEventBusService implements BeanPostProcessor, EventBusService {
  public void publishEvent(Object event) { eventBus.post(event); }  // EVENT_PUBLISH
  public Object postProcessAfterInitialization(final Object bean, final String beanName) {
    if (AnnotationUtils.findAnnotation(bean.getClass(), EventHandler.class) != null) {
      registerEventSuscriber(bean);  // REGISTRATION (read bean metadata)
    }
    return bean;
  }
}
```

| Method | Classification | Rationale |
|--------|-----------------|-----------|
| `publishEvent()` | **EVENT_PUBLISH** | Publishes event to bus. |
| `postProcessAfterInitialization()` | **READ_METADATA** (scan for @EventHandler) | Registers event listeners; read-only scan of beans. |

---

## (c) CLASS-LEVEL EDGE LIST (DIRECTED GRAPH)

### Command-Side Edges (Write Model)

```
(controller/entry) SpringGate.dispatch --CALLS--> CommandExecutorServiceImpl.run
CommandExecutorServiceImpl.run --CREATES--> CommandExecutionContextImpl
CommandExecutorServiceImpl.run --RETRIEVES_HANDLER--> SpringCommandServiceProvider.getService(command)
SpringCommandServiceProvider.getService --RETURNS--> CommandServiceSpec (polymorphic: RandomNumberHandler, DummyObjectCommandHandler, etc.)
CommandExecutorServiceImpl.run --BUILDS_CHAIN--> CommandRunnerWorkflow.buildChain()
CommandRunnerWorkflow.buildChain --ADDS_INTERCEPTOR--> DefaultCommandRunner (wraps handler)
CommandRunnerWorkflow.buildChain --ADDS_INTERCEPTOR--> CommandLoggingRunner (observes)
CommandRunnerWorkflow.buildChain --ADDS_INTERCEPTOR--> EventThrowerRunner (post-event)
DefaultCommandRunner.execute --CALLS--> handler.handle(command)  [polymorphic]
RandomNumberHandler.handle --RETURNS--> Integer (result)
CommandServiceWithHandlerMethods.handle --RETURNS--> Object/void (result)
```

### Event-Side Edges (Async/Subscribe)

```
EventThrowerRunner.execute --CALLS--> eventBusService.publishEvent(event)
GuavaEventBusService.publishEvent --POSTS-TO--> eventBus (Guava EventBus)
GuavaEventBusService.postProcessAfterInitialization --REGISTERS_SUBSCRIBER--> @EventHandler-annotated bean
eventBus --DISPATCHES_EVENT--> @EventHandler-annotated method (async if AsyncEventBus)
```

### Complete Edge List (Directed)

| Source | Edge Type | Target | Package/Role |
|--------|-----------|--------|--------------|
| `SpringGate` | **calls** | `CommandExecutorServiceImpl.run()` | gateway → executor |
| `CommandExecutorServiceImpl.run()` | **retrieves** | `SpringCommandServiceProvider.getService()` | executor → router |
| `SpringCommandServiceProvider.getService()` | **returns** | `CommandServiceSpec<?,?>` (polymorphic) | router → handler interface |
| `CommandExecutorServiceImpl.run()` | **creates** | `CommandExecutionContextImpl` | executor → context |
| `CommandExecutorServiceImpl.run()` | **creates** | `DefaultCommandRunner` | executor → interceptor |
| `DefaultCommandRunner` | **wraps** | `CommandServiceSpec` (handler) | middleware → handler |
| `CommandRunnerWorkflow` | **builds chain** | `DefaultCommandRunner` + `CommandLoggingRunner` + `EventThrowerRunner` | workflow → interceptors |
| `CommandRunnerWorkflow` | **adds** | `CommandValidatingRunner` | workflow → validator |
| `CommandRunnerWorkflow` | **adds** | `CommandProfilingRunner` | workflow → profiler |
| `BootstrapRunner` | **kicks_off** | `CommandRunnerChain` | bootstrap → chain |
| `DefaultCommandRunner.execute()` | **calls** | `CommandServiceSpec.handle()` | interceptor → handler |
| `RandomNumberHandler.handle()` | **returns** | `Integer` (result) | handler → result |
| `CommandServiceWithHandlerMethods.handle()` | **returns** | `Object` (result) | handler → result |
| `EventThrowerRunner.execute()` | **calls** | `eventBusService.publishEvent()` | post-hook → event publish |
| `GuavaEventBusService.publishEvent()` | **dispatches** | `eventBus.post()` | event service → bus |
| `GuavaEventBusService.postProcessAfterInitialization()` | **scans** | `@EventHandler` annotation | bean post-proc → metadata |
| `GuavaEventBusService` | **registers** | `@EventHandler`-marked bean | event bus → subscriber |
| `eventBus` | **calls** | `@EventHandler` method (async dispatch) | bus → listener |

---

## (d) Proposed Golden CQRS Clustering

Given the CQRS pattern split and the strangler-fig validation goal, here is the **correct** decomposition:

### COMMAND SERVICE (Write Model)

**Owned Classes:**
- `com.byoskill.spring.cqrs.fakeapp.RandomNumberHandler` ✓ (command handler)
- `com.byoskill.spring.cqrs.fakeapp.RandomErrorNumberHandler` ✓ (command handler)
- `com.byoskill.spring.cqrs.fakeapp.DummyObjectCommandHandler` ✓ (command handler)
- `com.byoskill.spring.cqrs.fakeapp.BrokenObjectCommandHandler` ✓ (command handler)
- `com.byoskill.spring.cqrs.fakeapp.CommandServiceWithHandlerMethods` ✓ (command handler)
- `com.byoskill.spring.cqrs.fakeapp.commands.*` ✓ (command DTOs)
- `com.byoskill.spring.cqrs.commandgateway.SpringGate` ✓ (command entry point)
- `com.byoskill.spring.cqrs.commandgateway.Gate` ✓ (interface)
- `com.byoskill.spring.cqrs.commandgateway.SimpleCommandGateway` ✓ (alternate impl)
- `com.byoskill.spring.cqrs.workflow.CommandExecutorServiceImpl` ✓ (command executor)
- `com.byoskill.spring.cqrs.workflow.CommandRunnerWorkflow` ✓ (pipeline orchestrator)
- `com.byoskill.spring.cqrs.workflow.CommandRunnerWorkflowService` ✓ (factory)
- `com.byoskill.spring.cqrs.commandservices.SpringCommandServiceProvider` ✓ (handler router)
- `com.byoskill.spring.cqrs.commandservices.CommandServiceProvider` ✓ (interface)
- `com.byoskill.spring.cqrs.commands.CommandServiceSpec<C,R>` ✓ (handler contract)
- `com.byoskill.spring.cqrs.interceptors.DefaultCommandRunner` ✓ (core handler wrapper)
- `com.byoskill.spring.cqrs.interceptors.CommandLoggingRunner` ✓ (write logging)
- `com.byoskill.spring.cqrs.interceptors.CommandProfilingRunner` ✓ (write profiling)
- `com.byoskill.spring.cqrs.interceptors.CommandThrottlingRunner` ✓ (write throttling)
- `com.byoskill.spring.cqrs.interceptors.CommandTraceRunner` ✓ (write tracing)
- `com.byoskill.spring.cqrs.interceptors.DefaultExceptionHandlerRunner` ✓ (error handling)
- `com.byoskill.spring.cqrs.interceptors.CommandValidatingRunner` ✓ (command validation)
- `com.byoskill.spring.cqrs.interceptors.BootstrapRunner` ✓ (pipeline start)

**Shared Dependencies (see below):**
- `com.byoskill.spring.cqrs.workflow.CommandExecutionContext`
- `com.byoskill.spring.cqrs.workflow.CommandExecutionContextImpl`
- `com.byoskill.spring.cqrs.workflow.CommandRunnerChain`
- `com.byoskill.spring.cqrs.workflow.CommandInterceptor`

---

### QUERY SERVICE (Read Model)

**Owned Classes:**
- `com.byoskill.spring.cqrs.annotations.QueryHandler` ✓ (query annotation; read-only marker)
- *(No concrete query handlers in framework; left to user)*

**Note:** The framework provides the annotation but intentionally avoids prescribing query implementation. Users define their own read models, repositories, and query handlers. This aligns with CQRS philosophy: query side is simpler and should be straightforward database access (no interceptor chain).

---

### SHARED / DOMAIN / INFRASTRUCTURE

**Shared Classes (duplicated or symlink; used by both command and query sides):**

- **Command/Query DTOs:**
  - `com.byoskill.spring.cqrs.commands.CommandNotValidException` (exception; shared)
  - `com.byoskill.spring.cqrs.commands.EventThrower` (event factory interface; command-generated events subscribed by query handlers)
  - `com.byoskill.spring.cqrs.utils.validation.ObjectValidation` (validation shared)
  - `com.byoskill.spring.cqrs.utils.validation.InvalidCommandException` (validation shared)

- **Annotations (shared):**
  - `com.byoskill.spring.cqrs.annotations.CommandService` (command service marker)
  - `com.byoskill.spring.cqrs.annotations.CommandHandler` (command handler marker)
  - `com.byoskill.spring.cqrs.annotations.CommandOptions` (command options)
  - `com.byoskill.spring.cqrs.annotations.EventHandler` (event subscriber marker; query-side concern)
  - `com.byoskill.spring.cqrs.annotations.ReturnEventOnSuccess` (command→event bridge)
  - `com.byoskill.spring.cqrs.annotations.Throttle` (write-side throttling marker; shared)
  - `com.byoskill.spring.cqrs.annotations.EnableCqrsModule` (module bootstrap; shared)

- **Event Bus (Pub-Sub backbone):**
  - `com.byoskill.spring.cqrs.events.EventBusService` (event publisher interface; shared backbone)
  - `com.byoskill.spring.cqrs.events.guava.GuavaEventBusService` (event dispatcher; shared)
  - `com.byoskill.spring.cqrs.events.guava.GuavaEventBusPostProcessor` (bean scanning; shared)
  - `com.byoskill.spring.cqrs.events.guava.EventLoggerListener` (event logging; shared)

- **Workflow Context (shared data structure):**
  - `com.byoskill.spring.cqrs.workflow.CommandExecutionContext` (interface)
  - `com.byoskill.spring.cqrs.workflow.CommandExecutionContextImpl` (impl)
  - `com.byoskill.spring.cqrs.workflow.CommandRunnerChain` (interceptor chain contract)
  - `com.byoskill.spring.cqrs.workflow.CommandRunnerChainBlock` (chain node)
  - `com.byoskill.spring.cqrs.workflow.CommandInterceptor` (interceptor interface)
  - `com.byoskill.spring.cqrs.workflow.CommandExceptionContext` (exception context)
  - `com.byoskill.spring.cqrs.workflow.CommandExecutionException` (exception)

- **Configuration (initialization; shared):**
  - `com.byoskill.spring.cqrs.configuration.CqrsConfiguration` (base config)
  - `com.byoskill.spring.cqrs.configuration.CqrsImportationSelector` (config selector)
  - `com.byoskill.spring.cqrs.configuration.ImportDefaultCqrsConfiguration` (default setup)
  - `com.byoskill.spring.cqrs.configuration.ImportCqrsInjectionConfiguration` (DI setup)
  - `com.byoskill.spring.cqrs.configuration.ImportCommandServiceScanningConfiguration` (component scan)
  - `com.byoskill.spring.cqrs.configuration.EventBusConfiguration` (event bus setup)
  - `com.byoskill.spring.cqrs.configuration.ImportGuavaEventBusConfiguration` (Guava setup)
  - `com.byoskill.spring.cqrs.configuration.ImportGuavaAsyncEventBusConfiguration` (async Guava setup)
  - `com.byoskill.spring.cqrs.configuration.LoggingConfiguration` (logging setup)
  - `com.byoskill.spring.cqrs.configuration.TraceConfiguration` (tracing setup)

- **Throttling (cross-cutting):**
  - `com.byoskill.spring.cqrs.throttling.ThrottlingInterface` (interface)
  - `com.byoskill.spring.cqrs.configuration.DefaultThrottlingInterface` (default impl)

- **Exception Handling (cross-cutting):**
  - `com.byoskill.spring.cqrs.commandservices.CqrsUncaughtExceptionhandler` (exception handler)

- **Utility (cross-cutting):**
  - `com.byoskill.spring.cqrs.commandgateway.CqrsException` (base exception)
  - `com.byoskill.spring.cqrs.commandgateway.CommandGateway` (interface)
  - `com.byoskill.spring.cqrs.filter.SpringGateFilters` (gateway filters; optional)
  - `com.byoskill.spring.cqrs.interceptors.ExecutionStatus` (status tracking)
  - `com.byoskill.spring.cqrs.interceptors.TraceCommandExecution` (tracing)
  - `com.byoskill.spring.cqrs.interceptors.CommandTrace` (trace model)

---

## Summary: Decomposition Coupling

### Command Service → Query Service Dependencies
- **EventThrowerRunner** publishes events → Query service **@EventHandler** subscribers
- Command result → Event conversion (via `EventThrower` or `@ReturnEventOnSuccess`)

### Query Service → Command Service Dependencies
- **None** (query side reads materialized view or event log; does not call back to write)

### Coupling Points (Shared Contract)
1. **Event DTOs** (payload; published by commands, consumed by queries)
2. **Annotations** (@CommandHandler, @EventHandler, @CommandService, @QueryHandler)
3. **EventBusService** (async backbone; command → query coupling via pub-sub)
4. **Exceptions** (CommandNotValidException, InvalidCommandException)

### Strangler-Fig Decomposition Path
1. **Phase 1:** Extract Command service (core → new microservice)
   - Move all command handlers, gateway, interceptor chain
   - Move command DTOs
   - Share EventBusService + Config

2. **Phase 2:** Extract Query service (async-consume events)
   - Define read models / materialized views
   - Implement @EventHandler subscribers
   - Consume events from command service

3. **Phase 3:** Migrate event backbone
   - Replace Guava EventBus with RabbitMQ/Kafka
   - Command service publishes; Query service subscribes (async)

---

## File Reference Summary

| File | Role |
|------|------|
| `/home/sleroy/git/spring-cqrs-arch/src/main/java/com/byoskill/spring/cqrs/commandgateway/Gate.java` | Command entry interface |
| `/home/sleroy/git/spring-cqrs-arch/src/main/java/com/byoskill/spring/cqrs/commandgateway/SpringGate.java` | Command gateway impl |
| `/home/sleroy/git/spring-cqrs-arch/src/main/java/com/byoskill/spring/cqrs/commands/CommandServiceSpec.java` | Command handler interface |
| `/home/sleroy/git/spring-cqrs-arch/src/main/java/com/byoskill/spring/cqrs/workflow/CommandExecutorServiceImpl.java` | Command executor |
| `/home/sleroy/git/spring-cqrs-arch/src/main/java/com/byoskill/spring/cqrs/interceptors/DefaultCommandRunner.java` | Core handler wrapper |
| `/home/sleroy/git/spring-cqrs-arch/src/main/java/com/byoskill/spring/cqrs/interceptors/EventThrowerRunner.java` | Event publisher interceptor |
| `/home/sleroy/git/spring-cqrs-arch/src/main/java/com/byoskill/spring/cqrs/events/EventBusService.java` | Event pub-sub interface |
| `/home/sleroy/git/spring-cqrs-arch/src/main/java/com/byoskill/spring/cqrs/events/guava/GuavaEventBusService.java` | Event bus impl |
| `/home/sleroy/git/spring-cqrs-arch/src/main/java/com/byoskill/spring/cqrs/annotations/QueryHandler.java` | Query handler annotation |
| `/home/sleroy/git/spring-cqrs-arch/src/test/java/com/byoskill/spring/cqrs/fakeapp/RandomNumberHandler.java` | Example command handler |
| `/home/sleroy/git/spring-cqrs-arch/src/test/java/com/byoskill/spring/cqrs/fakeapp/CommandServiceWithHandlerMethods.java` | Example @CommandHandler service |
