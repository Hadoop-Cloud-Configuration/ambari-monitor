package com.sequenceiq.ambari.scaling.monitor.event;

import com.sequenceiq.ambari.domain.ScalingType;
import org.springframework.context.ApplicationEvent;

/**
 * Created by jiang on 15/11/4.
 */
public class ScalingEvent extends ApplicationEvent {

    private ScalingType scalingType;

    public ScalingEvent(ScalingType scalingType){
        super(scalingType);
        this.scalingType =  scalingType;
    }

    public ScalingType getScalingType(){
        return this.scalingType;
    }
}
