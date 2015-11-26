package com.sequenceiq.ambari.web.controller;

import com.sequenceiq.ambari.client.AmbariClient;
import com.sequenceiq.ambari.domain.Cluster;
import com.sequenceiq.ambari.domain.Instance;
import com.sequenceiq.ambari.domain.InstanceState;
import com.sequenceiq.ambari.factory.AmbariClientFactory;
import com.sequenceiq.ambari.factory.InstanceFactory;
import com.sequenceiq.ambari.services.ClusterService;
import com.sequenceiq.ambari.services.InstanceService;
import com.sequenceiq.ambari.web.json.InstanceJson;
import com.sequenceiq.ambari.web.mapper.InstanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiang on 15/11/17.
 */
@RestController
@RequestMapping("/clusters/{clusterId}/instances")
public class InstancesController {

    @Autowired
    private InstanceFactory instanceFactory;
    @Autowired
    private InstanceMapper instanceMapper;
    @Autowired
    private InstanceService instanceService;
    @Autowired
    private ClusterService clusterService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<List<InstanceJson>> addInstances( @PathVariable long clusterId, @RequestBody List<InstanceJson> jsons) {
        List<Instance> instances = new ArrayList<>();
        Cluster cluster = clusterService.findOne(clusterId);
        AmbariClient client = AmbariClientFactory.createAmbariClient(cluster);
        List<String> hostGroup = client.getClusterHosts();
        for(InstanceJson json : jsons){
            InstanceState state = hostGroup.contains(json.getPrivateIP()) ? InstanceState.RUNNING : InstanceState.STARTED;
            Instance ins = instanceFactory.createInstance(json.getPrivateIP(), state);
            ins.setCluster(cluster);
            instances.add(ins);
            instanceService.save(instances);
        }

        cluster.addInstanceList(instances);
        clusterService.save(cluster);
        return new ResponseEntity<>(instanceMapper.mapAsList(instances, InstanceJson.class), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<InstanceJson>> getInstances() {
        List<Instance> instances = instanceService.findAll();
        return new ResponseEntity<>(instanceMapper.mapAsList(instances, InstanceJson.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/{instanceId}", method = RequestMethod.GET)
    public ResponseEntity<InstanceJson> getInstance( @PathVariable long instanceId) {
        return createInstanceJsonResponse(instanceService.findOne( instanceId));
    }

    @RequestMapping(value = "/{instanceId}", method = RequestMethod.DELETE)
    public ResponseEntity<InstanceJson> deleteInstance( @PathVariable long instanceId) {
         instanceService.remove( instanceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private ResponseEntity<InstanceJson> createInstanceJsonResponse(Instance instance){
        return createInstanceJsonResponse(instance, HttpStatus.OK);
    }

    private ResponseEntity<InstanceJson> createInstanceJsonResponse(Instance instance, HttpStatus status){
        return new ResponseEntity<InstanceJson>(instanceMapper.map(instance, InstanceJson.class),status);
    }

}
