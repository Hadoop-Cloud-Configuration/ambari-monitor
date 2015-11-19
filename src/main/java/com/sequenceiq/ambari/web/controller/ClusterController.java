package com.sequenceiq.ambari.web.controller;

import com.sequenceiq.ambari.domain.Ambari;
import com.sequenceiq.ambari.domain.Cluster;
import com.sequenceiq.ambari.factory.AmbariFactory;
import com.sequenceiq.ambari.services.ClusterService;
import com.sequenceiq.ambari.web.mapper.AmbariMapper;
import com.sequenceiq.ambari.web.mapper.ClusterMapper;
import com.sequenceiq.ambari.web.json.AmbariJson;
import com.sequenceiq.ambari.web.json.ClusterJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
    private ClusterMapper clusterMapper;
    @Autowired
    private AmbariFactory ambariFactory;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ClusterJson> addCluster( @RequestBody AmbariJson ambariServer) {
        return setCluster( ambariServer, null);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ClusterJson>> getClusters( ) {
        List<Cluster> clusters = clusterService.findActiveCluster();
        return new ResponseEntity<>(clusterMapper.mapAsList(clusters, ClusterJson.class),HttpStatus.OK);
    }

    @RequestMapping(value = "/{clusterId}", method = RequestMethod.GET)
    public ResponseEntity<ClusterJson> getCluster( @PathVariable long clusterId) {
        return createClusterJsonResponse(clusterService.findOne(clusterId));
    }

    @RequestMapping(value = "/{clusterId}", method = RequestMethod.DELETE)
    public ResponseEntity<ClusterJson> deleteCluster( @PathVariable long clusterId) {
        clusterService.remove(clusterId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private ResponseEntity<ClusterJson> createClusterJsonResponse(Cluster cluster) {
        return new ResponseEntity<>(clusterMapper.map(cluster, ClusterJson.class), HttpStatus.OK);
    }
    private ResponseEntity<ClusterJson> createClusterJsonResponse(Cluster cluster, HttpStatus status) {
        return new ResponseEntity<>(clusterMapper.map(cluster, ClusterJson.class), status);
    }

    private ResponseEntity<ClusterJson> setCluster( AmbariJson json, Long clusterId) {
        Ambari ambari = ambariFactory.createAmbari(json.getHost(), json.getPort(), json.getUser(),json.getPass());
        Cluster cluster = clusterService.create(ambari);
        return createClusterJsonResponse(cluster);
    }


}
