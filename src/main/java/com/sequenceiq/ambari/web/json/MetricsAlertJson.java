package com.sequenceiq.ambari.web.json;

import com.sequenceiq.ambari.domain.AlertState;
import com.sequenceiq.ambari.domain.ScalingType;
import org.springframework.stereotype.Component;

/**
 * Created by jiang on 15/11/7.
 */
@Component
public class MetricsAlertJson extends  AbstractAlertJson {

    private String alertDefinition;
    private int time;
    private AlertState alertState;
    private ScalingType scalingPolicy;

    public void setTime(int time) {
        this.time = time;
    }

    public ScalingType getScalingPolicy() {
        return scalingPolicy;
    }

    public void setScalingPolicy(ScalingType scalingPolicy) {
        this.scalingPolicy = scalingPolicy;
    }

    public String getAlertDefinition() {
        return alertDefinition;
    }

    public void setAlertDefinition(String alertDefinition) {
        this.alertDefinition = alertDefinition;
    }

    public int getTime() {
        return time;
    }

    public void setPeriod(int time) {
        this.time = time;
    }

    public AlertState getAlertState() {
        return alertState;
    }

    public void setAlertState(AlertState alertState) {
        this.alertState = alertState;
    }

}
