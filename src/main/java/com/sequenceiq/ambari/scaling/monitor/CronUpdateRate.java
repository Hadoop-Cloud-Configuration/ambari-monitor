package com.sequenceiq.ambari.scaling.monitor;

/**
 * Created by jiang on 15/11/5.
 */
public final class CronUpdateRate {
    /**
     * Every 30 seconds.
     */
    public static final String METRIC_UPDATE_RATE_CRON = "0/10 * * * * ?";

    /**
     * Every 10 seconds.
     */
    public static final String TIME_UPDATE_RATE_CRON = "0/1 * * * * ?";

    /**
     * Time update rate in ms, aligned to the cron expression.
     */
    public static final int CLUSTER_UPDATE_RATE = 10_000;

    private CronUpdateRate() {
        throw new IllegalStateException();
    }
}
