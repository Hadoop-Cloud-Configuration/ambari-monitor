package com.sequenceiq.ambari.server;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

public class AmbariServerApp {
	
	public static void main(String[] args) throws ClientProtocolException, IOException, URISyntaxException {
		AmbariServerConfig serverConfig = new AmbariServerConfig();
		AmbariServerService ambServer = new AmbariServerService();
		AmbariAgentService ambAgent = new AmbariAgentService();
		/*********************** Install Agent on EC2 ***************************/
//		String agentRes = ambAgent.installAmbAgent(serverConfig.hosts, serverConfig.agentUrl, serverConfig.fileName);
//		System.out.println(agentRes);
//		String res = ambServer.httpGet(serverConfig.agentUrl + "/21");
//		System.out.println(res);
		
		/************* Add a host and deploy components using APIs **************/
		String hostName = serverConfig.NEW_HOST_ADDED;
		String componentName = "DATANODE";
		ambServer.addHostToCluster(hostName);
		String res1 = ambServer.ensureAddHostToCluster(hostName);
		System.out.println("##1: " + res1);
		String res2 = ambServer.addComponentToHost(componentName, "DATANODE");
		System.out.println("##2: " + res2);
//		String res0 = ambServer.installComponentToHost(componentName, "DATANODE");
//		System.out.println(res0);
		
//		String res = ambServer.startComponentOnHost(componentName, hostName);
//		System.out.println(res);
		
//		String url = "http://54.172.166.250:8080/api/v1/clusters/hadoop/hosts/ip-172-31-59-88.ec2.internal/host_components/DATANODE";
//		String res4 = ambServer.myHttpPut(url);
//		System.out.println("##4: " + res4);
	}
}
