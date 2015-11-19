package com.sequenceiq.ambari.domain;


import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiang on 15/11/4.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Cluster.findByState", query = "SELECT c FROM Cluster c WHERE c.state= :state"),
        @NamedQuery(name = "Cluster.findOne", query = "SELECT c FROM Cluster c WHERE c.id= :id")
})
public class Cluster {
    private final static int MAX_SIZE =100;
    private final static int MIN_SIZE = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    private ClusterState state = ClusterState.RUNNING;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Ambari ambari;

    @OneToMany(mappedBy = "cluster", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MetricsAlert> metricsAlerts = new ArrayList<>();

    public Cluster(){

    }

    public Cluster(Ambari ambari){
        this.ambari = ambari;
    }

    public void addMetricsAlert(MetricsAlert alert){
        this.metricsAlerts.add(alert);
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
        return ambari.getHost();
    }

    public String getAmbariUser(){
        return ambari.getUser();
    }

    public String getAmbariPss(){
        return ambari.getPass();
    }

    public String getPort(){
        return ambari.getPort();
    }


}
