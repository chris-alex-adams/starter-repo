package au.com.rmcc.common.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import au.com.rmcc.common.model.ManifestCoin;
import au.com.rmcc.common.repository.ManifestCoinRepository;
import au.com.rmcc.common.util.CoinUtil;

@Component
public class ManifestCoinService {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	ManifestCoinRepository manifestRepo;
	
	@Autowired
	RestTemplate restTemplate;
	
	public ManifestCoin getCoin(String symbol) {
		List<ManifestCoin> foundCoins = manifestRepo.findBySymbol(symbol);
		if(foundCoins.isEmpty()) {
			logger.error("Found no Manifest coins for symbol {}", foundCoins.size(), symbol);
			return null;
		} else if(foundCoins.size() > 1) {
			logger.warn("Found {} number of Manifest coins for symbol {}", foundCoins.size(), symbol);
		}
		
		return foundCoins.get(0);
	}
	
	public List<ManifestCoin> getAllCoins() {
		return manifestRepo.findAll();
		
	}
	
	public void insertCoin(ManifestCoin coin) {
		if(coin == null) {
			return;
		}
		
		List<ManifestCoin> manifestCoins = manifestRepo.findBySymbol(coin.getSymbol());
		if(!manifestCoins.isEmpty()) {
			ManifestCoin foundCoin = manifestCoins.get(0);
			ManifestCoin mergedCoin = ManifestCoin.merge(foundCoin, coin);
			mergedCoin.setId(foundCoin.getId());
			manifestRepo.save(mergedCoin);
		} else {
			String imageString = getImageFromUrl(coin.getIconImage());
			coin.setIconImage(imageString);
			manifestRepo.save(coin);
		}
		
		
	}
	
	public String getImageFromUrl(String url) {
		String imageString = url;
		try {
			byte[] imageBytes = restTemplate.getForObject(url, byte[].class);
			imageString = CoinUtil.encodeBase64(imageBytes);
		} catch(Exception er) {
			logger.error("Failed to get image from url: {}", url, er);
		}

		return imageString;
	}

}
