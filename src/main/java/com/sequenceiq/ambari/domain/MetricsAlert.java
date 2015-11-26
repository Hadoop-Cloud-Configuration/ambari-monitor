package com.sequenceiq.ambari.domain;


import javax.persistence.*;

/**
 * Created by jiang on 15/11/7.
 */
@Entity
@Table(name="metrics_alert")
@NamedQueries({
        @NamedQuery(name="MetricsAlert.findAllByCluster",  query="SELECT c FROM MetricsAlert c WHERE c.cluster.id= :clusterId"),
        @NamedQuery(name="MetricsAlert.findByCluster",  query="SELECT c FROM MetricsAlert c WHERE c.cluster.id= :clusterId  AND id= :alertId")

})
public class MetricsAlert implements Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Cluster cluster;

    @Column(name = "name")
    private String name;

    @Column(name = "alert_state")
    private AlertState alertState;

    @Column(name="definition_name")
    private String definitionName;
    @Column(name="time_definition")
    private int timeDefinition;
    @Column(name="scaling_policy")
    private ScalingType scalingPolicy;

    public MetricsAlert() {
    }

    public ScalingType getScalingPolicy() {
        return scalingPolicy;
    }

    public void setScalingPolicy(ScalingType scalingPolicy) {
        this.scalingPolicy = scalingPolicy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCluster(Cluster cluster){
        this.cluster = cluster;
    }

    public Cluster getCluster(){
        return cluster;
    }

    public AlertState getAlertState() {
        return alertState;
    }

    public void setAlertState(AlertState alertState) {
        this.alertState = alertState;
    }

    public String getDefinitionName() {
        return definitionName;
    }

    public void setDefinitionName(String definitionName) {
        this.definitionName = definitionName;
    }

    public int getTimeDefinition() {
        return timeDefinition;
    }

    public void setTimeDefinition(int timeDefinition){
        this.timeDefinition = timeDefinition;
    }
}
