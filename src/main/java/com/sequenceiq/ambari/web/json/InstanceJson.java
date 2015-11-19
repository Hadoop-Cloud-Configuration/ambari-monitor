package com.sequenceiq.ambari.web.json;

import com.sequenceiq.ambari.domain.InstanceState;

/**
 * Created by jiang on 15/11/17.
 */
public class InstanceJson {
    private long id;
    private String privateIP;

    private InstanceState state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public InstanceState getState() {
        return state;
    }

    public void setState(InstanceState state) {
        this.state = state;
    }

    public String getPrivateIP() {
        return privateIP;
    }

    public void setPrivateIP(String privateIp) {
        this.privateIP = privateIp;
    }
}
