package com.saji.stocks.batch.config.jobs;

import com.saji.stocks.batch.listener.ItemFailureLoggerListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SuppressWarnings("exports")
@Configuration
public class AutoMonthlyConfig {

    @Value("${saji.batch.autoMonthlyStock.concurrency.limit:20}")
    private int concurrencyLimit;

    @Bean
    public Step autoMonthlyStep(final StepBuilderFactory stepBuilders,
                                @Qualifier("autoMonthlyItemReader") final ItemReader<String> autoMonthlyItemReader,
                                @Qualifier("autoMonthlyItemWriter") final ItemWriter<String> autoMonthlyItemWriter,
                                @Qualifier("autoSkipPolicy") final SkipPolicy autoSkipPolicy) {
        return stepBuilders.get("autoMonthlyStep").<String, String>chunk(3).reader(autoMonthlyItemReader)
                .writer(autoMonthlyItemWriter).faultTolerant().skipPolicy(autoSkipPolicy)
                .listener(new ItemFailureLoggerListener<String, String>()).build();
    }

    @Bean
    public Step autoMonthlyJobStep(final StepBuilderFactory stepBuilders,
                                   @Qualifier("autoMonthlyStep") final Step autoMonthlyStep,
                                   @Qualifier("taskExecutor") final ThreadPoolTaskExecutor taskExecutor,
                                   @Qualifier("autoMonthlyPartitioner") final Partitioner partitioner) {
        return stepBuilders.get("autoMonthlyJobStep").partitioner(autoMonthlyStep)
                .partitioner("autoMonthlyPartitoner", partitioner).taskExecutor(taskExecutor).gridSize(concurrencyLimit)
                .build();
    }

    @Bean
    public Job autoMonthlyJob(final JobBuilderFactory jobBuilders,
                              @Qualifier("autoMonthlyJobStep") final Step autoMonthlyJobStep) {
        return jobBuilders.get("autoMonthlyJob")
                .incrementer(new RunIdIncrementer())
                .start(autoMonthlyJobStep).build();
    }
}
