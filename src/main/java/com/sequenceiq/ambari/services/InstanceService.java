package com.sequenceiq.ambari.services;

import com.sequenceiq.ambari.domain.Instance;
import com.sequenceiq.ambari.domain.InstanceState;
import com.sequenceiq.ambari.repository.InstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Iterable<Instance> findAllRunningInstances(){
        return instanceRepository.findByState(InstanceState.RUNNING);
    }
}
