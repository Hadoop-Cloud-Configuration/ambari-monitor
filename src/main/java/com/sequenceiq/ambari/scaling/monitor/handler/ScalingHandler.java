package com.sequenceiq.ambari.scaling.monitor.handler;

import com.sequenceiq.ambari.domain.Cluster;
import com.sequenceiq.ambari.domain.ClusterState;
import com.sequenceiq.ambari.domain.MetricsAlert;
import com.sequenceiq.ambari.domain.ScalingType;
import com.sequenceiq.ambari.scaling.monitor.event.ScalingEvent;
import com.sequenceiq.ambari.server.ScalingService;
import com.sequenceiq.ambari.server.AmbariServerConfig;
import com.sequenceiq.ambari.services.ClusterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

/**
 * Created by jiang on 15/11/4.
 */
@Component
public class ScalingHandler implements ApplicationListener<ScalingEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScalingHandler.class);

    @Autowired
    private ScalingService scalingService;
    @Autowired
    private ClusterService clusterService;
    @Autowired
    private ExecutorService executorService;
    @Autowired
    private AmbariServerConfig ambariServerConfig;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ScalingEvent event) {
        MetricsAlert alert = (MetricsAlert)event.getAlert();
        Cluster cluster = clusterService.findOne(alert.getCluster().getId());
        boolean isOperationReady = isReadyForScalingOperation(cluster);
            if( isOperationReady){

                ScalingOperation scalingOperation = (ScalingOperation)applicationContext.getBean("ScalingOperation",cluster,alert.getScalingPolicy());
                executorService.execute(scalingOperation);
                updateCluster(cluster);

            }else{
                LOGGER.info("Wait until previous operation done");
            }

    }


    private boolean isReadyForScalingOperation(Cluster cluster){
           return cluster.getState()== ClusterState.RUNNING&&cluster.getOperationInterval()*60000 < System.currentTimeMillis()- cluster.getLatestScalingActivity();
    }

    private void updateCluster(Cluster cluster){

        cluster.setLatestScalingActivity();
        cluster.setState(ClusterState.UPDATING);
        clusterService.save(cluster);
    }

}
