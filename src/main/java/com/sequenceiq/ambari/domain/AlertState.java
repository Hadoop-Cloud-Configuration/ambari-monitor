package com.sequenceiq.ambari.domain;

/**
 * Created by jiang on 15/11/7.
 */
public enum  AlertState {
    OK("OK"),
    WARN("WARNING"),
    CRITICAL("CRITICAL");

    private String value;

    AlertState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
