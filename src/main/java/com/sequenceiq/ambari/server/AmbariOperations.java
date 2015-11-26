package com.sequenceiq.ambari.server;

import com.sequenceiq.ambari.client.AmbariClient;

import java.util.Map;

/**
 * Created by jiang on 15/11/22.
 */
public class AmbariOperations {
    private AmbariClient ambariClient;
    private Map<String, Integer> requests;

    public AmbariOperations( AmbariClient ambariClient, Map<String, Integer> requests) {
        this.ambariClient = ambariClient;
        this.requests = requests;
    }

    public AmbariClient getAmbariClient() {
        return ambariClient;
    }

    public Map<String, Integer> getRequests() {
        return requests;
    }

}
