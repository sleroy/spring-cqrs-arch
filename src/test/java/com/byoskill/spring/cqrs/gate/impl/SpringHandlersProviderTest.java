package com.byoskill.spring.cqrs.gate.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;

import com.byoskill.spring.cqrs.api.ICommandHandler;

@RunWith(MockitoJUnitRunner.class)
public class SpringHandlersProviderTest {
    public static class FakeCommandHandler implements ICommandHandler<String, String> {

	@Override
	public String handle(final String _command) {
	    return _command.toUpperCase();
	}

    }

    private static final String BEAN = "BEAN";

    @Mock
    private ConfigurableListableBeanFactory beanFactory;

    @InjectMocks
    private SpringHandlersProvider springHandlersProvider;

    @Test
    public void testGetHandler() throws Exception {
	initHandlers();
	final ICommandHandler value = new ICommandHandler<String, String>() {

	    @Override
	    public String handle(String command) throws Exception {
		// TODO Auto-generated method stub
		return null;
	    }

	};
	springHandlersProvider.postProcessAfterInitialization(value, BEAN);
	
	Mockito.when(beanFactory.getBean(BEAN, ICommandHandler.class)).thenReturn(value);
	assertNotNull(springHandlersProvider.getHandler("COMMAND_AS_STRING"));
    }

    @Test
    public void testOnApplicationEvent() throws Exception {
	initHandlers();
    }

    private void initHandlers() {

	springHandlersProvider.postProcessAfterInitialization(BEAN, BEAN);
    }
}
