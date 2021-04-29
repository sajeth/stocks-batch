package com.saji.stocks.batch.config;

import com.saji.stocks.batch.quartz.GenericJobBlockingLauncherDetails;
import com.saji.stocks.batch.quartz.GenericJobLauncherDetails;
import com.saji.stocks.core.repository.batch.BatchJobRepository;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.Trigger;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
@PropertySource(value = {"classpath:application.properties"})
public class QuartzConfig {
    private static final String BATCH_PROPERTY_PREFIX = "batch.job.cron.";
    private final Logger log = Logger.getLogger("QuartzConfig");
    @Autowired
    private Environment env;

    @Autowired
    private BatchJobRepository batchJobRepository;

    @Autowired
    private JobLocator jobLocator;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JpaTransactionManager transactionManager;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private AsyncListenableTaskExecutor taskExecutor;
    @Value("${batch.job.names}")
    private String[] batchJobs;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        final SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        final Map<String, Object> schedulerContextAsMap = new HashMap<>();
        Trigger[] triggers = new Trigger[batchJobs.length];
        schedulerContextAsMap.put("taskExecutor", taskExecutor);
        schedulerContextAsMap.put("batchJobRepository", batchJobRepository);
        schedulerContextAsMap.put("jobLocator", jobLocator);
        schedulerContextAsMap.put("jobLauncher", jobLauncher);
        scheduler.setSchedulerContextAsMap(schedulerContextAsMap);
        scheduler.setApplicationContextSchedulerContextKey("applicationContext");
        scheduler.setDataSource(dataSource);
        scheduler.setTransactionManager(transactionManager);
        scheduler.setQuartzProperties(quartzProperties());
        for (int i = 0; i < batchJobs.length; i++) {
            triggers[i] = buildJobTrigger(batchJobRepository, buildJobDetail(batchJobRepository, batchJobs[i]),
                    batchJobs[i]).getObject();
        }
        scheduler.setTriggers(triggers);
        return scheduler;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    private JobDetailFactoryBean buildJobDetail(final BatchJobRepository batchJobRepository, final String jobName) {
        final JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();

        if (fetchBlockingState(batchJobRepository, jobName)) {
            jobDetailFactory.setJobClass(GenericJobBlockingLauncherDetails.class);
        } else {
            jobDetailFactory.setJobClass(GenericJobLauncherDetails.class);
        }
        jobDetailFactory.setGroup(jobName + "Group");
        jobDetailFactory.setJobDataMap(buildDataMap(jobName));
        jobDetailFactory.setName(jobName);
        jobDetailFactory.afterPropertiesSet();
        return jobDetailFactory;
    }

    private boolean fetchBlockingState(final BatchJobRepository batchJobRepository, final String jobName) {
        return Optional.ofNullable(batchJobRepository.getBlockingStatus(jobName).getValue()).map(Boolean::parseBoolean)
                .orElseGet(() -> Boolean
                        .parseBoolean(env.getProperty(BATCH_PROPERTY_PREFIX + jobName + ".blocking", "false")));
    }

    private CronTriggerFactoryBean buildJobTrigger(final BatchJobRepository batchJobRepository,
                                                   final JobDetailFactoryBean jobDetail, final String jobName) {

        final CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
        factory.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        factory.setJobDetail(jobDetail.getObject());
        factory.setName(jobName + "_trigger");
        factory.setGroup(BatchJobConstants.DEFAULT.toString());
        String cron = fetchCronExpression(batchJobRepository, jobName);
        factory.setCronExpression(cron);
        factory.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY);
        try {
            factory.afterPropertiesSet();
        } catch (ParseException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        return factory;

    }

    private JobDataMap buildDataMap(final String jobName) {
        final JobDataMap dataMap = new JobDataMap();
        dataMap.put(BatchJobConstants.JOB_NAME.toString(), jobName);
        return dataMap;
    }

    private String fetchCronExpression(final BatchJobRepository batchJobRepository, final String jobName) {
        return batchJobRepository.getCronExpression(jobName).get().getValue();
    }

}
