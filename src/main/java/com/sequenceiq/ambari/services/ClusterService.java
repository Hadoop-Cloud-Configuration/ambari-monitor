package com.sequenceiq.ambari.services;

import com.sequenceiq.ambari.domain.Ambari;
import com.sequenceiq.ambari.domain.Cluster;
import com.sequenceiq.ambari.repository.ClusterRepository;
import com.sequenceiq.ambari.domain.ClusterState;
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
    @Autowired
    private AlertService alertService;

    public ClusterService(){

    }

    public Cluster create(Ambari ambari){
        Cluster cluster = new Cluster(ambari);
        save(cluster);
        alertService.addAmbariAlert(cluster);
        return cluster;
    }

    public void save(Cluster cluster){
        clusterRepository.save(cluster);
    }

    public void remove(Long id){clusterRepository.delete(id);}

    public List<Cluster> findActiveCluster(){
        return clusterRepository.findByState(ClusterState.RUNNING);
    }

    public Cluster findOne(Long id){ return clusterRepository.findOne(id); }

    public void upscaleCluster(){

    }

    public void downscaleCluster(){

    }
}
