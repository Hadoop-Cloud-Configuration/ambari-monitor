package com.sequenceiq.ambari.client;

import com.sequenceiq.ambari.domain.Cluster;
import org.springframework.stereotype.Component;

/**
 * Created by jiang on 15/11/4.
 */
@Component
public class AmbariClientFactory {
     public AmbariClient createAmbariClient(Cluster cluster){
         return new AmbariClient(cluster.getHost(), cluster.getPort());
     }
}
