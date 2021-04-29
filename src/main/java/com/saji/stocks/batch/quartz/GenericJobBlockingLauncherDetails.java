/**
 *
 */
package com.saji.stocks.batch.quartz;

import com.saji.stocks.core.repository.batch.BatchJobRepository;
import com.saji.stocks.entities.batch.DefaultParameter;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author sajeth
 *
 */
@DisallowConcurrentExecution
public class GenericJobBlockingLauncherDetails extends QuartzJobBean {
    private static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().toString());
    private static final String JOB_NAME = "jobName";

    @Autowired
    private JobLocator jobLocator;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private BatchJobRepository batchJobRepository;
    @Autowired
    private AsyncTaskExecutor taskExecutor;

    public GenericJobBlockingLauncherDetails() {

    }

//    /**
//     * @param jobLocator
//     * @param jobLauncher
//     * @param batchJobDAO
//     * @param taskExecutor
//     */
//    public GenericJobBlockingLauncherDetails(final JobLocator jobLocator,final JobLauncher jobLauncher,final BatchJobRepository batchJobRepository,
//            final AsyncTaskExecutor taskExecutor) {
//        this.jobLocator = jobLocator;
//        this.jobLauncher = jobLauncher;
//        this.batchJobRepository = batchJobRepository;
//        this.taskExecutor = taskExecutor;
//    }

    @Override
    protected void executeInternal(final JobExecutionContext context) throws JobExecutionException {
        final Map<String, Object> jobDataMap = context.getMergedJobDataMap();
        final String jobName = (String) jobDataMap.get(JOB_NAME);
        final Date startTime = new Date();
        jobDataMap.put("startTime", startTime);
        final List<DefaultParameter> defaultParameters = this.batchJobRepository.getBatchDefaultParameters(jobName);
        defaultParameters.stream().filter(param -> !jobDataMap.containsKey(param.getKey()))
                .forEach(param -> jobDataMap.put(param.getKey(), param.getValue()));
        final JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap);
        Long maxRunTime;
        try {
            maxRunTime = Long.parseLong(jobParameters.getString("maxRuntime"));
        } catch (Exception e) {
            maxRunTime = 0L;
        }
        AsyncLaunchJobTask launchJobTask = new AsyncLaunchJobTask(jobLauncher, jobLocator, jobName, jobParameters);
        log.log(Level.FINE, "Launching job {} in foreground", jobName);
        final Future<Object> runningJob = taskExecutor.submit(launchJobTask);
        try {
            if (maxRunTime > 0) {
                runningJob.get(maxRunTime, TimeUnit.MINUTES);
            } else {
                runningJob.get(1, TimeUnit.DAYS);
            }
        } catch (InterruptedException | ExecutionException e) {
            log.log(Level.WARNING, "Exception while running job" + jobName, e);
        } catch (TimeoutException e) {
            runningJob.cancel(true);
            log.log(Level.WARNING, "Batch job " + jobName + " timed out,cancelled");
        }
    }

    /**
     * @param jobDataMap
     * @return
     */
    private JobParameters getJobParametersFromJobMap(final Map<String, Object> jobDataMap) {
        final JobParametersBuilder builder = new JobParametersBuilder();
        jobDataMap.entrySet().forEach(obj -> {
            final String key = obj.getKey();
            final Object value = obj.getValue();
            if (value instanceof String && !key.equals(JOB_NAME)) {
                builder.addString(key, (String) value);
            } else if (value instanceof Float || value instanceof Double) {
                builder.addDouble(key, ((Number) value).doubleValue());
            } else if (value instanceof Integer || value instanceof Long) {
                builder.addLong(key, ((Number) value).longValue());
            } else if (value instanceof Date) {
                builder.addDate(key, (Date) value);
            }
        });
        builder.addDate("run date", new Date());
        return builder.toJobParameters();
    }

    @Override
    public String toString() {
        return "GenericBlockingJobLauncherDetails [jobLocator=" + jobLocator + ", jobLauncher=" + jobLauncher
                + ", taskExecutor=" + taskExecutor + "]";
    }

    public final void setJobLocator(final JobLocator jobLocator) {
        this.jobLocator = jobLocator;
    }

    public final void setJobLauncher(final JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    public final void setTaskExecutor(final AsyncTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

}
