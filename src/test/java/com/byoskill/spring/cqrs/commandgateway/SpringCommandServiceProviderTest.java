package com.byoskill.spring.cqrs.commandgateway;

import static org.junit.Assert.assertNotNull;

import com.byoskill.spring.cqrs.commandservices.SpringCommandServiceProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.byoskill.spring.cqrs.commands.CommandServiceSpec;

@RunWith(MockitoJUnitRunner.class)
public class SpringCommandServiceProviderTest {
    private static final String BEAN = "BEAN";
    @Mock
    private ConfigurableListableBeanFactory beanFactory;
    @InjectMocks
    private SpringCommandServiceProvider springCommandServiceProvider;

    @Test
    public void testGetService() throws Exception {
        final CommandServiceSpec<String, String> value = new CommandServiceSpec<String, String>() {

            @Override
            public String handle(String command) throws RuntimeException {
                return null;
            }
        };
        springCommandServiceProvider.subscribe(String.class.getName(), value);

        Mockito.when(beanFactory.getBean(BEAN)).thenReturn(value);
        assertNotNull(springCommandServiceProvider.getService("COMMAND_AS_STRING"));
    }

    public static class FakeCommandHandler implements CommandServiceSpec<String, String> {

        @Override
        public String handle(final String _command) {
            return _command.toUpperCase();
        }

    }

}
