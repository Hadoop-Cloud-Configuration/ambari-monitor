package com.sequenceiq.ambari.scaling.monitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sequenceiq.ambari.client.AmbariClient;
import com.sequenceiq.ambari.client.SendReq;
import com.sequenceiq.ambari.domain.ScalingType;
import com.sequenceiq.ambari.scaling.monitor.event.AbstractEventPublisher;
import com.sequenceiq.ambari.scaling.monitor.event.ScalingEvent;

/**
 * Created by jiang on 15/11/4.
 */
public class MetricsMonitor extends AbstractEventPublisher {
	String host;
	String port;

	public MetricsMonitor(String host, String port) {
		// TODO Auto-generated constructor stub
		this.host = host;
		this.port = port;
	}

	public boolean valid() throws InterruptedException {
		// TODO Auto-generated method stub
		List<Double> list = Collections.synchronizedList(new ArrayList());
		while (list.size() < 10) {
			Thread t1 = new Thread(new SendReq(list, host, port));
			t1.start();
			t1.join();
		}
		System.out.println(list);
		double avg = 0;
		for (double i : list)
			avg += i;
		return avg / 10 > 80 ? true : false;
	}
}
