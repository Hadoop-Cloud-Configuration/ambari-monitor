package com.sequenceiq.ambari.services;

import com.sequenceiq.ambari.client.AmbariClient;
import com.sequenceiq.ambari.factory.AmbariClientFactory;
import com.sequenceiq.ambari.domain.Cluster;
import com.sequenceiq.ambari.domain.MetricsAlert;
import com.sequenceiq.ambari.repository.MetricsAlertRepository;
import com.sequenceiq.ambari.repository.ClusterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.ui.freemarker.FreeMarkerTemplateUtils.processTemplateIntoString;
import freemarker.template.Configuration;

/**
 * Created by jiang on 15/11/7.
 */
@Service
public class AlertService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlertService.class);
    private final String ALERT_PATH="alerts/";
    private final String APP_ALERT="pending_app.ftl";
    private final String CONTAINER_ALERT="pending_container.ftl";
    private final String MEMORY_ALERT="allocated_memory.ftl";

    @Autowired
    private Configuration freemarker;
    @Autowired
    private MetricsAlertRepository metricsAlertRepository;
    @Autowired
    private ClusterRepository clusterRepository;

    public MetricsAlert createMetricsAlert(Long clusterId, MetricsAlert alert){
        Cluster cluster = clusterRepository.findOne(clusterId);
        cluster.addMetricsAlert(alert);
        alert.setCluster(cluster);
        alert = metricsAlertRepository.save(alert);
        clusterRepository.save(cluster);
        return alert;
    }

    public void addAmbariAlert(Cluster cluster){
        AmbariClient ambariClient = AmbariClientFactory.createAmbariClient(cluster);
        try{
            createAlert(ambariClient, getAlertTemplate(ambariClient, APP_ALERT));
            createAlert(ambariClient, getAlertTemplate(ambariClient, CONTAINER_ALERT));
            createAlert(ambariClient, getAlertTemplate(ambariClient, MEMORY_ALERT));

        }catch (Exception e){
            LOGGER.error("Cannot parse alert template");
        }

    }

    public void createAlert(AmbariClient client, String alert){
        try {
            client.createAlert(alert);
        }catch (Exception e){
            LOGGER.error("Cannot add alert to Ambari server");
        }
    }

    public String getAlertTemplate(AmbariClient client, String name) throws Exception{
        Map<String, String> model = Collections.singletonMap("clusterName", client.getClusterName());
        return processTemplateIntoString(freemarker.getTemplate(ALERT_PATH + name, "UTF-8"), model);
    }

    public List<MetricsAlert> findMetricsAlertsByClusterId(long clusterId) {
        return metricsAlertRepository.findAllByCluster(clusterId);
    }

    public void removeMetricsAlert(long clusterId, long alertId) {
        MetricsAlert alert = metricsAlertRepository.findByCluster(clusterId, alertId);
        metricsAlertRepository.delete(alert);
    }
}
