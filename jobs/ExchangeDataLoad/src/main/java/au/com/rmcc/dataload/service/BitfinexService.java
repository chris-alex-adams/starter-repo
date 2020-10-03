package au.com.rmcc.dataload.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import au.com.anymoove.common.model.CoinUnits;
import au.com.anymoove.common.model.CommonTicker;

@Component
public class BitfinexService implements CoinService<String[]> {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${api.bitfinex.endpoint}")
	private String ENDPOINT;
	
	@Value("${api.bitfinex.name}")
	private String EXCHANGE;
	
	@Autowired
	RestTemplate restTemplate;
	


	@Override
	public List<CommonTicker> getAllTickers() throws HttpClientErrorException {
		ResponseEntity<List<String[]>> responseEntity =
		        restTemplate.exchange(ENDPOINT,
		            HttpMethod.GET, null, new ParameterizedTypeReference<List<String[]>>() {
		            });
		List<String[]> listOfString = responseEntity.getBody();
		HttpStatus status = responseEntity.getStatusCode();
		if(status.is4xxClientError()) {
			logger.info("exception thrown");
			throw new HttpClientErrorException(status);
		}
		
		List<CommonTicker> coins = listOfString.stream().filter(x -> x.length == 11).map(x -> parseToCommonTicker(x)).collect(Collectors.toList());
		return coins;
	}

	/**
	  // on trading pairs (ex. tBTCUSD)
	  [
	    0 SYMBOL,
	    1 BID, 
	    2 BID_SIZE, 
	    3 ASK, 
	    4 ASK_SIZE, 
	    5 DAILY_CHANGE, 
	    6 DAILY_CHANGE_PERC, 
	    7 LAST_PRICE, 
	    8 VOLUME, 
	    9 HIGH, 
	    10 LOW
	  ],
	 */
	@Override
	public CommonTicker parseToCommonTicker(String[] ticker) {
		String tickerSymbol = ticker[0];
		String tickerUnit = "";
		CoinUnits[] units = CoinUnits.values();
		for(CoinUnits unit: units) {
			String stringUnit = unit.toString();
			if(tickerSymbol.endsWith(stringUnit)) {
				tickerSymbol = tickerSymbol.substring(1, tickerSymbol.length()-stringUnit.length());
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
				.last(ticker[7])
				.ask(ticker[3])
				.bid(ticker[1])
				.baseVolume24h(ticker[8])
				.timestamp(Instant.now().toString())
				.exchangeName(EXCHANGE)
				.build();
		return commonTicker;
	}

}
