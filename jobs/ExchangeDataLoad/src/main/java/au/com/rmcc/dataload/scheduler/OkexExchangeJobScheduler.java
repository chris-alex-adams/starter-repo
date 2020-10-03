package au.com.rmcc.dataload.scheduler;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import au.com.rmcc.dataload.config.SpringJobFactory;
import au.com.rmcc.dataload.job.BitfinexExchangeJob;
import au.com.rmcc.dataload.job.OkexExchangeJob;
import au.com.rmcc.dataload.util.QuartzUtil;

@Configuration
public class OkexExchangeJobScheduler {
	 
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	@Qualifier("quartzProperties")
	Properties properties;

   @Bean
   public SchedulerFactoryBean okexSchedulerFactoryBean(JobFactory jobFactory,
		   @Qualifier("okexExchangeTrigger")Trigger simpleJobTrigger) throws IOException {
      SchedulerFactoryBean factory = new SchedulerFactoryBean();
      factory.setJobFactory(jobFactory);
      factory.setQuartzProperties(properties);
      factory.setTriggers(simpleJobTrigger);
      return factory;
   }

   @Bean
   public SimpleTriggerFactoryBean okexExchangeTrigger(
         @Qualifier("okexExchangeJobDetail") JobDetail jobDetail,
         @Value("${api.okex.jobFrequency}") long frequencyInSeconds) {
      SimpleTriggerFactoryBean factoryBean = QuartzUtil.createNormalJobTrigger(jobDetail, frequencyInSeconds);
      return factoryBean;
   }
   
   @Bean
   public SimpleTriggerFactoryBean timeoutJobTriggerOkex(
	         @Qualifier("okexExchangeJobDetail")JobDetail jobDetail,
	         @Value("${api.okex.jobFrequencyTimeout}") long frequencyInSeconds) {
	      SimpleTriggerFactoryBean factoryBean = QuartzUtil.createTimeoutJobTrigger(jobDetail, frequencyInSeconds);
	      return factoryBean;
    }
   
   @Bean("okexExchangeJobDetail")
   public JobDetailFactoryBean okexExchangeJobDetail() {
      JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
      factoryBean.setJobClass(OkexExchangeJob.class);
      factoryBean.setDurability(true);
      return factoryBean;
   }

}
