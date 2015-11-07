package com.sequenceiq.ambari.config;

import org.quartz.simpl.SimpleJobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.concurrent.Executor;

/**
 * Created by jiang on 15/11/5.
 */
@Configuration
@EnableAsync
@EnableScheduling
public class AppConfig implements AsyncConfigurer{

    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);


    private int corePoolSize = 50;
    private int maxPoolSize = 500;
    private int queueCapacity = 1000;

    @Bean
    public ThreadPoolExecutorFactoryBean getThreadPoolExecutorFactoryBean() {
        ThreadPoolExecutorFactoryBean executorFactoryBean = new ThreadPoolExecutorFactoryBean();
        executorFactoryBean.setCorePoolSize(corePoolSize);
        executorFactoryBean.setMaxPoolSize(maxPoolSize);
        executorFactoryBean.setQueueCapacity(queueCapacity);
        return executorFactoryBean;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setTaskExecutor(getAsyncExecutor());
        scheduler.setAutoStartup(true);
        scheduler.setJobFactory(new SimpleJobFactory());
        return scheduler;
    }

    @Override
    public Executor getAsyncExecutor() {
        try {
            return getThreadPoolExecutorFactoryBean().getObject();
        } catch (Exception e) {
            LOGGER.error(Logger.ROOT_LOGGER_NAME, "Error creating task executor.", e);
        }
        return null;
    }
}
