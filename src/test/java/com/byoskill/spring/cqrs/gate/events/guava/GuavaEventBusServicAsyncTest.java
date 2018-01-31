package com.byoskill.spring.cqrs.gate.events.guava;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.PayloadApplicationEvent;

import com.byoskill.spring.cqrs.gate.conf.CqrsConfiguration;
import com.google.common.eventbus.Subscribe;

public class GuavaEventBusServicAsyncTest {

    public static class TestEventSuscriber {

	public List<Object> events = new ArrayList<>();

	@Subscribe
	public void suscribe(final Object event) {
	    events.add(event);
	}

    }

    private ApplicationContext applicationContext;

    private GuavaEventBusService guavaEventBusService;

    private final TestEventSuscriber testEventSuscriber = new TestEventSuscriber();

    @After
    public void after() {
	guavaEventBusService.destroy();
    }

    @Before
    public void before() {
	final CqrsConfiguration cqrsConfiguration = new CqrsConfiguration();
	applicationContext = Mockito.mock(ApplicationContext.class);
	cqrsConfiguration.setAsyncEventQueries(true);
	cqrsConfiguration.setLoggingEnabled(true);
	guavaEventBusService = new GuavaEventBusService(cqrsConfiguration);

	guavaEventBusService.registerEventSuscriber(testEventSuscriber);

    }

    @Test
    public final void testPublishEventApplicationEvent() {
	guavaEventBusService.publishEvent(new PayloadApplicationEvent(this, "EVENT1"));
	try {
	    Thread.sleep(1000);
	} catch (final InterruptedException e) {
	    e.printStackTrace();
	}
	Assert.assertEquals(testEventSuscriber.events.size(), 1);
    }

    @Test
    public final void testPublishEventObject() {
	guavaEventBusService.publishEvent("EVENT1");
	try {
	    Thread.sleep(1000);
	} catch (final InterruptedException e) {
	    e.printStackTrace();
	}
	Assert.assertEquals(testEventSuscriber.events.size(), 1);

    }

}
