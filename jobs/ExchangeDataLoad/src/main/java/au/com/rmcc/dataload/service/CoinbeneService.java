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
import au.com.rmcc.dataload.model.CoinbeneParentTicker;
import au.com.rmcc.dataload.model.CoinbeneTicker;

@Component
public class CoinbeneService implements CoinService<CoinbeneTicker> {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${api.coinbene.endpoint}")
	private String ENDPOINT;
	
	@Value("${api.coinbene.name}")
	private String EXCHANGE;
	
	@Autowired
	RestTemplate restTemplate;

	@Override
	public List<CommonTicker> getAllTickers() {
		
		CoinbeneParentTicker response = restTemplate.getForObject(ENDPOINT, CoinbeneParentTicker.class);
		if(response == null) {
			logger.error("response was null");
			return null;
		}
		List<CoinbeneTicker> result = response.getTicker();
		List<CommonTicker> coins = result.stream().map(x -> parseToCommonTicker(x)).filter(x -> x != null).collect(Collectors.toList());
		return coins;
	}

	@Override
	public CommonTicker parseToCommonTicker(CoinbeneTicker ticker) {
		String tickerSymbol = ticker.getSymbol();
		String tickerUnit = "";
		CoinUnits[] units = CoinUnits.values();
		for(CoinUnits unit: units) {
			String stringUnit = unit.toString();
			if(tickerSymbol.endsWith(stringUnit)) {
				tickerSymbol = tickerSymbol.substring(0, tickerSymbol.length()-stringUnit.length());
				tickerUnit = stringUnit;
				break;
			}
		}
		
		if(tickerUnit.equals("")) {
			return null;
		}
		
		CommonTicker commonTicker = CommonTicker.builder()
				.coinId(tickerSymbol)
				.units(tickerUnit)
				.last(ticker.getLast())
				.ask(ticker.getAsk())
				.bid(ticker.getBid())
				.baseVolume24h(ticker.get24hrVol())
				.timestamp(Instant.now().toString())
				.exchangeName(EXCHANGE)
				.build();
		return commonTicker;
	}

}
