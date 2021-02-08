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
//public class Auto15MinUpdateConfig {
//
//private static final Integer SKIP_LIMIT = 20;
//	
//	
//	@Value("${saji.batch.auto15Min.concurrency.limit:10}")
//	private int concurrencyLimit;
//	
//	@Bean
//	public  SkipPolicy auto15MinSkipPolicy() {
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
//	public Step auto15MinNewStep(final StepBuilderFactory stepBuilders,
//			@Qualifier("auto15MinNewItemReader") final ItemReader<StockDTO> auto15MinNewItemReader,
//			@Qualifier("auto15MinNewItemWriter")final ItemWriter<StockDTO> auto15MinNewItemWriter,
//			@Qualifier("auto15MinSkipPolicy")final SkipPolicy auto15MinSkipPolicy) {
//		return stepBuilders.get("auto15MinNewStep")
//				.<StockDTO,StockDTO>chunk(1)
//				.reader(auto15MinNewItemReader)
//				.writer(auto15MinNewItemWriter)
//				.faultTolerant().skipPolicy(auto15MinSkipPolicy)
//				.listener(new ItemFailureLoggerListener<StockDTO,StockDTO>()).build();
//	}
//	
//	@Bean
//	public Step auto15MinNewStockJobStep(final StepBuilderFactory stepBuilders,
//			@Qualifier("auto15MinNewStep") final Step auto15MinNewStep,
//			@Qualifier("taskExecutor")final ThreadPoolTaskExecutor taskExecutor,
//			@Qualifier("auto15MinNewPartitioner")final Partitioner partitioner) {
//		return stepBuilders.get("auto15MinNewStockJobStep")
//				.partitioner(auto15MinNewStep)
//				.partitioner("auto15MinNewPartitoner", partitioner)
//				.taskExecutor(taskExecutor)
//				.gridSize(concurrencyLimit)
//				.build();
//	}
//	
//	@Bean
//	public Step auto15MinOldStep(final StepBuilderFactory stepBuilders,
//			@Qualifier("auto15MinOldItemReader") final ItemReader<StockDTO> auto15MinOldItemReader,
//			@Qualifier("auto15MinOldItemWriter")final ItemWriter<StockDTO> auto15MinOldItemWriter,
//			@Qualifier("auto15MinSkipPolicy")final SkipPolicy auto15MinSkipPolicy) {
//		return stepBuilders.get("auto15MinOldItemStep")
//				.<StockDTO,StockDTO>chunk(1)
//				.reader(auto15MinOldItemReader)
//				.writer(auto15MinOldItemWriter)
//				.faultTolerant().skipPolicy(auto15MinSkipPolicy)
//				.listener(new ItemFailureLoggerListener<StockDTO,StockDTO>()).build();
//	}
//	
//	@Bean
//	public Step auto15MinOldStockJobStep(final StepBuilderFactory stepBuilders,
//			@Qualifier("auto15MinOldStep") final Step auto15MinOldStep,
//			@Qualifier("taskExecutor")final ThreadPoolTaskExecutor auto15MinTaskExecutor,
//			@Qualifier("auto15MinOldPartitioner")final Partitioner partitioner) {
//		return stepBuilders.get("auto15MinOldStockJobStep")
//				.partitioner(auto15MinOldStep)
//				.partitioner("auto15MinOldPartitoner", partitioner)
//				.taskExecutor(auto15MinTaskExecutor)
//				.gridSize(concurrencyLimit)
//				.build();
//	}
//	
//	@Bean
//	public Job auto15MinJob(final JobBuilderFactory jobBuilders,
//			@Qualifier("auto15MinNewStockJobStep") final Step auto15MinNewStockJobStep,
//			@Qualifier("auto15MinOldStockJobStep") final Step auto15MinOldStockJobStep) {
//		return jobBuilders.get("auto15MinJob").start(auto15MinNewStockJobStep).next(auto15MinOldStockJobStep).build();
//	}
//	
//}
