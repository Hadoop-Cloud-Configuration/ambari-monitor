package com.sequenceiq.ambari.web.controller;

import com.sequenceiq.ambari.domain.Instance;
import com.sequenceiq.ambari.factory.InstanceFactory;
import com.sequenceiq.ambari.services.InstanceService;
import com.sequenceiq.ambari.web.json.InstanceJson;
import com.sequenceiq.ambari.web.mapper.InstanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jiang on 15/11/17.
 */
@RestController
@RequestMapping("/instances")
public class InstancesController {

    @Autowired
    private InstanceFactory instanceFactory;
    @Autowired
    private InstanceMapper instanceMapper;
    @Autowired
    private InstanceService instanceService;

    //@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<InstanceJson> addInstance( @RequestBody InstanceJson json) {
        Instance instance =  instanceFactory.createInstance(json.getPrivateIP());
        instanceService.save(instance);
        return createInstanceJsonResponse(instance);
    }
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<List<InstanceJson>> addInstances( @RequestBody List<InstanceJson> jsons) {
        List<Instance> instances = new ArrayList<>();
        for(InstanceJson json : jsons){
            instances.add(instanceFactory.createInstance(json.getPrivateIP()));
        }
        instanceService.save(instances);
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
