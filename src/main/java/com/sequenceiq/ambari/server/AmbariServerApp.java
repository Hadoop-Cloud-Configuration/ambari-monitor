package com.sequenceiq.ambari.server;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

public class AmbariServerApp {
	
	public static void main(String[] args) throws ClientProtocolException, IOException, URISyntaxException {
		AmbariServerConfig serverConfig = new AmbariServerConfig();
		AmbariServerService ambServer = new AmbariServerService();
		AmbariAgentService ambAgent = new AmbariAgentService();
		
		/*********************** Process1: Install Agent on EC2 ***************************/
//		String agentRes = ambAgent.installAmbAgent(serverConfig.hosts, serverConfig.agentUrl, serverConfig.fileName);
//		System.out.println(agentRes);
//		String res0 = ambServer.httpGet(serverConfig.agentUrl + "/22");
//		System.out.println(res0);
		
		/************* Process2: Add a host and deploy components using APIs **************/
		String hostName = serverConfig.NEW_HOST_ADDED;
		String componentName = "DATANODE";
		// Step 1: Ensure the host is registered properly.
		String res1 = ambServer.getRegisteredHosts();
		System.out.println(res1);
		
		// Step 2: Add the host to the cluster
		ambServer.addHostToCluster(hostName);
		String res2 = ambServer.ensureAddHostToCluster(hostName);
		System.out.println("##2: " + res2);
		
		// Step 3: Add the necessary host components to the host
		String res3 = ambServer.addComponentToHost(componentName, hostName);
		
		// Step 4: Install the components
		String res4 = ambServer.installComponentToHost(componentName, hostName);
		System.out.println(res4);
		
		/**************** Process3: Last Step ******************/
//		// Step 5: Start the components, but can't run with previous step
//		String res = ambServer.startComponentOnHost(componentName, hostName);
//		System.out.println(res);
		
	}
}
