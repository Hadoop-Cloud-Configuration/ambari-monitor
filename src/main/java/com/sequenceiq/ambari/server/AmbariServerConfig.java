package com.sequenceiq.ambari.server;

import org.springframework.stereotype.Repository;


@Repository
public class AmbariServerConfig {
	public String username = "admin";
	public String password = "admin";
	public String AMBARI_SERVER_HOST = "54.85.181.100";
	public String AMBARI_SERVER_PORT = "8080";
	public String CLUSTER_NAME = "hadoop";
	public String NEW_HOST_ADDED = "ip-172-31-49-161.ec2.internal";
	public String[] hosts = {"ip-172-31-49-161.ec2.internal"};
	public String fileName = "/home/fred/Dropbox/Saves/pli5.pem";
	public String baseUrl = "http://" + AMBARI_SERVER_HOST + ":" + AMBARI_SERVER_PORT + "/api/v1";
	public String agentUrl = baseUrl + "/bootstrap";
//	RESTClient ambri = new AmbariClient(AMBARI_SERVER_HOST, "8080", "admin", "admin");
}
