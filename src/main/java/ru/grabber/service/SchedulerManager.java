package ru.grabber.service;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.grabber.stores.Store;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class SchedulerManager implements AutoCloseable {
    private static final Logger log = Logger.getLogger(SchedulerManager.class);
    private Scheduler scheduler;

    public void init() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
        } catch (SchedulerException se) {
            log.error("When init scheduler", se);
        }
    }

    public void load(int period, Class<SuperJobGrab> task, Store store) {

        try {
            var data = new JobDataMap();
            var job = newJob()
                    .usingJobData(data)
                    .build();

            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(period)
                    .repeatForever();

            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();

            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            log.error("When init job", se);
        }
    }

    @Override
    public void close() throws Exception {
        if (scheduler != null) {
            try {
                scheduler.shutdown();
            } catch (SchedulerException e) {
                log.error("When shutdown scheduler", e);
            }
        }
    }
}
