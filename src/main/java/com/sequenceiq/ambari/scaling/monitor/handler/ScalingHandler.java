package com.sequenceiq.ambari.scaling.monitor.handler;

import com.sequenceiq.ambari.domain.ScalingType;
import com.sequenceiq.ambari.scaling.monitor.event.ScalingEvent;
import com.sequenceiq.ambari.scaling.services.ScalingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by jiang on 15/11/4.
 */
@Component
public class ScalingHandler implements ApplicationListener<ScalingEvent> {

    @Autowired
    private ScalingService scalingService;

    @Override
    public void onApplicationEvent(ScalingEvent event) {
        System.out.println("Event Handled");
        if(event.getScalingType()  == ScalingType.UP_SCALE){
            scalingService.upScaleCluster();
        }else{
            scalingService.downScaleCluster();
        }
    }
}
