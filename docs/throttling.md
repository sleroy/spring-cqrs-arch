# Throttling

Commands can be decorated with an annotation *@Throttle*. This annotation takes a parameter, the name of the Rate limiter. Therefore you can defined named policy that limits the number of command executed for a given period of time.

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
