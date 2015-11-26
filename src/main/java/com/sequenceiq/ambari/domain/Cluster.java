package com.sequenceiq.ambari.domain;


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
    public final static int MAX_SIZE =100;
    public final static int MIN_SIZE = 2;
    private final static int DEFAULT_OPERATION_INTERVAL = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    private ClusterState state = ClusterState.RUNNING;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Ambari ambari;

    @OneToMany(mappedBy = "cluster", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MetricsAlert> metricsAlerts = new ArrayList<>();
    @OneToMany(mappedBy = "cluster", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Instance> instances = new ArrayList<>();
    @Column(name="operation_interval")
    private int operationInterval = DEFAULT_OPERATION_INTERVAL;
    @Column(name="latest_scaling_activity")
    private long latestScalingActivity;

    public Cluster(){

    }

    public Cluster(Ambari ambari){
        this.ambari = ambari;
    }

    public void addMetricsAlert(MetricsAlert alert){
        this.metricsAlerts.add(alert);
    }

    public void addInstance(Instance instance){
        this.instances.add(instance);
    }

    public void addInstanceList(List<Instance> instances){
        this.instances.addAll(instances);
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


    public int getOperationInterval() {
        return operationInterval;
    }

    public void setOperationInterval(int interval ){
        this.operationInterval = interval;
    }

    public long getLatestScalingActivity() {
        return latestScalingActivity;
    }

    public void setLatestScalingActivity() {
        this.latestScalingActivity = System.currentTimeMillis();
    }
}
