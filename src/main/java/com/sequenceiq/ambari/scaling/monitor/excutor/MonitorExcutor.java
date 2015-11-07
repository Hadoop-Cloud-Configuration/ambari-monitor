package com.sequenceiq.ambari.scaling.monitor.excutor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.Map;

/**
 * Created by jiang on 15/11/5.
 */
public interface MonitorExcutor  extends ApplicationEventPublisher, ApplicationEventPublisherAware, Runnable{
    void setContext(Map<String, Object> context);
}
