//package com.saji.stocks.batch.config.jobs;
//
//import java.net.SocketTimeoutException;
//import java.util.Arrays;
//import java.util.concurrent.TimeoutException;
////import org.hibernate.StaleObjectStateException;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.partition.support.Partitioner;
//import org.springframework.batch.core.step.skip.SkipPolicy;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.dao.OptimisticLockingFailureException;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.HttpServerErrorException;
//
//import com.saji.stocks.batch.dto.StockDTO;
//import com.saji.stocks.batch.exception.BatchSkipPolicy;
//import com.saji.stocks.batch.listener.ItemFailureLoggerListener;
//@SuppressWarnings("exports")
//@Configuration
//public class Auto5MinUpdateConfig {
//	
//	private static final Integer SKIP_LIMIT = 20;
//	
//	
//	@Value("${saji.batch.auto5min.concurrency.limit:10}")
//	private int concurrencyLimit;
//	
//	
//	@Bean
//	public Step auto5MinNewStockJobStep(final StepBuilderFactory stepBuilders,
//			@Qualifier("auto5MinNewStep") final Step auto5MinNewStep,
//			@Qualifier("taskExecutor")final ThreadPoolTaskExecutor taskExecutor,
//			@Qualifier("auto5MinNewPartitioner")final Partitioner partitioner) {
//		return stepBuilders.get("auto5MinNewStockJobStep")
//				.partitioner(auto5MinNewStep)
//				.partitioner("auto5MinNewPartitoner", partitioner)
//				.taskExecutor(taskExecutor)
//				.gridSize(concurrencyLimit)
//				.build();
//	}
//	@Bean
//	public Step auto5MinOldStockJobStep(final StepBuilderFactory stepBuilders,
//			@Qualifier("auto5MinOldStep") final Step auto5MinOldStep,
//			@Qualifier("taskExecutor")final ThreadPoolTaskExecutor auto5MinTaskExecutor,
//			@Qualifier("auto5MinOldPartitioner")final Partitioner partitioner) {
//		return stepBuilders.get("auto5MinOldStockJobStep")
//				.partitioner(auto5MinOldStep)
//				.partitioner("auto5MinOldPartitoner", partitioner)
//				.taskExecutor(auto5MinTaskExecutor)
//				.gridSize(concurrencyLimit)
//				.build();
//	}
//	
//	@Bean
//	public Job auto5MinJob(final JobBuilderFactory jobBuilders,
//			@Qualifier("auto5MinNewStockJobStep") final Step auto5MinNewStockJobStep,
//			@Qualifier("auto5MinOldStockJobStep") final Step auto5MinOldStockJobStep) {
//		return jobBuilders.get("auto5MinJob").start(auto5MinNewStockJobStep).next(auto5MinOldStockJobStep).build();
//	}
//	
//	@Bean
//	public  SkipPolicy auto5MinSkipPolicy() {
//		return new BatchSkipPolicy(SKIP_LIMIT,Arrays.asList(
//				OptimisticLockingFailureException.class,
//				//StaleObjectStateException.class,
//				SocketTimeoutException.class,
//				TimeoutException.class,
//				HttpClientErrorException.class,
//				HttpServerErrorException.class));
//	}
//	
//	@Bean
//	public Step auto5MinNewStep(final StepBuilderFactory stepBuilders,
//			@Qualifier("auto5MinNewItemReader") final ItemReader<StockDTO> auto5MinNewItemReader,
//			@Qualifier("auto5MinNewItemWriter")final ItemWriter<StockDTO> auto5MinNewItemWriter,
//			@Qualifier("auto5MinSkipPolicy")final SkipPolicy auto5MinSkipPolicy) {
//		return stepBuilders.get("auto5MinItemStep")
//				.<StockDTO,StockDTO>chunk(1)
//				.reader(auto5MinNewItemReader)
//				.writer(auto5MinNewItemWriter)
//				.faultTolerant().skipPolicy(auto5MinSkipPolicy)
//				.listener(new ItemFailureLoggerListener<StockDTO,StockDTO>()).build();
//	}
//	
//	@Bean
//	public Step auto5MinOldStep(final StepBuilderFactory stepBuilders,
//			@Qualifier("auto5MinOldItemReader") final ItemReader<StockDTO> auto5MinOldItemReader,
//			@Qualifier("auto5MinOldItemWriter")final ItemWriter<StockDTO> auto5MinOldItemWriter,
//			@Qualifier("auto5MinSkipPolicy")final SkipPolicy auto5MinSkipPolicy) {
//		return stepBuilders.get("auto5MinItemStep")
//				.<StockDTO,StockDTO>chunk(1)
//				.reader(auto5MinOldItemReader)
//				.writer(auto5MinOldItemWriter)
//				.faultTolerant().skipPolicy(auto5MinSkipPolicy)
//				.listener(new ItemFailureLoggerListener<StockDTO,StockDTO>()).build();
//	}
//	
//}
