package au.com.rmcc.dataload.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

public class QuartzUtil {
	
	private static Logger logger = LoggerFactory.getLogger(QuartzUtil.class);
	
	private final static long JOBDELAY = 10 * 1000;
	
	public static void rescheduleJob(Scheduler scheduler, TriggerKey oldTriggerKey, Trigger newTrigger){
		try {
			scheduler.pauseTrigger(oldTriggerKey);
			logger.info("rescheduling job...");
			logger.info("old job key: " + oldTriggerKey);
			logger.info("new job key: " + newTrigger.getKey());
			logger.info("Trigger next fire time: " + newTrigger.getNextFireTime());
			
		    Date firetime = scheduler.rescheduleJob(oldTriggerKey, newTrigger);
		    
		    logger.info("new firetime is: " + firetime);
		    if(firetime == null) {
		    	logger.info("can't find trigger");
		    }
			scheduler.resumeTrigger(newTrigger.getKey());
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void shutdownJob(Scheduler scheduler){
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
   public static SimpleTriggerFactoryBean createTimeoutJobTrigger(JobDetail jobDetail, long frequencyInSeconds) {
      SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
      factoryBean.setJobDetail(jobDetail);
      factoryBean.setStartDelay(JOBDELAY);
      factoryBean.setRepeatInterval(frequencyInSeconds * 1000);
      factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
      return factoryBean;
    }
   
   public static SimpleTriggerFactoryBean createNormalJobTrigger(JobDetail jobDetail, long frequencyInSeconds) {
	   SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
	   factoryBean.setJobDetail(jobDetail);
	   factoryBean.setStartDelay(JOBDELAY);
	   factoryBean.setRepeatInterval(frequencyInSeconds * 1000);
	   factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
	   return factoryBean;
   }

	

}
