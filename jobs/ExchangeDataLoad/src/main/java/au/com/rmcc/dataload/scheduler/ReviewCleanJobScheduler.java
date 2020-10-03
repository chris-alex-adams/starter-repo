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

import au.com.rmcc.dataload.job.ReviewCleanJob;
import au.com.rmcc.dataload.util.QuartzUtil;

@Configuration
public class ReviewCleanJobScheduler {
   Logger logger = LoggerFactory.getLogger(getClass());
   
	@Autowired
	@Qualifier("quartzProperties")
	Properties properties;

   @Bean
   public SchedulerFactoryBean reviewCleanSchedulerFactoryBean(JobFactory jobFactory,
         Trigger simpleJobTriggerReviewClean) throws IOException {
      SchedulerFactoryBean factory = new SchedulerFactoryBean();
      factory.setJobFactory(jobFactory);
      factory.setQuartzProperties(properties);
      factory.setTriggers(simpleJobTriggerReviewClean);
      return factory;
   }

   @Bean
   public SimpleTriggerFactoryBean simpleJobTriggerReviewClean(
         @Qualifier("reviewCleanJobDetail")JobDetail jobDetail,
         @Value("${reviewClean.jobFrequency}") long frequencyInSeconds) {
	  SimpleTriggerFactoryBean factoryBean = QuartzUtil.createNormalJobTrigger(jobDetail, frequencyInSeconds);
      return factoryBean;
   }

   @Bean("reviewCleanJobDetail")
   public JobDetailFactoryBean reviewCleanJobDetail() {
      JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
      factoryBean.setJobClass(ReviewCleanJob.class);
      factoryBean.setDurability(true);
      return factoryBean;
   }
}
