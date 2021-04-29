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
public class AutoDailyConfig {

    @Value("${saji.batch.autoDaily.concurrency.limit:15}")
    private int concurrencyLimit;


    @Bean
    public Step autoDailyStep(final StepBuilderFactory stepBuilders,
                              @Qualifier("autoDailyItemReader") final ItemReader<String> autoDailyItemReader,
                              @Qualifier("autoDailyItemWriter") final ItemWriter<String> autoDailyItemWriter,
                              @Qualifier("autoSkipPolicy") final SkipPolicy autoSkipPolicy) {
        return stepBuilders.get("autoDailyStep").<String, String>chunk(1).reader(autoDailyItemReader)
                .writer(autoDailyItemWriter).faultTolerant().skipPolicy(autoSkipPolicy)
                .listener(new ItemFailureLoggerListener<String, String>()).build();
    }

    @Bean
    public Step autoDailyJobStep(final StepBuilderFactory stepBuilders,
                                 @Qualifier("autoDailyStep") final Step autoDailyStep,
                                 @Qualifier("taskExecutor") final ThreadPoolTaskExecutor taskExecutor,
                                 @Qualifier("autoDailyPartitoner") final Partitioner partitioner) {
        return stepBuilders.get("autoDailyJobStep").partitioner(autoDailyStep)
                .partitioner("autoDailyPartitoner", partitioner)
                .taskExecutor(taskExecutor).gridSize(concurrencyLimit)
                .build();
    }

    @Bean
    public Job autoDailyJob(final JobBuilderFactory jobBuilders,
                            @Qualifier("autoDailyJobStep") final Step autoDailyJobStep) {

        return jobBuilders.get("autoDailyJob")
                .incrementer(new RunIdIncrementer())
                .start(autoDailyJobStep).build();
    }

}
