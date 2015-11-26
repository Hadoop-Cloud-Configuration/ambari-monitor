package com.sequenceiq.ambari.server;

/**
 * Created by jiang on 15/11/22.
 */
public class AmbariScalingException extends RuntimeException  {
    public AmbariScalingException (String message) {
        super(message);
    }

    public AmbariScalingException(String message, Throwable cause) {
        super(message, cause);
    }

    public AmbariScalingException(Throwable cause) {
        super(cause);
    }
}
