package com.sequenceiq.ambari.web.controller;

import com.sequenceiq.ambari.domain.Cluster;
import com.sequenceiq.ambari.services.ClusterService;
import com.sequenceiq.ambari.web.mapper.ClusterJsonMapper;
import com.sequenceiq.ambari.web.json.AmbariJson;
import com.sequenceiq.ambari.web.json.ClusterJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by jiang on 15/11/6.
 */
@Controller
@RequestMapping("/clusters")
public class ClusterController {
    @Autowired
    private ClusterService clusterService;
    @Autowired
    private ClusterJsonMapper clusterJsonMapper;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ClusterJson> addCluster( @RequestBody AmbariJson ambariServer) {
        return setCluster( ambariServer, null);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ClusterJson>> getClusters( ) {
        List<Cluster> clusters = clusterService.findActiveCluster();
        return new ResponseEntity<>(clusterJsonMapper.mapAsList(clusters, ClusterJson.class), HttpStatus.OK);
    }

    private ResponseEntity<ClusterJson> createClusterJsonResponse(Cluster cluster, HttpStatus status) {
        return new ResponseEntity<>(clusterJsonMapper.map(cluster, ClusterJson.class), status);
    }

    private ResponseEntity<ClusterJson> setCluster( AmbariJson json, Long clusterId) {
        Cluster cluster = clusterService.create();
        return createClusterJsonResponse(cluster, HttpStatus.OK);
    }


}
