package com.camon;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jooyong on 2016-03-03.
 */
public class App {

    private static Logger logger = LoggerFactory.getLogger(App.class);

    /**
     * google 앱 비밀번호 생성 해야함
     * https://support.google.com/accounts/answer/185833?hl=ko
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws SchedulerException {
        if (args.length != 4) {
            logger.info("Invalid Argument!");
            logger.info("ex) java -jar tooni-jar-with-dependencies.jar <Gmail-Id> <Gmail-Password> <Tooni-ID> <Tooni-Password>");
            return;
        }

        String googleUsename = args[0];
        String googlePw = args[1];
        String tooniId = args[2];
        String tooniPw = args[3];

        JobDetail job = JobBuilder.newJob(LoginJob.class)
                .withIdentity("loginJob", "oneHourGroup").build();

        job.getJobDataMap().put("googleUsename", googleUsename);
        job.getJobDataMap().put("googlePw", googlePw);
        job.getJobDataMap().put("tooniId", tooniId);
        job.getJobDataMap().put("tooniPw", tooniPw);

        String cronExpression = "0 59 22,23, * * ?";
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("loginJob", "oneHourGroup")
                .withSchedule(
                        CronScheduleBuilder.cronSchedule(cronExpression))
                .build();

/*
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("loginJob", "oneHourGroup")
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(60).repeatForever())
                .build();
*/

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);

        logger.info("### tooni check job is scheduled at {}", cronExpression);
    }
}
