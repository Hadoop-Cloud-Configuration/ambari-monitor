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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
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
public class AmbariAgentService {
	@Autowired
	private AmbariServerConfig serverConfig = new AmbariServerConfig();
	@Autowired
	AmbariServerService serverService = new AmbariServerService();
	
	/**
	 * Install Ambari Agent on EC2 isntances using Ambari Server Restful API.
	 * @param hosts
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public String installAmbAgent(String[] hosts, String url, String pemFilePath) throws IOException {
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(url);
		String authStr = serverConfig.username + ":" + serverConfig.password;
        String authEncoded = Base64.encode(authStr.getBytes());
    	request.addHeader("Authorization", "Basic " + authEncoded);
    	request.addHeader("X-Requested-By", "ambari");
    	request.addHeader("Content-Type", "application/json");
		
    	JSONObject json = new JSONObject();
    	JSONArray list = new JSONArray();
    	
    	if (hosts != null || hosts.length != 0) {
    		for (String host: hosts) {
        		list.add(host);
        	}
    	}
    	
    	String key = getKeyFromFile(pemFilePath);
    	json.put("verbose", new Boolean(true));
    	json.put("sshKey", key);
    	json.put("hosts", list);
    	json.put("user", "root");
    	json.put("userRunAs", "root");
    	
    	StringWriter out = new StringWriter();
    	json.writeJSONString(out);
        String jsonText = out.toString();
    	StringEntity entity = new StringEntity(jsonText);
    	request.setEntity(entity);
        HttpResponse response = client.execute(request);
        String result = EntityUtils.toString(response.getEntity());
        
        return result;
	}
	
	/**
	 * Read xxx.pem file from host path.
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	public String getKeyFromFile(String fileName) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(fileName));
		StringBuilder sb = new StringBuilder();
		while (scanner.hasNext()) {
			sb.append(scanner.nextLine() + '\n');
		}
		return sb.toString();
	}
}