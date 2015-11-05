package com.sequenceiq.ambari.scaling.monitor.handler;

import com.sequenceiq.ambari.domain.ScalingType;
import com.sequenceiq.ambari.scaling.monitor.event.ScalingEvent;
import com.sequenceiq.ambari.scaling.services.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

/**
 * Created by jiang on 15/11/4.
 */
public class ScaleHandler implements ApplicationListener<ScalingEvent> {

    @Autowired
    private ClusterService clusterService;


    @Override
    public void onApplicationEvent(ScalingEvent event) {
        if(event.getScalingType()  == ScalingType.UP_SCALE){
            clusterService.upscaleCluster();
        }else{
            clusterService.downscaleCluster();
        }
    }
}
