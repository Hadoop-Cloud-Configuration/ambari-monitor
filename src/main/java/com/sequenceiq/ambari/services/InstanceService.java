package com.sequenceiq.ambari.services;

import com.sequenceiq.ambari.client.services.*;
import com.sequenceiq.ambari.domain.Cluster;
import com.sequenceiq.ambari.domain.Instance;
import com.sequenceiq.ambari.domain.InstanceState;
import com.sequenceiq.ambari.repository.InstanceRepository;
import com.sequenceiq.ambari.web.json.InstanceJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jiang on 15/11/18.
 */
@Service
public class InstanceService {
    @Autowired
    private InstanceRepository instanceRepository;


    public Instance findOne(long id){
        return instanceRepository.findOne(id);
    }

    public void remove(long id){
        instanceRepository.delete(id);
    }

    public void save(Instance instance){
        instanceRepository.save(instance);
    }

    public void save(List<Instance> instances){
        instanceRepository.save(instances);
    }

    public List<Instance> findAll(){
        return (List)instanceRepository.findAll();
    }

    public List<Instance> findAvailableInstances(long clusterId, int n){
        return findInstancesByState(InstanceState.STARTED, clusterId, n);
    }

    public List<Instance> findInstancesByState(InstanceState state, long clusterId, int n){
        return (List)instanceRepository.findNByState(state, clusterId, new PageRequest(0,n));
    }

    public void updateState(InstanceState state, List<Instance> instanceList) {
        if( instanceList.size()>0){
            for(Instance ins : instanceList){
                ins.setState(state);
                instanceRepository.save(ins);
            }
        }
    }
}
