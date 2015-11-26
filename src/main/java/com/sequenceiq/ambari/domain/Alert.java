package com.sequenceiq.ambari.domain;

/**
 * Created by jiang on 15/11/7.
 */
public interface Alert {

    public ScalingType getScalingPolicy();
    public AlertState getAlertState();
}
