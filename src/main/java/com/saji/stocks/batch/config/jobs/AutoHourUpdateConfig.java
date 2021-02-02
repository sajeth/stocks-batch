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
//public class AutoHourUpdateConfig {
//
//private static final Integer SKIP_LIMIT = 20;
//	
////	@Value("${saji.batch.autoHour.thread.limit:100}")
////	private int corePoolSize;
////	
////	@Value("${saji.batch.autoHour.thread.limit:100}")
////	private int maxPoolSize;
//	
//	@Value("${saji.batch.autoHour.concurrency.limit:10}")
//	private int concurrencyLimit;
//	
//
////	@Bean
////	public ThreadPoolTaskExecutor autoHourTaskExecutor() {
////		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
////		executor.setCorePoolSize(corePoolSize);
////		executor.setMaxPoolSize(maxPoolSize);
////		executor.setAllowCoreThreadTimeOut(true);
////		return executor;
////	}
//	
//	@Bean
//	public Step autoHourNewStockJobStep(final StepBuilderFactory stepBuilders,
//			@Qualifier("autoHourNewStep") final Step autoHourNewStep,
//			@Qualifier("taskExecutor")final ThreadPoolTaskExecutor taskExecutor,
//			@Qualifier("autoHourNewPartitioner")final Partitioner partitioner) {
//		return stepBuilders.get("autoHourNewStockJobStep")
//				.partitioner(autoHourNewStep)
//				.partitioner("autoHourNewPartitoner", partitioner)
//				.taskExecutor(taskExecutor)
//				.gridSize(concurrencyLimit)
//				.build();
//	}
//	@Bean
//	public Step autoHourOldStockJobStep(final StepBuilderFactory stepBuilders,
//			@Qualifier("autoHourOldStep") final Step autoHourOldStep,
//			@Qualifier("taskExecutor")final ThreadPoolTaskExecutor autoHourTaskExecutor,
//			@Qualifier("autoHourOldPartitioner")final Partitioner partitioner) {
//		return stepBuilders.get("autoHourOldStockJobStep")
//				.partitioner(autoHourOldStep)
//				.partitioner("autoHourOldPartitoner", partitioner)
//				.taskExecutor(autoHourTaskExecutor)
//				.gridSize(concurrencyLimit)
//				.build();
//	}
//	
//	@Bean
//	public Job autoHourJob(final JobBuilderFactory jobBuilders,
//			@Qualifier("autoHourNewStockJobStep") final Step autoHourNewStockJobStep,
//			@Qualifier("autoHourOldStockJobStep") final Step autoHourOldStockJobStep) {
//		return jobBuilders.get("autoHourJob").start(autoHourNewStockJobStep).next(autoHourOldStockJobStep).build();
//	}
//	
//	@Bean
//	public  SkipPolicy autoHourSkipPolicy() {
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
//	public Step autoHourNewStep(final StepBuilderFactory stepBuilders,
//			@Qualifier("autoHourNewItemReader") final ItemReader<StockDTO> autoHourNewItemReader,
//			@Qualifier("autoHourNewItemWriter")final ItemWriter<StockDTO> autoHourNewItemWriter,
//			@Qualifier("autoHourSkipPolicy")final SkipPolicy autoHourSkipPolicy) {
//		return stepBuilders.get("autoHourItemStep")
//				.<StockDTO,StockDTO>chunk(1)
//				.reader(autoHourNewItemReader)
//				.writer(autoHourNewItemWriter)
//				.faultTolerant().skipPolicy(autoHourSkipPolicy)
//				.listener(new ItemFailureLoggerListener<StockDTO,StockDTO>()).build();
//	}
//	
//	@Bean
//	public Step autoHourOldStep(final StepBuilderFactory stepBuilders,
//			@Qualifier("autoHourOldItemReader") final ItemReader<StockDTO> autoHourOldItemReader,
//			@Qualifier("autoHourOldItemWriter")final ItemWriter<StockDTO> autoHourOldItemWriter,
//			@Qualifier("autoHourSkipPolicy")final SkipPolicy autoHourSkipPolicy) {
//		return stepBuilders.get("autoHourItemStep")
//				.<StockDTO,StockDTO>chunk(1)
//				.reader(autoHourOldItemReader)
//				.writer(autoHourOldItemWriter)
//				.faultTolerant().skipPolicy(autoHourSkipPolicy)
//				.listener(new ItemFailureLoggerListener<StockDTO,StockDTO>()).build();
//	}
//}
