package com.sequenceiq.ambari.server;

/**
 * Created by jiang on 15/11/22.
 */
public interface StatusCheckerTask<T> {

    boolean checkStatus(T t);

    void handleTimeout(T t);

    String successMessage(T t);

    boolean exitPolling(T t);

    void handleException(Exception e);
}