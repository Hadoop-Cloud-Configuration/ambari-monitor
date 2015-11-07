package com.sequenceiq.ambari.server;
 /**
  * 
  * @author fred
  *
  */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Scanner;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@Repository
public class AmbariServerService {
	@Autowired
	private AmbariServerConfig serverConfig = new AmbariServerConfig();
	
	public String deployComponetsInHost( String componentName, String hostName) throws ClientProtocolException, IOException {
		addHostToCluster(hostName);
		String res1 = ensureAddHostToCluster(hostName);
		System.out.println("##1: " + res1);
		String res2 = addComponentToHost(componentName, hostName);
		System.out.println("##2: " + res2);
		String res3 = installComponentToHost(componentName, hostName);
		System.out.println("##3: " + res3);
		String res4 = startComponentOnHost(componentName, hostName);
		System.out.println("##4: " + res4);
		restartNagiosOnHost();
		
		return "Done";
	}
	
	/**
	 * Add the host to the cluster.
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String addHostToCluster(String hostName) throws ClientProtocolException, IOException {
		String url = serverConfig.baseUrl + "/clusters/" + serverConfig.CLUSTER_NAME + "/hosts/" + hostName;
		String result = httpPost(url);
        return result;
	}
	
	public String ensureAddHostToCluster(String hostName) throws ClientProtocolException, IOException {
		String url = serverConfig.baseUrl + "/clusters/" + serverConfig.CLUSTER_NAME + "/hosts/" + hostName;
		String result = httpGet(url);
        return result;
	}
	
	/**
	 * Add the necessary host components to the host.
	 * @param componentName
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */ 
	public String addComponentToHost(String componentName, String hostName) throws ClientProtocolException, IOException {
		String url = serverConfig.baseUrl + "/clusters/" + serverConfig.CLUSTER_NAME + "/hosts/" + hostName + "/host_components/" + componentName;
		String result = httpPost(url);
        return result;
	}
	
	/**
	 * Install the components
	 * @param componentName
	 * @param hostName
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String installComponentToHost(String componentName, String hostName) throws ClientProtocolException, IOException {
		String url = serverConfig.baseUrl + "/clusters/" + serverConfig.CLUSTER_NAME + "/hosts/" + hostName + "/host_components/" + componentName;
//		http://54.172.166.250:8080/api/v1/clusters/hadoop/hosts/ip-172-31-59-88.ec2.internal/host_components/DATANODE
		HttpClient client = HttpClientBuilder.create().build();
    	HttpPut request =  (HttpPut) httpRequestService("PUT", url);
    	String body = new String("{\"HostRoles\": {\"state\": \"INSTALLED\"}}");
    	StringEntity entity = new StringEntity(body);
    	request.setEntity(entity);
        HttpResponse response = client.execute(request);
        
        String result = EntityUtils.toString(response.getEntity());
        return result;
    }
	
	/**
	 * Start the components
	 * @param componentName
	 * @param hostName
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String startComponentOnHost(String componentName, String hostName) throws ClientProtocolException, IOException {
		String url = serverConfig.baseUrl + "/clusters/" + serverConfig.CLUSTER_NAME + "/hosts/" + hostName + "/host_components/" + componentName;
		
		HttpClient client = HttpClientBuilder.create().build();
    	HttpPut request =  (HttpPut) httpRequestService("PUT", url);
    	String body = new String("{\"HostRoles\": {\"state\": \"STARTED\"}}");
    	StringEntity entity = new StringEntity(body);
    	request.setEntity(entity);
        HttpResponse response = client.execute(request);
        
        String result = EntityUtils.toString(response.getEntity());
        return result;
    }
	
	/**
	 * Restart Nagios
	 * Nagios server may need to be restarted to ensure it picks up the new host and reports its status.
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String restartNagiosOnHost() throws ClientProtocolException, IOException {
		String url = serverConfig.baseUrl + "/clusters/" + serverConfig.CLUSTER_NAME + "/services/" + "NAGIOS";
		HttpClient client = HttpClientBuilder.create().build();
    	HttpPut request =  (HttpPut) httpRequestService("PUT", url);
    	// stop process
    	String stop = new String("{\"RequestInfo\": {\"context\": \"Stop Nagios\"}, \"ServiceInfo\": {\"state\": \"INSTALLED\"}}");
    	StringEntity stopEntity = new StringEntity(stop);
    	request.setEntity(stopEntity);
        HttpResponse stopResponse = client.execute(request);
        // start process
        String start = new String("{\"RequestInfo\": {\"context\": \"Stop Nagios\"}, \"ServiceInfo\": {\"state\": \"INSTALLED\"}}");
    	StringEntity startEntity = new StringEntity(stop);
    	request.setEntity(startEntity);
        HttpResponse startResponse = client.execute(request);
        
        String result = EntityUtils.toString(startResponse.getEntity());
        return result;
    }
	
	/**
	 * Provide authorized http GET, POST, PUT request methods
	 * @param method
	 * @param url
	 * @return
	 */
	public HttpRequest httpRequestService(String method, String url) {
		HttpRequest request = null;
		if (method.equals("GET")) {
			request = new HttpGet(url);
		} else if (method.equals("POST")) {
			request = new HttpPost(url);
		} else if (method.equals("PUT")) {
			request = new HttpPut(url);
		}
		String authStr = serverConfig.username + ":" + serverConfig.password;
        String authEncoded = Base64.encode(authStr.getBytes());
    	request.addHeader("Authorization", "Basic " + authEncoded);
    	request.addHeader("X-Requested-By", "ambari");
    	
    	return request;
	}
	
