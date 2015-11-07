package com.sequenceiq.ambari.services;

import com.sequenceiq.ambari.domain.Cluster;
import com.sequenceiq.ambari.repository.ClusterRepository;
import com.sequenceiq.ambari.repository.ClusterState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jiang on 15/11/4.
 */
@Service
public class ClusterService {

    @Autowired
    private ClusterRepository clusterRepository;

    public ClusterService(){

    }

    public Cluster create(){
        Cluster cluster = new Cluster();
        save(cluster);
        return cluster;
    }

    public void save(Cluster cluster){
        clusterRepository.save(cluster);
    }

    public List<Cluster> findActiveCluster(){
        return clusterRepository.findByState(ClusterState.RUNNING);
    }


    public void upscaleCluster(){

    }

    public void downscaleCluster(){

    }
}
