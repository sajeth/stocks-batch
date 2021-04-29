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
public class AutoWeeklyConfig {

    @Value("${saji.batch.autoWeekly.concurrency.limit:18}")
    private int concurrencyLimit;

    @Bean
    public Step autoWeeklyStep(final StepBuilderFactory stepBuilders,
                               @Qualifier("autoWeeklyItemReader") final ItemReader<String> autoWeeklyItemReader,
                               @Qualifier("autoWeeklyItemWriter") final ItemWriter<String> autoWeeklyItemWriter,
                               @Qualifier("autoSkipPolicy") final SkipPolicy autoSkipPolicy) {
        return stepBuilders.get("autoWeeklyStep").<String, String>chunk(1).reader(autoWeeklyItemReader)
                .writer(autoWeeklyItemWriter).faultTolerant().skipPolicy(autoSkipPolicy)
                .listener(new ItemFailureLoggerListener<String, String>()).build();
    }

    @Bean
    public Step autoWeeklyJobStep(final StepBuilderFactory stepBuilders,
                                  @Qualifier("autoWeeklyStep") final Step autoWeeklyStep,
                                  @Qualifier("taskExecutor") final ThreadPoolTaskExecutor taskExecutor,
                                  @Qualifier("autoWeeklyPartitioner") final Partitioner partitioner) {
        return stepBuilders.get("autoWeeklyJobStep").partitioner(autoWeeklyStep)
                .partitioner("autoWeeklyPartitoner", partitioner).taskExecutor(taskExecutor).gridSize(concurrencyLimit)
                .build();
    }

    @Bean
    public Job autoWeeklyJob(final JobBuilderFactory jobBuilders,
                             @Qualifier("autoWeeklyJobStep") final Step autoWeeklyJobStep) {
        return jobBuilders.get("autoWeeklyJob")
                .incrementer(new RunIdIncrementer())
                .start(autoWeeklyJobStep).build();
    }
}
