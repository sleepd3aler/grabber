package ru.quartz;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit {
    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData("param1", "Hello, Rabbit!")
                    .usingJobData("param2", 123)
                    .build();

            JobDataMap data = new JobDataMap();
            data.put("param1", "Hello, Rabbit!");
            data.put("param2", 123);
            JobDetail job2 = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            int interval = Integer.parseInt(properties().getProperty("rabbit.interval"));
            SimpleScheduleBuilder times =
                    simpleSchedule()
                            .withIntervalInSeconds(interval)
                            .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Properties properties() throws IOException {
        Properties properties = new Properties();
        try (InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            properties.load(in);
        }
        return properties;
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            String param1 = context.getJobDetail().getJobDataMap().getString("param1");
            int param2 = context.getJobDetail().getJobDataMap().getInt("param2");
            System.out.println("Rabbit runs here with param1: " + param1 + " and param2: " + param2);
        }
    }
}
