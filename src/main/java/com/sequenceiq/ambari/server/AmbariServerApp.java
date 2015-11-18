package com.sequenceiq.ambari.server;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;

public class AmbariServerApp {
	static AmbariServerConfig serverConfig = new AmbariServerConfig();
	AmbariServerService ambServer = new AmbariServerService();
	static AmbariAgentService ambAgent = new AmbariAgentService();
	static AWSService awsService = new AWSService();
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		AmbariServerApp demo = new AmbariServerApp();
		String hostName = serverConfig.NEW_HOST;
		String[] componentNames = serverConfig.componentNames;  // NODEMANAGER, DATANODE, METRICS_MONITOR 
		
		int flag = 2; // switch process
		// start EC2 instance
		if (flag == 0) {
			serverConfig.NEW_HOST = awsService.getHostName(serverConfig.createCommand);
//			serverConfig.hosts[0] = serverConfig.NEW_HOST;
//			System.out.println(serverConfig.NEW_HOST);
//			demo.operateInstance("start", "i-82ab733c");
		}
//		demo.sleep(20);
		if (flag == 1) {
			String agentRes = ambAgent.installAmbAgent(serverConfig.hosts, serverConfig.agentUrl, serverConfig.fileName);
			System.out.println(agentRes);
//			String res0 = ambServer.httpGet(serverConfig.agentUrl + "/4");
//			System.out.println(res0);
		}
		if (flag == 2) {
			demo.addHostService(componentNames, hostName);
		}
		if (flag == 3) {
			demo.stopHostService(componentNames, hostName);
		}
	}
	
	public void sleep(int second) {
		try {
			TimeUnit.SECONDS.sleep(second);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * add host service scale out hadoop cluster
	 * @param componentNames
	 * @param hostName
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void addHostService(String[] componentNames, String hostName) throws ClientProtocolException, IOException {
		// install components on host
		for (String component: componentNames) {
			installComponentOnHost(component, hostName);
			startComponentOnHost(component, hostName);
		}
		
		System.out.println("Start Host Successful.");
	}
	
	/**
	 * Install components on Host
	 * @param componentName
	 * @param hostName
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public int installComponentOnHost(String componentName, String hostName) throws ClientProtocolException, IOException {
		ambServer.getRegisteredHosts();
		
		// Step 2: Add the host to the cluster
		ambServer.addHostToCluster(hostName);
		ambServer.ensureAddHostToCluster(hostName);
		
		// Step 3: Add the necessary host components to the host
		ambServer.addComponentToHost(componentName, hostName);
		
		// Step 4: Install the components
		ambServer.installComponentToHost(componentName, hostName);
		
		return 0;
	}
	
	/**
	 * Start Component Services on Host
	 * @param componentName
	 * @param hostName
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public int startComponentOnHost(String componentName, String hostName) throws ClientProtocolException, IOException {
		int code = ambServer.startComponentOnHost(componentName, hostName);
		while (code != 202) {
			try {
				TimeUnit.SECONDS.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			code = ambServer.startComponentOnHost(componentName, hostName);
		}
		return code;
	}
	
	/**
	 * Stop host services, remove components and host from cluster
	 * @param componentNames
	 * @param hostName
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void stopHostService(String[] componentNames, String hostName) throws ClientProtocolException, IOException {
		// stop and delete components on host
		for (String component: componentNames) {
			stopComponentOnHost(component, hostName);
			deleteComponentOnHost(component, hostName);
		}
		// delete components on host
		int code = deleteHost(hostName);
		System.out.println("Stop Host Service Successful." + code);
	}
	
	public int stopComponentOnHost(String componentName, String hostName) throws ClientProtocolException, IOException {
		int code = ambServer.stopComponentOnHost(componentName, hostName);
		return code;
	}
	
	/**
	 * delete components on host
	 * @param componentName
	 * @param hostName
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public int deleteComponentOnHost(String componentName, String hostName) throws ClientProtocolException, IOException {
		int code = ambServer.deleteComponentOnHost(componentName, hostName);
		while (code != 200) {
			try {
				TimeUnit.SECONDS.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			code = ambServer.deleteComponentOnHost(componentName, hostName);
		}
		return code;
	}
	
	/**
	 * Detele host
	 * @param hostName
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public int deleteHost(String hostName) throws ClientProtocolException, IOException {
		int code = ambServer.deleteHost(hostName);
		while (code != 200) {
			try {
				TimeUnit.SECONDS.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			code = code = ambServer.deleteHost(hostName);
		}
		return code;
	}
}
