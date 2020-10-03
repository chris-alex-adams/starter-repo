package au.com.rmcc.dataload.job;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import au.com.anymoove.common.model.CommonTicker;
import au.com.anymoove.common.service.CommonTickerService;
import au.com.rmcc.dataload.service.BitfinexService;
import au.com.rmcc.dataload.util.QuartzUtil;

@Component
public class BitfinexExchangeJob implements Job {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	BitfinexService service;
	
	@Autowired
	@Qualifier("timeoutJobTriggerBitfinex")
	Trigger timeoutJobTrigger;
	
	@Autowired
	@Qualifier("bitfinexSchedulerFactoryBean")
	Scheduler scheduler;
	
	@Autowired
	CommonTickerService commonTickerService;
	
	private static int failedRequestsInARow = 0;
	private static boolean isFailed = false;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("Job ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());
		List<CommonTicker> tickers = null;
		try {
			if(failedRequestsInARow < 10) {
				tickers = service.getAllTickers();
			}
			
		} catch(HttpClientErrorException error) {
			failedRequestsInARow++;
			if(failedRequestsInARow > 10) {
				QuartzUtil.shutdownJob(scheduler);
			}
			if(isFailed != true) {
				isFailed = true;
				QuartzUtil.rescheduleJob(scheduler, context.getTrigger().getKey(), timeoutJobTrigger);
			}
		}
		
		if(tickers != null) {
			isFailed = false;
			failedRequestsInARow = 0;
			for(CommonTicker ticker: tickers) {
				commonTickerService.insertCoin(ticker);
			}
		}

		logger.info("Next job scheduled @ {}", context.getNextFireTime());
	}
}
