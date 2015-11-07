package com.sequenceiq.ambari.scaling.monitor;

import com.sequenceiq.ambari.domain.Cluster;
import org.quartz.Job;

import java.util.Map;

/**
 * Created by jiang on 15/11/5.
 */
public interface Monitor extends Job {
    public String getUpdateRate();

    public String getIdentifier();

    public Class getMonitorType();

    public Map<String, Object> getContext(Cluster cluster);
}
