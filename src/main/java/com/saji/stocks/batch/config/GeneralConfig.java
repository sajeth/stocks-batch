package com.saji.stocks.batch.config;

import com.saji.stocks.batch.exception.BatchSkipPolicy;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

@Configuration
public class GeneralConfig {
    private static final Integer SKIP_LIMIT = 20;
    @Value("${saji.batch.thread.limit:20}")
    private int corePoolSize;
    @Value("${saji.batch.thread.limit:20}")
    private int maxPoolSize;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    @StepScope
    public ThreadPoolTaskExecutor taskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //  executor.setConcurrencyLimit(corePoolSize);
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setAllowCoreThreadTimeOut(true);
        return executor;
    }

    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {

            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                taskExecutor().shutdown();
            }
        };
    }

    @Bean
    public SkipPolicy autoSkipPolicy() {
        return new BatchSkipPolicy(SKIP_LIMIT, Arrays.asList(OptimisticLockingFailureException.class,
                // StaleObjectStateException.class,
                SocketTimeoutException.class, TimeoutException.class, HttpClientErrorException.class,
                HttpServerErrorException.class));
    }

}
