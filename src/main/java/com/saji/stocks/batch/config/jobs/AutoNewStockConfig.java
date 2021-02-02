//package com.saji.stocks.batch.config.jobs;
//
//import com.saji.stocks.batch.listener.ItemFailureLoggerListener;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.partition.support.Partitioner;
//import org.springframework.batch.core.step.skip.SkipPolicy;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//@Configuration
//public class AutoNewStockConfig {
//    @Value("${saji.batch.autoNewStock.concurrency.limit:4}")
//    private int concurrencyLimit;
//
//    @Bean
//    public Step autoNewStockStep(final StepBuilderFactory stepBuilders,
//                                 @Qualifier("autoNewStockItemReader") final ItemReader<String> autoNewStockItemReader,
//                                 @Qualifier("autoNewStockItemWriter") final ItemWriter<String> autoNewStockItemWriter,
//                                 @Qualifier("autoSkipPolicy") final SkipPolicy autoSkipPolicy) {
//        return stepBuilders.get("autoNewStockStep").<String, String>chunk(1).reader(autoNewStockItemReader)
//                .writer(autoNewStockItemWriter).faultTolerant().skipPolicy(autoSkipPolicy)
//                .listener(new ItemFailureLoggerListener<String, String>()).build();
//    }
//
//    @Bean
//    public Step autoNewStockStockJobStep(final StepBuilderFactory stepBuilders,
//                                         @Qualifier("autoNewStockStep") final Step autoNewStockStep,
//                                         @Qualifier("taskExecutor") final ThreadPoolTaskExecutor taskExecutor,
//                                         @Qualifier("autoNewStockPartitioner") final Partitioner partitioner) {
//        return stepBuilders.get("autoNewStockStockJobStep").partitioner(autoNewStockStep)
//                .partitioner("autoNewStockPartitoner", partitioner).taskExecutor(taskExecutor)
//                .gridSize(concurrencyLimit).build();
//    }
//
//    @Bean
//    public Job autoNewStockJob(final JobBuilderFactory jobBuilders,
//                               @Qualifier("autoNewStockStockJobStep") final Step autoNewStockStockJobStep) {
//        return jobBuilders.get("autoNewStockJob").incrementer(new RunIdIncrementer()).start(autoNewStockStockJobStep)
//                .build();
//    }
//}
