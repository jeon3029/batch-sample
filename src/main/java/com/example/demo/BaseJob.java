package com.example.demo;


import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.List;

import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
public abstract class BaseJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        beforeExecute(context);
        doExecute(context);
        afterExecute(context);
        scheduleNextJob(context);
    }

    private void beforeExecute(JobExecutionContext context) {
        log.info("%%% Before executing job");
    }

    protected abstract void doExecute(JobExecutionContext context);

    private void afterExecute(JobExecutionContext context) {
        log.info("%%% After executing job");
        Object object = context.getJobDetail().getJobDataMap().get("JobDetailQueue");
        List<JobDetail> jobDetailQueue = (List<JobDetail>) object;

        if (jobDetailQueue.size() > 0) {
            jobDetailQueue.remove(0);
        }
    }

    private void scheduleNextJob(JobExecutionContext context) {
        log.info("$$$ Schedule Next Job");
        Object object = context.getJobDetail().getJobDataMap().get("JobDetailQueue");
        List<JobDetail> jobDetailQueue = (List<JobDetail>) object;

        if (jobDetailQueue.size() > 0) {
            JobDetail nextJobDetail = jobDetailQueue.get(0);
            nextJobDetail.getJobDataMap().put("JobDetailQueue", jobDetailQueue);
            Trigger nowTrigger = newTrigger().startNow().build();

            try {
                Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
                scheduler.start();
                scheduler.scheduleJob(nextJobDetail, nowTrigger);
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }
    }
}