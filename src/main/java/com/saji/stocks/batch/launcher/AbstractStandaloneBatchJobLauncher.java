/**
 *
 */
package com.saji.stocks.batch.launcher;

import com.saji.stocks.batch.config.BatchConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJvmExitCodeMapper;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

/**
 * @author sajeth
 *
 */
public abstract class AbstractStandaloneBatchJobLauncher {

    private AbstractStandaloneBatchJobLauncher() {
        super();
    }

    protected static void launchBatch(final String[] args, @SuppressWarnings("rawtypes") final Class jobClass,
                                      final String jobName) throws JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        AnnotationConfigApplicationContext context = null;
        int exitStatus = -1;
        try {
            context = new AnnotationConfigApplicationContext(BatchConfig.class, jobClass);
            context.registerShutdownHook();
            final JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
            final Job job = (Job) context.getBean(jobName);
            final SimpleJvmExitCodeMapper simpleJvmExitCodeMapper = new SimpleJvmExitCodeMapper();
            exitStatus = simpleJvmExitCodeMapper
                    .intValue(jobLauncher.run(job, new JobParameters()).getExitStatus().getExitCode());
        } finally {
            if (Optional.ofNullable(context).isPresent()) {
                context.close();
            }
            systemExit(exitStatus);
        }
    }

    protected static void systemExit(final int exitCode) {
        System.exit(exitCode);
    }
}
