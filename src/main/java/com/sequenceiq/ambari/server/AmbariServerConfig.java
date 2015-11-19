package com.sequenceiq.ambari.server;

import org.springframework.stereotype.Repository;


@Repository
public class AmbariServerConfig {
	public String username = "admin";
	public String password = "admin";
	public String AMBARI_SERVER_HOST = "54.172.65.114";
	public String AMBARI_SERVER_PORT = "8080";
	public String CLUSTER_NAME = "hadoop";
	public String NEW_HOST = "ip-172-31-17-220.ec2.internal";
	public String[] hosts = {"ip-172-31-17-220.ec2.internal"};
	public String[] allHosts = {};
	public String[] componentNames = {"DATANODE", "NODEMANAGER"};  // NODEMANAGER, DATANODE, METRICS_MONITOR
	public String fileName = "/home/fred/Dropbox/Saves/pli5.pem";
	public String baseUrl = "http://" + AMBARI_SERVER_HOST + ":" + AMBARI_SERVER_PORT + "/api/v1";
	public String agentUrl = baseUrl + "/bootstrap";
	public String createCommand = "./aws_linux create -key pli5 -id ami-02dc4c6b";
}
