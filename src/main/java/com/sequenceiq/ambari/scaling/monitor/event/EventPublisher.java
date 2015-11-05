package com.sequenceiq.ambari.scaling.monitor.event;

/**
 * Created by jiang on 15/11/4.
 */

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

public interface EventPublisher extends ApplicationEventPublisher, ApplicationEventPublisherAware, Runnable {
}
