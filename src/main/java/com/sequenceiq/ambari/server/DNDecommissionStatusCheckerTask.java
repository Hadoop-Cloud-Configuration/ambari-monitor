package com.sequenceiq.ambari.server;

/**
 * Created by jiang on 15/11/23.
 */
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sequenceiq.ambari.client.AmbariClient;

@Component
public class DNDecommissionStatusCheckerTask implements StatusCheckerTask<AmbariOperations> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DNDecommissionStatusCheckerTask.class);

    @Override
    public boolean checkStatus(AmbariOperations t) {
        AmbariClient ambariClient = t.getAmbariClient();
        Map<String, Long> dataNodes = ambariClient.getDecommissioningDataNodes();
        boolean finished = dataNodes.isEmpty();
        if (!finished) {
            LOGGER.info("DataNode decommission is in progress: {}", dataNodes);
        }
        return finished;
    }

    @Override
    public void handleTimeout(AmbariOperations t) {
        throw new IllegalStateException("DataNode decommission timed out");

    }

    @Override
    public String successMessage(AmbariOperations t) {
        return "Requested DataNode decommission operations completed";
    }

    @Override
    public boolean exitPolling(AmbariOperations ambariOperations) {
        return false;
    }

    @Override
    public void handleException(Exception e) {

    }

}
