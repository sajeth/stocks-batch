package com.saji.stocks.batch.config.jobs;

import com.saji.stocks.batch.listener.ItemFailureLoggerListener;
import com.saji.stocks.mongo.pojos.StockData;
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
public class Auto3MonthlyConfig {

    @Value("${saji.batch.auto3MonthlyStock.concurrency.limit:25}")
    private int concurrencyLimit;

    @Bean
    public Step auto3MonthlyStep(final StepBuilderFactory stepBuilders,
                                 @Qualifier("auto3MonthlyItemReader") final ItemReader<StockData> auto3MonthlyItemReader,
                                 @Qualifier("auto3MonthlyItemWriter") final ItemWriter<StockData> auto3MonthlyItemWriter,
                                 @Qualifier("autoSkipPolicy") final SkipPolicy autoSkipPolicy) {
        return stepBuilders.get("auto3MonthlyStep").<StockData, StockData>chunk(3).reader(auto3MonthlyItemReader)
                .writer(auto3MonthlyItemWriter).faultTolerant().skipPolicy(autoSkipPolicy)
                .listener(new ItemFailureLoggerListener<String, String>()).build();
    }

    @Bean
    public Step auto3MonthlyJobStep(final StepBuilderFactory stepBuilders,
                                    @Qualifier("auto3MonthlyStep") final Step auto3MonthlyStep,
                                    @Qualifier("taskExecutor") final ThreadPoolTaskExecutor taskExecutor,
                                    @Qualifier("auto3MonthlyPartitioner") final Partitioner partitioner) {
        return stepBuilders.get("auto3MonthlyJobStep").partitioner(auto3MonthlyStep)
                .partitioner("auto3MonthlyPartitoner", partitioner).taskExecutor(taskExecutor).gridSize(concurrencyLimit)
                .build();
    }

    @Bean
    public Job auto3MonthlyJob(final JobBuilderFactory jobBuilders,
                               @Qualifier("auto3MonthlyJobStep") final Step auto3MonthlyJobStep) {
        return jobBuilders.get("auto3MonthlyJob")
                .incrementer(new RunIdIncrementer())
                .start(auto3MonthlyJobStep).build();
    }
}
