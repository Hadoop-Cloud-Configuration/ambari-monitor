package com.sequenceiq.ambari.scaling.monitor;

import com.sequenceiq.ambari.domain.ScalingType;
import com.sequenceiq.ambari.scaling.monitor.event.AbstractEventPublisher;
import com.sequenceiq.ambari.scaling.monitor.event.ScalingEvent;

/**
 * Created by jiang on 15/11/4.
 */
public class MetricsMonitor extends AbstractEventPublisher {
    @Override
    public void run() {
        if(true){
            publishEvent(new ScalingEvent(ScalingType.UP_SCALE));
        }else{
            publishEvent(new ScalingEvent(ScalingType.DOWN_SCALE));
        }
    }
}
