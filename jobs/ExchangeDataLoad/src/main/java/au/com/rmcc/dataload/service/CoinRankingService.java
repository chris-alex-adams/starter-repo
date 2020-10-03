package au.com.rmcc.dataload.service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import au.com.anymoove.common.model.CoinUnits;
import au.com.anymoove.common.model.CommonTicker;
import au.com.anymoove.common.model.ManifestCoin;
import au.com.anymoove.common.util.CoinUtil;
import au.com.rmcc.dataload.model.CoinRankingParent;
import au.com.rmcc.dataload.model.CoinRankingTicker;

@Component
public class CoinRankingService{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${api.coinRanking.endpoint}")
	private String ENDPOINT;
	
	@Value("${api.coinRanking.name}")
	private String EXCHANGE;
	
	@Autowired
	RestTemplate restTemplate;

	public List<CoinRankingTicker> getAllTickers(int offset) {
		CoinRankingParent response = restTemplate.getForObject(ENDPOINT + "?limit=100&offset=" + offset, CoinRankingParent.class);
		List<CoinRankingTicker> coinRankingTickers = response.getData().getCoins();
		return coinRankingTickers;
	}

	public CommonTicker parseToCommonTicker(CoinRankingTicker ticker) {
		CommonTicker commonTicker = CommonTicker.builder()
				.coinId(ticker.getSymbol())
				.units("USD")
				.baseVolume24h(String.valueOf(ticker.getVolume()))
				.timestamp(Instant.now().toString())
				.last(String.valueOf(ticker.getPrice()))
				.exchangeName(EXCHANGE)
				.build();
		return commonTicker;
	}
	
	public ManifestCoin parseToManifestCoin(CoinRankingTicker ticker) {
		int rankValue = ticker.getRank();
		if(ticker.getRank() < 1) {
			rankValue = Integer.MAX_VALUE;
		}
		
		ManifestCoin manifestCoin = ManifestCoin.builder()
				.symbol(ticker.getSymbol())
				.name(ticker.getName())
				.description(ticker.getDescription())
				.iconType(ticker.getIconType())
				.iconImage(ticker.getIconUrl())
				.circulatingSupply(ticker.getCirculatingSupply())
				.isConfirmedSupply(ticker.isConfirmedSupply())
				.totalSupply(ticker.getTotalSupply())
				.firstSeen(ticker.getFirstSeen())
				.marketCap(ticker.getMarketCap())
				.rank(rankValue)
				.build();
		return manifestCoin;
	}

}
