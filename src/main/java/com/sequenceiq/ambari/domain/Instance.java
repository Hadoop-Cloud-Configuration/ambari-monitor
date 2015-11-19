package com.sequenceiq.ambari.domain;

import javax.persistence.*;

/**
 * Created by jiang on 15/11/17.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Instance.findByState", query = "SELECT c FROM Instance c WHERE c.state= :state"),
        @NamedQuery(name = "Instance.findOne", query = "SELECT c FROM Instance c WHERE c.id= :id")
})
public class Instance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String privateIP;

    private InstanceState state;


    public Instance(){

    }

    public Instance(String ip){
        this.privateIP = ip;
        this.state = InstanceState.RUNNING;
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

    public void setPrivateIP(String privateIP) {
        this.privateIP = privateIP;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



}
