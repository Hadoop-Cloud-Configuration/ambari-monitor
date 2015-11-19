package com.sequenceiq.ambari.factory;

import com.sequenceiq.ambari.client.AmbariClient;
import com.sequenceiq.ambari.domain.Cluster;

/**
 * Created by jiang on 15/11/6.
 */
public class AmbariClientFactory {
    public static AmbariClient createAmbariClient(Cluster cluster){
        return new AmbariClient(cluster.getHost(), cluster.getPort(), cluster.getAmbariUser(),cluster.getAmbariPss());
    }
}
