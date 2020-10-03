package au.com.rmcc.dataload.job;

import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.com.anymoove.common.model.CoinProfile;
import au.com.anymoove.common.repository.CoinProfileRepository;
import au.com.anymoove.common.service.CoinProfileService;

@Component
@DisallowConcurrentExecution
public class CoinProfileJob implements Job {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	CoinProfileService service;
	
	@Autowired
	CoinProfileRepository coinProfileRepo;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("Job ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());
		List<CoinProfile> coinList = service.generateAllCoins();
		
		for(CoinProfile coin: coinList) {
			List<CoinProfile> foundCoins = coinProfileRepo.findBySymbol(coin.getSymbol());
			if(!foundCoins.isEmpty()) {
				CoinProfile foundCoin = foundCoins.get(0);
				coin.setId(foundCoin.getId());
			}
			coinProfileRepo.save(coin);
		}
		
		logger.info("Finished the coin profile job. Next job scheduled @ {}", context.getNextFireTime());
	}
}
