package com.sequenceiq.ambari.client;

import java.util.List;

public class SendReq implements Runnable{
	private List<Double> list;
	private AmbariClient client;
	public SendReq(List<Double> list,String host,String port) {
		// TODO Auto-generated constructor stub
		client = new AmbariClient(host, port,"admin","admin");
		this.list = list;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			String t = client.getYarnData();
			list.add(Double.valueOf(t));
//			System.out.println(list);
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
	}

}