	/** 
	 * Find bugs
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String myHttpPut(String url) throws ClientProtocolException, IOException {
		System.out.println(url);
		HttpClient client = HttpClientBuilder.create().build();
//    	HttpPut request = new HttpPut(url);
		HttpPut request = (HttpPut) httpRequestService("PUT", url);

//    	JSONObject injson = new JSONObject();
//    	injson.put("state", "INSTALLED");
//    	JSONObject outjson = new JSONObject();
//    	outjson.put("HostRoles", injson);
//    
//    	
//    	StringWriter out = new StringWriter();
//    	outjson.writeJSONString(out);
//        String jsonText = out.toString();
//    	StringEntity entity = new StringEntity(jsonText);
//    	request.setEntity(entity);
    	
//    	String start = new String("{\"HostRoles\": {\"state\": \"STARTED\"}}");
    	String start = new String("{\"HostRoles\": {\"state\": \"INSTALLED\"}}");
    	StringEntity entity = new StringEntity(start);
    	request.setEntity(entity);
    	
        HttpResponse response = client.execute(request);
//        System.out.println(response);
        String result = EntityUtils.toString(response.getEntity());
        System.out.println(result);

        return "###########";
    }
	
	public String httpPut1(String url) throws ClientProtocolException, IOException {
		System.out.println(url);
		HttpClient client = HttpClientBuilder.create().build();
//    	HttpPut request =  (HttpPut) httpRequestService("PUT", url);
    	HttpPut request = new HttpPut(url);
    	String authStr = serverConfig.username + ":" + serverConfig.password;
        String authEncoded = Base64.encode(authStr.getBytes());
    	request.addHeader("Authorization", "Basic " + authEncoded);
    	request.addHeader("X-Requested-By", "ambari");
    	request.addHeader("Content-Type", "application/json");
//    	String body = new String("{\"HostRoles\": {\"state\": \"INSTALLED\"}}");
//    	String start = new String("{\"HostRoles\": {\"state\": \"STARTED\"}}");
//    	String start = new String("{\"HostRoles\": {\"state\": \"STARTED\"}}");
    	
    	
    	JSONObject injson = new JSONObject();
    	injson.put("state", "STARTED");
    	JSONObject outjson = new JSONObject();
    	outjson.put("HostRoles", injson);
    
    	
    	StringWriter out = new StringWriter();
    	outjson.writeJSONString(out);
        String jsonText = out.toString();
    	StringEntity entity = new StringEntity(jsonText);
    	request.setEntity(entity);
    	
        HttpResponse response = client.execute(request);
        System.out.println(response);
//        String result = EntityUtils.toString(response.getEntity());

        return "###########";
    }
	
	private String httpPost(String url) throws ClientProtocolException, IOException {
    	HttpClient client = HttpClientBuilder.create().build();
    	HttpPost request = (HttpPost) httpRequestService("POST", url);
        HttpResponse response = client.execute(request);
        String result = EntityUtils.toString(response.getEntity());

        return result;
    }
	
    public String httpGet(String url) throws ClientProtocolException, IOException {
    	HttpClient client = HttpClientBuilder.create().build();
    	HttpGet request = (HttpGet) httpRequestService("GET", url);
        HttpResponse response = client.execute(request);
        String result = EntityUtils.toString(response.getEntity());

        return result;
    }
}