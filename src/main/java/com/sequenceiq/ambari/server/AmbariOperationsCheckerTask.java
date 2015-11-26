package com.sequenceiq.ambari.server;

import com.sequenceiq.ambari.client.AmbariClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;


/**
 * Created by jiang on 15/11/22.
 */
@Component
public class AmbariOperationsCheckerTask implements StatusCheckerTask<AmbariOperations> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmbariOperationsCheckerTask.class);

    private static final BigDecimal COMPLETED = new BigDecimal(100.0);
    private static final BigDecimal FAILED = new BigDecimal(-1.0);
    private static final int MAX_RETRY = 3;

    @Override
    public boolean checkStatus(AmbariOperations ambariOperations) {
        Map<String, Integer> installRequests = ambariOperations.getRequests();
        boolean allFinished = true;
        for (Map.Entry<String, Integer> request : installRequests.entrySet()) {
            AmbariClient ambariClient = ambariOperations.getAmbariClient();
            BigDecimal installProgress = ambariClient.getRequestProgress(request.getValue());
            LOGGER.info("Ambari operation: '{}', Progress: {}", request.getKey(), installProgress);
            allFinished = allFinished && installProgress != null && COMPLETED.compareTo(installProgress) == 0;
            if (installProgress != null && FAILED.compareTo(installProgress) == 0) {
                boolean failed = true;
                for (int i = 0; i < MAX_RETRY; i++) {
                    if (ambariClient.getRequestProgress(request.getValue()).compareTo(FAILED) != 0) {
                        failed = false;
                        break;
                    }
                }
                if (failed) {

                    throw new AmbariScalingException(String.format("Ambari operation failed: [component: '%s', requestID: '%s']", request.getKey(),
                            request.getValue()));
                }
            }
        }
        return allFinished;
    }

    @Override
    public void handleTimeout(AmbariOperations ambariOperations) {
        throw new IllegalStateException(String.format("Ambari operations timed out: %s", ambariOperations.getRequests()));
    }

    @Override
    public String successMessage(AmbariOperations ambariOperations) {
        return String.format("Requested Ambari operations completed: %s", ambariOperations.getRequests().toString());
    }

    @Override
    public boolean exitPolling(AmbariOperations ambariOperations) {
        return false;
    }

    @Override
    public void handleException(Exception e) {
        LOGGER.error("Ambari operation failed.", e);
    }
}
