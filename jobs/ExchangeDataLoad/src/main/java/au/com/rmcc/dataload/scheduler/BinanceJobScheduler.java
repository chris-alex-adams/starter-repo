package au.com.rmcc.dataload.scheduler;

import java.io.IOException;
import java.util.Properties;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import au.com.rmcc.dataload.job.BinanceExchangeJob;
import au.com.rmcc.dataload.util.QuartzUtil;

@Configuration
public class BinanceJobScheduler {
   Logger logger = LoggerFactory.getLogger(getClass());
   
	@Autowired
	@Qualifier("quartzProperties")
	Properties properties;

   @Bean
   public SchedulerFactoryBean binanceSchedulerFactoryBean(JobFactory jobFactory,
         Trigger simpleJobTriggerBinance) throws IOException {
      SchedulerFactoryBean factory = new SchedulerFactoryBean();
      factory.setJobFactory(jobFactory);
      factory.setQuartzProperties(properties);
      factory.setTriggers(simpleJobTriggerBinance);
      return factory;
   }

   @Bean
   public SimpleTriggerFactoryBean simpleJobTriggerBinance(
         @Qualifier("binanceJobDetail")JobDetail jobDetail,
         @Value("${api.binance.jobFrequency}") long frequencyInSeconds) {
	  SimpleTriggerFactoryBean factoryBean = QuartzUtil.createNormalJobTrigger(jobDetail, frequencyInSeconds);
      return factoryBean;
   }
   
   @Bean
   public SimpleTriggerFactoryBean timeoutJobTriggerBinance(
	         @Qualifier("binanceJobDetail")JobDetail jobDetail,
	         @Value("${api.binance.jobFrequencyTimeout}") long frequencyInSeconds) {
	      SimpleTriggerFactoryBean factoryBean = QuartzUtil.createTimeoutJobTrigger(jobDetail, frequencyInSeconds);
	      return factoryBean;
    }

   @Bean("binanceJobDetail")
   public JobDetailFactoryBean binanceJobDetail() {
      JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
      factoryBean.setJobClass(BinanceExchangeJob.class);
      factoryBean.setDurability(true);
      return factoryBean;
   }
}
