package au.com.rmcc.dataload.scheduler;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
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

import au.com.rmcc.dataload.job.BitfinexExchangeJob;
import au.com.rmcc.dataload.util.QuartzUtil;

@Configuration
public class BitfinexJobScheduler {
   Logger logger = LoggerFactory.getLogger(getClass());
   
	@Autowired
	@Qualifier("quartzProperties")
	Properties properties;

   @Bean
   public SchedulerFactoryBean bitfinexSchedulerFactoryBean(JobFactory jobFactory,
         Trigger simpleJobTriggerBitfinex) throws IOException {
      SchedulerFactoryBean factory = new SchedulerFactoryBean();
      factory.setJobFactory(jobFactory);
      factory.setQuartzProperties(properties);
      factory.setTriggers(simpleJobTriggerBitfinex);
      return factory;
   }

   @Bean
   public SimpleTriggerFactoryBean simpleJobTriggerBitfinex(
         @Qualifier("bitfinexJobDetail")JobDetail jobDetail,
         @Value("${api.bitfinex.jobFrequency}") long frequencyInSeconds) {
	  SimpleTriggerFactoryBean factoryBean = QuartzUtil.createNormalJobTrigger(jobDetail, frequencyInSeconds);
      return factoryBean;
   }
   
   @Bean
   public SimpleTriggerFactoryBean timeoutJobTriggerBitfinex(
	         @Qualifier("bitfinexJobDetail")JobDetail jobDetail,
	         @Value("${api.bitfinex.jobFrequencyTimeout}") long frequencyInSeconds) {
	      SimpleTriggerFactoryBean factoryBean = QuartzUtil.createTimeoutJobTrigger(jobDetail, frequencyInSeconds);
	      return factoryBean;
    }

   @Bean("bitfinexJobDetail")
   public JobDetailFactoryBean bitfinexJobDetail() {
      JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
      factoryBean.setJobClass(BitfinexExchangeJob.class);
      factoryBean.setDurability(true);
      return factoryBean;
   }
}
