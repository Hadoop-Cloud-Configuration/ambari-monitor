package com.sequenceiq.ambari.scaling.monitor;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by jiang on 15/11/5.
 */
@Component
public class MonitorScheduler {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private List<Monitor> monitorList;

    @PostConstruct
    public void scheduleMonitors() throws SchedulerException {
        for (Monitor monitor : monitorList) {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put(MonitorContext.APPLICATION_CONTEXT.name(), applicationContext);
            JobDetail jobDetail = newJob(monitor.getClass()).withIdentity(monitor.getIdentifier()).setJobData(jobDataMap).build();
            CronScheduleBuilder cronBuilder = CronScheduleBuilder.cronSchedule(monitor.getUpdateRate());
            Trigger trigger = newTrigger().startNow().withSchedule(cronBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        }
    }

}
