package com.example.demo;


import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

@Slf4j
public class MyJob extends BaseJob {

    @Override
    protected void doExecute(JobExecutionContext context) {
        log.info("### {} is being executed!",
                context.getJobDetail().getJobDataMap().get("JobName").toString());
    }
}