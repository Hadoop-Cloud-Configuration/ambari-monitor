package com.sequenceiq.ambari.scaling.monitor.excutor;

import com.sequenceiq.ambari.client.AmbariClient;
import com.sequenceiq.ambari.factory.AmbariClientFactory;
import com.sequenceiq.ambari.repository.MetricsAlertRepository;
import com.sequenceiq.ambari.services.ClusterService;
import com.sequenceiq.ambari.domain.Cluster;
import com.sequenceiq.ambari.domain.MetricsAlert;
import com.sequenceiq.ambari.domain.ScalingType;
import com.sequenceiq.ambari.scaling.monitor.event.AbstractEventPublisher;
import com.sequenceiq.ambari.scaling.monitor.event.ScalingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by jiang on 15/11/4.
 */
@Component("MetricsMonitorExcutor")
@Scope("prototype")
public class MetricsMonitorExcutor extends AbstractEventPublisher implements MonitorExcutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsMonitorExcutor.class);

    private final String ALERT_STATE="state";
    private final String ALERT_LAST_STATE_CHANGED="timestamp";
    private final long MIN_IN_MS = 1000*60;
    private long cluster_id;
    @Autowired
    private ClusterService clusterService;
    @Autowired
    private MetricsAlertRepository metricsAlertRepository;
    @Override
    public void run() {
        Cluster cluster  = clusterService.findOne(cluster_id);
        AmbariClient client = AmbariClientFactory.createAmbariClient(cluster);

        for(MetricsAlert alert : metricsAlertRepository.findAllByCluster(cluster_id) ){
            List<Map<String, Object>> alertHistory = client.getAlertHistory(alert.getDefinitionName(), 1);
            int historySize = alertHistory.size();
            if(!alertHistory.isEmpty()){
                Map<String, Object> history = alertHistory.get(0);
                boolean isRequiredState = isStateSame((String)history.get(ALERT_STATE), alert);
                if(isRequiredState){
                    long interval = getStateDuration(history);
                    if(isDurationMet(interval, alert) ){
                        LOGGER.info("Metircs Monitor trigger a scaling event on {} when {} is {} for more than {} minutes", cluster.getHost() , alert.getDefinitionName() ,alert.getAlertState(),alert.getTimeDefinition());
                        publishEvent(new ScalingEvent(alert));
                    }
                }
            }
        }
    }

    private boolean isStateSame(String current, MetricsAlert alert){
        return current.equals(alert.getAlertState().getValue());
    }

    private boolean isDurationMet(Long current, MetricsAlert alert){
        return current > alert.getTimeDefinition()* MIN_IN_MS;
    }
    private Long getStateDuration(Map<String, Object> history ){
        return System.currentTimeMillis() - (Long)history.get(ALERT_LAST_STATE_CHANGED);
    }


    @Override
    public void setContext(Map<String, Object> context) {
        this.cluster_id = (long) context.get(ExcutorContext.CLUSTER_ID.name());
    }
}
