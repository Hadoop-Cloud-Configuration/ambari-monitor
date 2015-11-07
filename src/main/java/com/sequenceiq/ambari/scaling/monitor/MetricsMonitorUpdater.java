package com.sequenceiq.ambari.scaling.monitor;

import com.sequenceiq.ambari.domain.Cluster;
import com.sequenceiq.ambari.scaling.monitor.excutor.ExcutorContext;
import com.sequenceiq.ambari.scaling.monitor.excutor.MetricsMonitorExcutor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * Created by jiang on 15/11/5.
 */
@Component
public class MetricsMonitorUpdater extends AbstractMonitor{
    @Override
    public String getUpdateRate() {
        return CronUpdateRate.METRIC_UPDATE_RATE_CRON;
    }

    @Override
    public String getIdentifier() {
        return "Metrics-Monitor";
    }

    @Override
    public Class getMonitorType() {
        return MetricsMonitorExcutor.class;
    }

    @Override
    public Map<String, Object> getContext(Cluster cluster){
        return Collections.<String, Object>singletonMap(ExcutorContext.CLUSTER_ID.name(), cluster.getId());
    }
}
