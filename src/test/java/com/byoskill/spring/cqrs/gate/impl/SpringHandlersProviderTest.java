package com.byoskill.spring.cqrs.gate.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import com.byoskill.spring.cqrs.api.ICommandHandler;
import com.byoskill.spring.cqrs.gate.impl.SpringHandlersProvider;

@RunWith(MockitoJUnitRunner.class)
public class SpringHandlersProviderTest {
	private static final String BEAN = "BEAN";

	public static class FakeCommandHandler implements ICommandHandler<String, String> {
		
		@Override
		public String handle(final String _command) {
			return _command.toUpperCase();
		}
		
	}
	
	@Mock
	private ConfigurableListableBeanFactory beanFactory;
	
	@InjectMocks
	private SpringHandlersProvider springHandlersProvider;
	
	@Test
	public void testGetHandler() throws Exception {
		initHandlers();
		final ICommandHandler value = new ICommandHandler() {
			
			@Override
			public Object handle(final Object _command) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		Mockito.when(beanFactory.getBean(BEAN, ICommandHandler.class)).thenReturn(value);
		assertNotNull(springHandlersProvider.getHandler("COMMAND_AS_STRING"));
	}
	
	@Test
	public void testOnApplicationEvent() throws Exception {
		initHandlers();
	}
	
	private void initHandlers() {
		Mockito.when(beanFactory.getBeanNamesForType(ICommandHandler.class)).thenReturn(new String[] { BEAN });
		final GenericBeanDefinition value = new GenericBeanDefinition();
		value.setBeanClass(FakeCommandHandler.class);
		Mockito.when(beanFactory.getBeanDefinition(BEAN)).thenReturn(value);
		springHandlersProvider.onApplicationEvent();
	}
}
