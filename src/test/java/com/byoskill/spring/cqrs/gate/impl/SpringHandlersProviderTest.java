package com.byoskill.spring.cqrs.gate.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.byoskill.spring.cqrs.api.CommandServiceSpec;

@RunWith(MockitoJUnitRunner.class)
public class SpringHandlersProviderTest {
    public static class FakeCommandHandler implements CommandServiceSpec<String, String> {

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
        public void testGetService() throws Exception {
    	final CommandServiceSpec<String, String> value = new CommandServiceSpec<String, String>() {
    
    	    @Override
    	    public String handle(String command) throws RuntimeException {
    		return null;
    	    }
    	};
    	springHandlersProvider.putCommand(value, BEAN);
    
    	Mockito.when(beanFactory.getBean(BEAN)).thenReturn(value);
    	assertNotNull(springHandlersProvider.getService("COMMAND_AS_STRING"));
        }

}
