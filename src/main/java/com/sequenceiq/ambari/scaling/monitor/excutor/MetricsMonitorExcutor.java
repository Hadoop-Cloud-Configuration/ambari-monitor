package com.sequenceiq.ambari.scaling.monitor.excutor;

import com.sequenceiq.ambari.domain.ScalingType;
import com.sequenceiq.ambari.scaling.monitor.event.AbstractEventPublisher;
import com.sequenceiq.ambari.scaling.monitor.event.ScalingEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by jiang on 15/11/4.
 */
@Component("MetricsMonitorExcutor")
@Scope("prototype")
public class MetricsMonitorExcutor extends AbstractEventPublisher implements MonitorExcutor {

    private long cluster_id;
    @Override
    public void run() {
        if(true){
            publishEvent(new ScalingEvent(ScalingType.UP_SCALE));
        }else{
            publishEvent(new ScalingEvent(ScalingType.DOWN_SCALE));
        }
    }

    @Override
    public void setContext(Map<String, Object> context) {
        this.cluster_id = (long) context.get(ExcutorContext.CLUSTER_ID.name());
    }
}
