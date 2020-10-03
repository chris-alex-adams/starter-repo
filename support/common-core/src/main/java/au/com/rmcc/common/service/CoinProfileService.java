package au.com.rmcc.common.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.com.rmcc.common.model.CoinPriceSummary;
import au.com.rmcc.common.model.CoinProfile;
import au.com.rmcc.common.model.ManifestCoin;
import au.com.rmcc.common.repository.CoinProfileRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CoinProfileService {
	
	
	@Autowired
	CommonTickerService commonTickerService;
	
	@Autowired
	ManifestCoinService manifestService;
	
	@Autowired
	CoinProfileRepository coinProfileRepository;
	
	@Autowired
	ReviewService reviewService;
	
	public List<CoinProfile> generateAllCoins() {
		
		HashMap<String, CoinPriceSummary> coinsMap = commonTickerService.getAllCoinEntry();
		log.info("number of coin summaries: {}", coinsMap.size());
		List<ManifestCoin> manifestCoins = manifestService.getAllCoins();
		log.info("number of manifest coins : {}", manifestCoins.size());
		List<CoinProfile> coinProfiles = new ArrayList<CoinProfile>();
		log.info("collected all coins. Ready to process");
		
		for(ManifestCoin manifestCoin: manifestCoins) {
			CoinPriceSummary coinSum = coinsMap.get(manifestCoin.getSymbol());
			if(coinSum != null) {
				CoinProfile coinProfile = new CoinProfile();
				coinProfile.setSymbol(manifestCoin.getSymbol());
				coinProfile.setCoinSummary(coinSum);
				coinProfile.setManifestCoin(manifestCoin);
				coinProfile.setReviewsLastDay(reviewService.countReviewBySymbolLastDay(manifestCoin.getSymbol()));
				coinProfiles.add(coinProfile);
			}
			if(manifestCoin.getSymbol().equals("XMO")) {
				
			}
		}
		;
		log.info("Generated all the coins!");
		return coinProfiles;
	}
	
	
	public List<CoinProfile> getAllCoinsOrderByReviews() {
		List<CoinProfile> allCoins = getAllCoins();
		List<CoinProfile> coinsLastDay = allCoins.stream()
		  .filter(c -> c.getReviewsLastDay() != 0).sorted(Comparator.comparing(CoinProfile::getReviewsLastDay).reversed())
		  .collect(Collectors.toList());
		if(coinsLastDay.isEmpty()) {
			return allCoins.stream().sorted(Comparator.comparing(CoinProfile::getReviewsLastDay).reversed())
					  .collect(Collectors.toList());
		}
		return coinsLastDay;
	}
	
	public List<CoinProfile> getAllCoins() {
		List<CoinProfile> coins = coinProfileRepository.findAll();
	    return coins;
	}
	
	public List<CoinProfile> getAllCoinsFilter() {
		List<CoinProfile> coins = coinProfileRepository.findAll();
	    return coins.stream().map(c -> {
			CoinProfile c2 = new CoinProfile();
			ManifestCoin mc2 = new ManifestCoin();
			mc2.setSymbol(c.getManifestCoin().getSymbol());
			mc2.setName(c.getManifestCoin().getName());
			c2.setManifestCoin(mc2);
			return c2;
			}).collect(Collectors.toList());
	}
	
	public List<CoinProfile> getAllCoinsByReviews() {
		List<CoinProfile> coins = coinProfileRepository.findAll();
	    return coins;
	}
	
	
	
	public CoinProfile getCoin(String symbol) {
		List<CoinProfile> coinProfile = coinProfileRepository.findBySymbol(symbol);
		if(!coinProfile.isEmpty()) {
			return coinProfile.get(0);
		} else {
			return null;
		}
	}
	
	public long countCoins() {
		long count = coinProfileRepository.count();
		return count;
	}

}
