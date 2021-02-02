/**
 *
 */
package com.saji.stocks.batch.config;

import com.saji.stocks.core.config.CoreConfig;
import com.saji.stocks.mongo.config.MongoConfig;
import com.saji.stocks.redis.config.RedisConfig;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.scope.JobScope;
import org.springframework.batch.core.scope.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @author sajeth
 *
 */
@SuppressWarnings("exports")
@Configuration
@ComponentScan("com.saji.stocks.batch.config")
@Import({CoreConfig.class, RedisConfig.class, MongoConfig.class, QuartzConfig.class, GeneralConfig.class})
public class BatchConfig {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        PropertySourcesPlaceholderConfigurer props = new PropertySourcesPlaceholderConfigurer();
        props.setIgnoreResourceNotFound(true);
        return props;
    }

    @Bean
    public static StepScope stepScope() {
        final StepScope stepScope = new StepScope();
        stepScope.setAutoProxy(false);
        return stepScope;
    }

    @Bean
    public static JobScope jobScope() {
        final JobScope jobScope = new JobScope();
        jobScope.setAutoProxy(false);
        return jobScope;
    }

    @Bean
    public JobRegistry jobRegistry() {
        return new MapJobRegistry();
    }

    @Bean
    public JobRegistryBeanPostProcessor getJobRegistryBeanPostProcessor(final JobRegistry jobRegistry)
            throws Exception {
        final JobRegistryBeanPostProcessor beanProcessor = new JobRegistryBeanPostProcessor();
        beanProcessor.setJobRegistry(jobRegistry);
        beanProcessor.afterPropertiesSet();
        return beanProcessor;
    }

    @Bean
    public JobRepository jobRepository() throws Exception {
        final JobRepositoryFactoryBean repoFactory = new JobRepositoryFactoryBean();
        repoFactory.setDataSource(dataSource);
        repoFactory.setTransactionManager(transactionManager);

        repoFactory.setIsolationLevelForCreate("ISOLATION_DEFAULT");
        // repoFactory.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ");
        repoFactory.afterPropertiesSet();
        return repoFactory.getObject();

//		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
//	    factory.setDataSource(dataSource);
//	    factory.setTransactionManager(transactionManager);
//	    factory.setValidateTransactionState(true);
//	    factory.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ");
//	    factory.setIncrementerFactory(customIncrementerFactory());
//	    factory.afterPropertiesSet();
//	    return factory.getObject();
    }

    @Bean
    public SimpleJobLauncher jobLauncher(final JobRepository jobRepository) {
        final SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }

    @Bean
    public JobExplorer jobExplorer(final DataSource dataSource) throws Exception {
        final JobExplorerFactoryBean bean = new JobExplorerFactoryBean();
        bean.setDataSource(dataSource);
        bean.afterPropertiesSet();
        return bean.getObject();
    }

    @Bean
    public JobBuilderFactory jobBuilders(final JobRepository jobRepository) throws Exception {
        return new JobBuilderFactory(jobRepository);
    }

    @Bean
    public StepBuilderFactory stepBuilders(final JobRepository repo, final PlatformTransactionManager txManager) {
        return new StepBuilderFactory(repo, txManager);
    }

}
