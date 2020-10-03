package au.com.rmcc.dataload.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import au.com.anymoove.common.model.CommonTicker;
import au.com.rmcc.dataload.model.OkexTicker;

@Component
public class OkexService implements CoinService<OkexTicker> {
	
	@Value("${api.okex.endpoint}")
	private String ENDPOINT;
	
	@Value("${api.okex.name}")
	private String EXCHANGE;
	
	@Autowired
	RestTemplate restTemplate;
	
	public List<CommonTicker> getAllTickers() {
		OkexTicker[] response = restTemplate.getForObject(ENDPOINT, OkexTicker[].class);
		List<CommonTicker> coins = Arrays.asList(response).stream().map(x -> parseToCommonTicker(x)).collect(Collectors.toList());
		return coins;
	}
	
	public CommonTicker parseToCommonTicker(OkexTicker okexTicker) {
		String[] split = okexTicker.instrument_id.split("-");
		
		CommonTicker commonTicker = CommonTicker.builder()
				.bestAsk(okexTicker.best_ask)
				.bestBid(okexTicker.best_bid)
				.coinId(split[0])
				.units(split[1])
				.last(okexTicker.last)
				.ask(okexTicker.ask)
				.bid(okexTicker.bid)
				.open24h(okexTicker.open_24h)
				.high24h(okexTicker.high_24h)
				.low24h(okexTicker.low_24h)
				.baseVolume24h(okexTicker.base_volume_24h)
				.timestamp(okexTicker.timestamp)
				.quoteVolume24h(okexTicker.quote_volume_24h)
				.exchangeName(EXCHANGE)
				.build();
		return commonTicker;
	}
	
	
	
	

}
