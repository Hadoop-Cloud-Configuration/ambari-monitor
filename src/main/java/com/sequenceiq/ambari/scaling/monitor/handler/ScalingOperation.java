package com.sequenceiq.ambari.scaling.monitor.handler;

import com.sequenceiq.ambari.client.AmbariClient;
import com.sequenceiq.ambari.domain.*;
import com.sequenceiq.ambari.factory.AmbariClientFactory;
import com.sequenceiq.ambari.server.*;
import com.sequenceiq.ambari.services.ClusterService;
import com.sequenceiq.ambari.services.InstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiang on 15/11/22.
 */
@Component("ScalingOperation")
@Scope("prototype")
public class ScalingOperation implements Runnable{

    private static final Logger LOGGER = LoggerFactory.getLogger(ScalingOperation.class);
    @Autowired
    private AmbariService ambariService;
    @Autowired
    private InstanceService instanceService;
    @Autowired
    private ClusterService clusterService;

    private Cluster cluster;
    private int updateNodes;
    private  ScalingType scalingType;

    public ScalingOperation(Cluster cluster, ScalingType scalingtype){
        this.cluster = cluster;
        this.scalingType = scalingtype;
    }

    private int getDesiredNodes(AmbariClient client) {
        int nodes = 0;
        if(scalingType == ScalingType.UP_SCALE){
            nodes =2;
        }else{
           int clusterSize =  client.getClusterHosts().size();
            nodes = clusterSize-cluster.MIN_SIZE >0 ? clusterSize-cluster.MIN_SIZE :0;
        }

        return nodes;
    }

    @Override
    public void run() {
        long clusterId = this.cluster.getId();
        AmbariClient client =  AmbariClientFactory.createAmbariClient(cluster);
        this.updateNodes =  getDesiredNodes(client);

        if( updateNodes>0){
            if(this.scalingType == ScalingType.DOWN_SCALE){
                downScaleCluster(client, clusterId, updateNodes);
            }else{
                upScaleCluster(client, clusterId, updateNodes);
            }
            cluster.setState(ClusterState.RUNNING);
            clusterService.save(cluster);
        }else{
            LOGGER.info("No nodes need to be modified to the cluster");
        }


    }

    private void upScaleCluster(AmbariClient client, long clusterId, int nodes) {
        List<Instance> instanceList = instanceService.findAvailableInstances(clusterId, nodes);
        int n = instanceList.size();
        if(n< nodes){
            LOGGER.info("Requested {} instances, only {} found ", nodes, n);
        }
        if( n== 0){
            throw new AmbariScalingException("There is no available instances for scaling");
        }

        List<String> hostGroup = new ArrayList<>();
        for(Instance instance : instanceList){
            hostGroup.add(instance.getPrivateIP());
        }
        try {
            ambariService.upscaleAmbari(hostGroup, cluster, client);
            instanceService.updateState(InstanceState.RUNNING, instanceList);
            LOGGER.info("Add {} instances to the cluster", n);
        }catch (AmbariScalingException e){
            e.printStackTrace();
            LOGGER.error("Cannot add {} instances", n);
        }

    }

    private void downScaleCluster(AmbariClient client, long clusterId,int nodes) {
        try{
            List<Instance> instanceList = instanceService.findInstancesByState(InstanceState.RUNNING,clusterId, nodes);
            if(instanceList.size() == 0){
                LOGGER.info("No nodes need to be remvoed");
                return;
            }
            List<String> hostGroup = new ArrayList<>();
            for(Instance instance : instanceList){
                hostGroup.add(instance.getPrivateIP());
            }
            ambariService.decomissionNodes(hostGroup, cluster, client);
            instanceService.updateState(InstanceState.STARTED, instanceList);

            LOGGER.info("Requested delete {} instances , {} nodes decomission ", nodes, nodes);

        }catch (Exception e){
            e.printStackTrace();

            LOGGER.error("Cannot down scale {} instances", nodes );
        }
    }





}
