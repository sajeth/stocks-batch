/**
 *
 */
package com.saji.stocks.batch.quartz;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;

import java.util.concurrent.Callable;

/**
 * @author sajeth
 *
 */
public class AsyncLaunchJobTask implements Callable<Object> {

    private final JobLauncher jobLauncher;
    private final JobLocator jobLocator;
    private final String jobName;
    private final JobParameters jobParameters;

    /**
     * @param jobLauncher
     * @param jobLocator
     * @param jobName
     * @param jobParameters
     */
    public AsyncLaunchJobTask(final JobLauncher jobLauncher, final JobLocator jobLocator, final String jobName,
                              final JobParameters jobParameters) {
        super();
        this.jobLauncher = jobLauncher;
        this.jobLocator = jobLocator;
        this.jobName = jobName;
        this.jobParameters = jobParameters;
    }

    @Override
    public Object call() throws Exception {
        return jobLauncher.run(jobLocator.getJob(jobName), jobParameters);
    }

}
