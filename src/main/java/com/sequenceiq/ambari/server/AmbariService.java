package com.sequenceiq.ambari.server;

import com.sequenceiq.ambari.client.AmbariClient;
import com.sequenceiq.ambari.services.ClusterService;
import com.sequenceiq.ambari.domain.Cluster;
import com.sequenceiq.ambari.domain.ClusterState;
import com.sequenceiq.ambari.web.controller.BadRequestException;
import groovyx.net.http.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.Collections.singletonMap;

/**
 * Created by jiang on 15/11/22.
 */
@Component
public class AmbariService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AmbariService.class);

    @Autowired
    private AmbariOperationService ambariOperationService;
    @Autowired
    private ClusterService clusterService;

    public void upscaleAmbari(List<String> hosts, Cluster cluster, AmbariClient ambariClient) {
        PollingResult pollingResult = waitForOperations(ambariClient, installServices(hosts, cluster, ambariClient));
        if (PollingResult.isSuccess(pollingResult)) {
            pollingResult = waitForOperations(ambariClient, singletonMap("START_SERVICES", ambariClient.startAllServices()));
            if (PollingResult.isSuccess(pollingResult)) {
                cluster.setState(ClusterState.RUNNING);
                clusterService.save(cluster);
            }
        }

    }

    public void decomissionNodes(List<String> hostGroup, Cluster cluster, AmbariClient client) throws Exception
    {
        ScalingService scalingService = new ScalingService();
        for(String host : hostGroup) {
            client.removeHost(host);
        }
    }




    private Map<String, Integer> installServices(List<String> hosts, Cluster cluster, AmbariClient ambariClient) {
        try {
            Map<String, Integer> requests = new HashMap<>();


            ambariClient.addHosts(hosts);
            List<String> components = new ArrayList<String>();
            for (String s : AmbariServerConfig.componentNames) {
                components.add(s);
            }

            ambariClient.addComponentsToHosts(hosts, components);
            requests.put("Install components to the new hosts", ambariClient.installAllComponentsOnHosts(hosts));
            return requests;
        } catch (HttpResponseException e) {
            if ("Conflict".equals(e.getMessage())) {
                throw new BadRequestException("Host already exists.", e);
            } else {
                throw new AmbariScalingException("Ambari could not install services. ", e);
            }
        }
    }


    public PollingResult waitForOperations(AmbariClient ambariClient, Map<String, Integer> operationRequests) {
        return ambariOperationService.waitForAmbariOperations(ambariClient, operationRequests);
    }

}
