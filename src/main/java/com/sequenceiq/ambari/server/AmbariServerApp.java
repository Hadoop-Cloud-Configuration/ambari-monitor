package com.sequenceiq.ambari.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;

public class AmbariServerApp {
	AmbariServerConfig serverConfig = new AmbariServerConfig();
	AmbariServerService ambServer = new AmbariServerService();
	AmbariAgentService ambAgent = new AmbariAgentService();
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		AmbariServerConfig serverConfig = new AmbariServerConfig();
		AmbariServerService ambServer = new AmbariServerService();
		AmbariAgentService ambAgent = new AmbariAgentService();
		AmbariServerApp demo = new AmbariServerApp();
		String hostName = serverConfig.NEW_HOST_ADDED;
		String[] componentNames = {"DATANODE", "NODEMANAGER"};  // NODEMANAGER, DATANODE, METRICS_MONITOR 
		
		int flag = 4; // switch process
		// start EC2 instance
		if (flag == 0) {
//			serverConfig.NEW_HOST_ADDED = demo.getHostName(serverConfig.createCommand);
//			serverConfig.hosts[0] = serverConfig.NEW_HOST_ADDED;
//			System.out.println(serverConfig.NEW_HOST_ADDED);
			demo.operateInstance("stop", "i-82ab733c");
		}
//		demo.sleep(20);
		if (flag == 1) {
			String agentRes = ambAgent.installAmbAgent(serverConfig.hosts, serverConfig.agentUrl, serverConfig.fileName);
			System.out.println(agentRes);
//			String res0 = ambServer.httpGet(serverConfig.agentUrl + "/4");
//			System.out.println(res0);
		}
		if (flag == 2) {
			demo.startHostService(componentNames, hostName);
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
	 * Start and Stop EC2 instance
	 * @param method
	 * @param instanceId
	 */
	public void operateInstance(String method, String instanceId) {
        Process proc;
        String command = "./aws_linux " + method +" -instance " + instanceId;
        try {
			proc = Runtime.getRuntime().exec(command);
			proc.waitFor();
	        System.out.println ("exit: " + proc.exitValue());
	        proc.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Get hostname from AWS
	 * @param command
	 * @return
	 */
	public String getHostName(String command) {
		String res;
        Process proc;
        String hostName = "";
        try {
//          proc = Runtime.getRuntime().exec("ls -aF"); // aws_linux stop -instance i-f270a94c
        	proc = Runtime.getRuntime().exec(command); //aws_linux create -key pli5
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            while ((res = br.readLine()) != null) {
            	res = br.readLine();
            	if (res.contains("PrivateDnsName")) {
            		String[] str = res.split("\"");
            		hostName = str[1];
            		System.out.println("hostName: " + str[1]);
            		break;
            	}
            }
            proc.waitFor();
            System.out.println ("exit: " + proc.exitValue());
            proc.destroy();
        } catch (Exception e) {}
        
        return hostName;
	}
	
	/**
	 * Start host service
	 * @param componentNames
	 * @param hostName
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void startHostService(String[] componentNames, String hostName) throws ClientProtocolException, IOException {
		// install components on host
		for (String component: componentNames) {
			installComponentOnHost(component, hostName);
			startComponentOnHost(component, hostName);
		}
		
		System.out.println("Start Host Successful.");
	}
	
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
	 * Stop host service
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
