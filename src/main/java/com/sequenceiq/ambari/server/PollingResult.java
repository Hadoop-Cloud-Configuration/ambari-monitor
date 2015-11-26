package com.sequenceiq.ambari.server;

/**
 * Created by jiang on 15/11/22.
 */
public enum PollingResult {
    TIMEOUT, EXIT, SUCCESS, FAILURE;

    public static boolean isSuccess(PollingResult pollingResult) {
        return PollingResult.SUCCESS.equals(pollingResult);
    }

    public static boolean isExited(PollingResult pollingResult) {
        return PollingResult.EXIT.equals(pollingResult);
    }

    public static boolean isTimeout(PollingResult pollingResult) {
        return PollingResult.TIMEOUT.equals(pollingResult);
    }

    public static boolean isFailure(PollingResult pollingResult) {
        return PollingResult.FAILURE.equals(pollingResult);
    }

}
