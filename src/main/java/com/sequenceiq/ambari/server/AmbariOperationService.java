package com.sequenceiq.ambari.server;

import com.sequenceiq.ambari.client.AmbariClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by jiang on 15/11/22.
 */
@Service
public class AmbariOperationService  {

    public static final int MAX_ATTEMPTS_FOR_AMBARI_OPS = -1;
    public static final int AMBARI_POLLING_INTERVAL = 5000;
    public static final int MAX_ATTEMPTS_FOR_HOSTS = 240;
    public static final int MAX_FAILURE_COUNT = 5;
    private static final Logger LOGGER = LoggerFactory.getLogger(AmbariOperationService.class);

    @Autowired
    private AmbariOperationsCheckerTask ambariOperationsStatusCheckerTask;
    @Autowired
    private PollingService<AmbariOperations> operationsPollingService;

    public PollingResult waitForAmbariOperations( AmbariClient ambariClient, Map<String, Integer> operationRequests) {
        LOGGER.info("Waiting for Ambari operations to finish. [Operation requests: {}]", operationRequests);
        return waitForAmbariOperations( ambariClient, ambariOperationsStatusCheckerTask, operationRequests);
    }

    public PollingResult waitForAmbariOperations( AmbariClient ambariClient, StatusCheckerTask task, Map<String, Integer> operationRequests) {
        return operationsPollingService.pollWithTimeout(task, new AmbariOperations(ambariClient, operationRequests),
                AMBARI_POLLING_INTERVAL, MAX_ATTEMPTS_FOR_AMBARI_OPS, MAX_FAILURE_COUNT);
    }
}
