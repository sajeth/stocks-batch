//package com.saji.stocks.batch.config.jobs;
//
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
//import com.saji.stocks.batch.listener.ItemFailureLoggerListener;
//@SuppressWarnings("exports")
//@Configuration
//public class AutoHourConfig {
//
//private static final Integer SKIP_LIMIT = 20;
//
//    @Value("${saji.batch.autoHour.concurrency.limit:100}")
//    private int concurrencyLimit;
//
//
//    @Bean
//    public Step autoHourStep(final StepBuilderFactory stepBuilders,
//                              @Qualifier("autoHourItemReader") final ItemReader<String> autoHourItemReader,
//                              @Qualifier("autoHourItemWriter") final ItemWriter<String> autoHourItemWriter,
//                              @Qualifier("autoSkipPolicy") final SkipPolicy autoSkipPolicy) {
//        return stepBuilders.get("autoHourStep").<String, String>chunk(1).reader(autoHourItemReader)
//                .writer(autoHourItemWriter).faultTolerant().skipPolicy(autoSkipPolicy)
//                .listener(new ItemFailureLoggerListener<String, String>()).build();
//    }
//
//    @Bean
//    public Step autoHourJobStep(final StepBuilderFactory stepBuilders,
//                                 @Qualifier("autoHourStep") final Step autoHourStep,
//                                 @Qualifier("taskExecutor") final ThreadPoolTaskExecutor taskExecutor,
//                                 @Qualifier("autoHourPartitoner") final Partitioner partitioner) {
//        return stepBuilders.get("autoHourJobStep").partitioner(autoHourStep)
//                .partitioner("autoHourPartitoner", partitioner)
//                .taskExecutor(taskExecutor).gridSize(concurrencyLimit)
//                .build();
//    }
//
//    @Bean
//    public Job autoHourJob(final JobBuilderFactory jobBuilders,
//                            @Qualifier("autoHourJobStep") final Step autoHourJobStep) {
//
//        return jobBuilders.get("autoHourJob")
//                .incrementer(new RunIdIncrementer())
//                .start(autoHourJobStep).build();
//    }
//}
