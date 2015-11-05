package com.sequenceiq.ambari.scaling.monitor.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import javax.annotation.Resource;

/**
 * Created by jiang on 15/11/4.
 */
public abstract class AbstractEventPublisher implements EventPublisher {

    @Resource(name="applicationEventPublisher")
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void publishEvent(ApplicationEvent event) {
        this.eventPublisher.publishEvent(event);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

}
