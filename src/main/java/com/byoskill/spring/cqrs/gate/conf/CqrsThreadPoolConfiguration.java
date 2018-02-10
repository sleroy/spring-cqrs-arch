package com.byoskill.spring.cqrs.gate.conf;

import java.util.concurrent.RejectedExecutionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.byoskill.spring.cqrs.gate.impl.CqrsRejectedExecutionHandler;
import com.byoskill.spring.cqrs.gate.impl.CqrsTaskDecorator;

@Configuration
public class CqrsThreadPoolConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(CqrsThreadPoolConfiguration.class);

    @Autowired
    private CqrsConfiguration cqrsConfiguration;


    @Bean(name="cqrs-executor")
    public ThreadPoolTaskExecutor cqrsThreadExecutor(final RejectedExecutionHandler rejectionHandler, final TaskDecorator taskDecorator) {
	ThreadPoolTaskExecutor threadPoolTaskExecutor;
	threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
	threadPoolTaskExecutor.setAllowCoreThreadTimeOut(cqrsConfiguration.getAllowCoreTimeout());
	threadPoolTaskExecutor.setAwaitTerminationSeconds(cqrsConfiguration.getAwaitTerminationSeconds());
	threadPoolTaskExecutor.setBeanName("cqrs_pool_executor");
	threadPoolTaskExecutor.setCorePoolSize(cqrsConfiguration.getCorePoolSize());
	threadPoolTaskExecutor.setDaemon(cqrsConfiguration.isDaemon());
	threadPoolTaskExecutor.setKeepAliveSeconds(cqrsConfiguration.getKeepAliveSeconds());
	threadPoolTaskExecutor.setMaxPoolSize(cqrsConfiguration.getMaxPoolSize());
	threadPoolTaskExecutor.setQueueCapacity(cqrsConfiguration.getQueueCapacity());
	threadPoolTaskExecutor.setRejectedExecutionHandler(rejectionHandler);
	threadPoolTaskExecutor.setTaskDecorator(taskDecorator);
	threadPoolTaskExecutor.setThreadGroupName("cqrs");
	threadPoolTaskExecutor.setThreadNamePrefix("cqrs-command");
	threadPoolTaskExecutor.setThreadPriority(cqrsConfiguration.getThreadPriority());
	threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(cqrsConfiguration.isAwaitingOnShutdown());
	return threadPoolTaskExecutor;


    }

    @Bean
    public RejectedExecutionHandler rejectionHandler() {
	return new CqrsRejectedExecutionHandler();
    }


    @Bean
    public TaskDecorator taskDecorator()  {
	return new CqrsTaskDecorator();
    }
}
