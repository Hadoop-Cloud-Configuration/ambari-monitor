package com.sequenceiq.ambari.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AWSService {
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
}
