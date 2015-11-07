package com.sequenceiq.ambari.scaling.monitor;

import com.sequenceiq.ambari.domain.Cluster;
import com.sequenceiq.ambari.scaling.monitor.excutor.MonitorExcutor;
import com.sequenceiq.ambari.services.ClusterService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ExecutorService;

/**
 * Created by jiang on 15/11/5.
 */
public abstract class AbstractMonitor implements Monitor{

    private ApplicationContext applicationContext;
    private ExecutorService executorService;
    private ClusterService clusterService;

    @Override
    public void execute(JobExecutionContext context){
        JobDataMap map = context.getJobDetail().getJobDataMap();
        applicationContext = (ApplicationContext) map.get(MonitorContext.APPLICATION_CONTEXT.name());
        executorService = applicationContext.getBean(ExecutorService.class);
        clusterService = applicationContext.getBean(ClusterService.class);

        for( Cluster cluster : clusterService.findActiveCluster()){
            MonitorExcutor monitorExecutor = applicationContext.getBean(getMonitorType().getSimpleName(), MonitorExcutor.class);
            monitorExecutor.setContext(getContext(cluster));
            executorService.submit(monitorExecutor);
        }


    }
}
