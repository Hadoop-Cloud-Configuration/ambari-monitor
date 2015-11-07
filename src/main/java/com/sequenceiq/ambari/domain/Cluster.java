package com.sequenceiq.ambari.domain;


import com.sequenceiq.ambari.repository.ClusterState;

import javax.persistence.*;

/**
 * Created by jiang on 15/11/4.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Cluster.findByState", query = "SELECT c FROM Cluster c WHERE c.state= :state")
})
public class Cluster {
    private final static int MAX_SIZE =100;
    private final static int MIN_SIZE = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    private ClusterState state = ClusterState.RUNNING;

    private String host;

    private String port;

    public Cluster(){

    }

    public long getId(){
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ClusterState getState() {
        return state;
    }

    public void setState(ClusterState state) {
        this.state = state;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
