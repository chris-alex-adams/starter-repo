package au.com.rmcc.dataload.job;

import java.util.List;
import java.util.stream.Collectors;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import au.com.anymoove.common.model.CommonTicker;
import au.com.anymoove.common.model.ManifestCoin;
import au.com.anymoove.common.service.CommonTickerService;
import au.com.anymoove.common.service.ManifestCoinService;
import au.com.rmcc.dataload.model.CoinRankingTicker;
import au.com.rmcc.dataload.service.CoinRankingService;
import au.com.rmcc.dataload.util.QuartzUtil;

@Component
public class CoinRankingExchangeJob implements Job {

	private Logger logger = LoggerFactory.getLogger(getClass());
	//
	@Autowired
	CoinRankingService service;
	
	@Autowired
	@Qualifier("timeoutJobTriggerBinance")
	Trigger timeoutJobTrigger;
	
	@Autowired
	@Qualifier("coinRankingSchedulerFactoryBean")
	Scheduler scheduler;
	
	@Autowired
	CommonTickerService commonTickerService;
	
	@Autowired
	ManifestCoinService manifestCoinService;
	
	private static int failedRequestsInARow = 0;
	private static boolean isFailed = false;
	private static int offset = 0;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("Job ** {} ** fired @ {} ** offset = {}", context.getJobDetail().getKey().getName(), context.getFireTime(), offset);
		List<CoinRankingTicker> tickers = null;
		try {
			if(failedRequestsInARow < 10) {
				tickers = service.getAllTickers(offset);
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
			if(tickers.size() == 0) {
				offset = 0;
			} else {
				offset += 100;
			}
			
			isFailed = false;
			failedRequestsInARow = 0;
			List<CommonTicker> commonTickers = tickers.stream().map(x -> service.parseToCommonTicker(x)).filter(x -> x != null).collect(Collectors.toList());
			
			for(CommonTicker ticker: commonTickers) {
				commonTickerService.insertCoin(ticker);
			}
			
			List<ManifestCoin> manifestCoins = tickers.stream().map(x -> service.parseToManifestCoin(x)).filter(x -> x != null).collect(Collectors.toList());
			
			for(ManifestCoin ticker: manifestCoins) {
				manifestCoinService.insertCoin(ticker);
			}
		}

		logger.info("Next job scheduled @ {}", context.getNextFireTime());
	}
}
