//package com.saji.stocks.batch.config.jobs;
//
//import java.net.SocketTimeoutException;
//import java.util.Arrays;
//import java.util.concurrent.TimeoutException;
//
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
//public class Auto30MinUpdateConfig {
//	
//private static final Integer SKIP_LIMIT = 20;
//	
//	
//	@Value("${saji.batch.auto30Min.concurrency.limit:10}")
//	private int concurrencyLimit;
//	
//
////	@Bean
////	public ThreadPoolTaskExecutor auto30MinTaskExecutor() {
////		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
////		executor.setCorePoolSize(corePoolSize);
////		executor.setMaxPoolSize(maxPoolSize);
////		executor.setAllowCoreThreadTimeOut(true);
////		return executor;
////	}
//	
//	@Bean
//	public Step auto30MinNewStockJobStep(final StepBuilderFactory stepBuilders,
//			@Qualifier("auto30MinNewStep") final Step auto30MinNewStep,
//			@Qualifier("taskExecutor")final ThreadPoolTaskExecutor taskExecutor,
//			@Qualifier("auto30MinNewPartitioner")final Partitioner partitioner) {
//		return stepBuilders.get("auto30MinNewStockJobStep")
//				.partitioner(auto30MinNewStep)
//				.partitioner("auto30MinNewPartitoner", partitioner)
//				.taskExecutor(taskExecutor)
//				.gridSize(concurrencyLimit)
//				.build();
//	}
//	@Bean
//	public Step auto30MinOldStockJobStep(final StepBuilderFactory stepBuilders,
//			@Qualifier("auto30MinOldStep") final Step auto30MinOldStep,
//			@Qualifier("taskExecutor")final ThreadPoolTaskExecutor auto30MinTaskExecutor,
//			@Qualifier("auto30MinOldPartitioner")final Partitioner partitioner) {
//		return stepBuilders.get("auto30MinOldStockJobStep")
//				.partitioner(auto30MinOldStep)
//				.partitioner("auto30MinOldPartitoner", partitioner)
//				.taskExecutor(auto30MinTaskExecutor)
//				.gridSize(concurrencyLimit)
//				.build();
//	}
//	
//	@Bean
//	public Job auto30MinJob(final JobBuilderFactory jobBuilders,
//			@Qualifier("auto30MinNewStockJobStep") final Step auto30MinNewStockJobStep,
//			@Qualifier("auto30MinOldStockJobStep") final Step auto30MinOldStockJobStep) {
//		return jobBuilders.get("auto30MinJob").start(auto30MinNewStockJobStep).next(auto30MinOldStockJobStep).build();
//	}
//	
//	@Bean
//	public  SkipPolicy auto30MinSkipPolicy() {
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
//	public Step auto30MinNewStep(final StepBuilderFactory stepBuilders,
//			@Qualifier("auto30MinNewItemReader") final ItemReader<StockDTO> auto30MinNewItemReader,
//			@Qualifier("auto30MinNewItemWriter")final ItemWriter<StockDTO> auto30MinNewItemWriter,
//			@Qualifier("auto30MinSkipPolicy")final SkipPolicy auto30MinSkipPolicy) {
//		return stepBuilders.get("auto30MinItemStep")
//				.<StockDTO,StockDTO>chunk(1)
//				.reader(auto30MinNewItemReader)
//				.writer(auto30MinNewItemWriter)
//				.faultTolerant().skipPolicy(auto30MinSkipPolicy)
//				.listener(new ItemFailureLoggerListener<StockDTO,StockDTO>()).build();
//	}
//	
//	@Bean
//	public Step auto30MinOldStep(final StepBuilderFactory stepBuilders,
//			@Qualifier("auto30MinOldItemReader") final ItemReader<StockDTO> auto30MinOldItemReader,
//			@Qualifier("auto30MinOldItemWriter")final ItemWriter<StockDTO> auto30MinOldItemWriter,
//			@Qualifier("auto30MinSkipPolicy")final SkipPolicy auto30MinSkipPolicy) {
//		return stepBuilders.get("auto30MinItemStep")
//				.<StockDTO,StockDTO>chunk(1)
//				.reader(auto30MinOldItemReader)
//				.writer(auto30MinOldItemWriter)
//				.faultTolerant().skipPolicy(auto30MinSkipPolicy)
//				.listener(new ItemFailureLoggerListener<StockDTO,StockDTO>()).build();
//	}
//}
