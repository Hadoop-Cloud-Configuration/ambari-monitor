package com.sequenceiq.ambari.web.controller;

import com.sequenceiq.ambari.domain.Cluster;
import com.sequenceiq.ambari.domain.MetricsAlert;
import com.sequenceiq.ambari.services.AlertService;
import com.sequenceiq.ambari.web.json.MetricsAlertJson;
import com.sequenceiq.ambari.web.mapper.MetricsAlertMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jiang on 15/11/7.
 */
@RestController
@RequestMapping("/clusters/{clusterId}/alerts")
public class AlertController {
    @Autowired
    private AlertService alertService;
    @Autowired
    private MetricsAlertMapper metricsAlertMapper;

    @RequestMapping(value = "/metric", method = RequestMethod.POST)
    public ResponseEntity<MetricsAlertJson> createAlerts(@PathVariable long clusterId, @RequestBody MetricsAlertJson json) {
            MetricsAlert metricAlert = metricsAlertMapper.map(json, MetricsAlert.class);
            return createMetricAlarmResponse(alertService.createMetricsAlert(clusterId, metricAlert), HttpStatus.CREATED);
    }


    @RequestMapping(value = "/metric", method = RequestMethod.GET)
    public ResponseEntity<List<MetricsAlertJson>> createAlerts(@PathVariable long clusterId) {
        List<MetricsAlert> alerts = alertService.findMetricsAlertsByClusterId(clusterId);
        return new ResponseEntity<List<MetricsAlertJson>>(metricsAlertMapper.mapAsList(alerts, MetricsAlertJson.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/metric/{alertId}", method = RequestMethod.DELETE)
    public ResponseEntity<MetricsAlertJson> createAlerts(@PathVariable long clusterId, @PathVariable long alertId) {
        alertService.removeMetricsAlert(clusterId, alertId);
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<MetricsAlertJson> createMetricAlarmResponse( MetricsAlert alert, HttpStatus status){
        return new ResponseEntity<MetricsAlertJson>(metricsAlertMapper.map(alert, MetricsAlertJson.class), status);
    }
}
