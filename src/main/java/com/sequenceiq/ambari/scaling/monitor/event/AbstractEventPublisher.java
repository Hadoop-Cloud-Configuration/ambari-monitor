package com.sequenceiq.ambari.scaling.monitor.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Created by jiang on 15/11/4.
 */
public abstract class AbstractEventPublisher implements EventPublisher {
	@Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishEvent(ApplicationEvent event) {
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

}
