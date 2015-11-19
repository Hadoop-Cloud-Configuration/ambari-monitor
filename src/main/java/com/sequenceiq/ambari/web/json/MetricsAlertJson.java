package com.sequenceiq.ambari.web.json;

import com.sequenceiq.ambari.domain.AlertState;
import org.springframework.stereotype.Component;

/**
 * Created by jiang on 15/11/7.
 */
@Component
public class MetricsAlertJson extends  AbstractAlertJson {

    private String alertDefinition;
    private int time;
    private AlertState alertState;

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
