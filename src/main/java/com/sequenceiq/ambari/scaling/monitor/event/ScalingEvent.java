package com.sequenceiq.ambari.scaling.monitor.event;

import com.sequenceiq.ambari.domain.Alert;
import com.sequenceiq.ambari.domain.ScalingType;
import org.springframework.context.ApplicationEvent;

/**
 * Created by jiang on 15/11/4.
 */
public class ScalingEvent extends ApplicationEvent {

    private Alert alert;

    public ScalingEvent(Alert alert){
        super(alert);
        this.alert =  alert;
    }

    public ScalingType getScalingType(){
        return this.alert.getScalingPolicy();
    }

    public Alert getAlert(){
        return this.alert;
    }
}
