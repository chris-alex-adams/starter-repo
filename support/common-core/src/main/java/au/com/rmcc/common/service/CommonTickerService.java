package au.com.rmcc.common.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import au.com.rmcc.common.model.CoinPriceSummary;
import au.com.rmcc.common.model.CoinUnits;
import au.com.rmcc.common.model.CommonTicker;
import au.com.rmcc.common.repository.CommonTickerRepository;

@Component
public class CommonTickerService {
	
	@Autowired
	CommonTickerRepository commonTickerRepository;
	
	public CommonTicker getCoin(String coinId, String units) {
		Sort sort = new Sort(Sort.Direction.DESC, "timestamp");
		List<CommonTicker> coins = commonTickerRepository.findByCoinIdAndUnits(coinId, units, sort);
		return coins.get(0);

	}
	
	public CoinPriceSummary getCoinEntry(String coinId) {
		CoinPriceSummary coinEntry = new CoinPriceSummary();
		Sort sort = new Sort(Sort.Direction.DESC, "timestamp");
		double totalVolume = 0;
		String coinRankingVolume = "";
		double totalPriceUsd = 0;
		
		CoinUnits[] coinUnits = CoinUnits.values();
		for(CoinUnits coinUnit: coinUnits) {
			String unit = coinUnit.toString();
			List<CommonTicker> commonTickers = commonTickerRepository.findByCoinIdAndUnits(coinId, unit, sort);
				for(CommonTicker ticker: commonTickers) {
				if(unit.equals("USD") || unit.equals("USDT")) {
					double volume = Double.parseDouble(ticker.getBaseVolume24h());
					double total = volume * Double.parseDouble(ticker.getLast());
					totalVolume += volume;
					totalPriceUsd += total;
				} else if(unit.equals("BTC")){
					double btcPrice = getBtcToUsd();
					double volume = Double.parseDouble(ticker.getBaseVolume24h());
					double total = volume * (Double.parseDouble(ticker.getLast()) * btcPrice);
					totalVolume += volume;
					totalPriceUsd += total;
				}
				
				if(ticker.getExchangeName().equals("COIN RANKING")) {
					coinRankingVolume = ticker.getBaseVolume24h();
				}
			}
		}
		
		double vwapPriceUsd = 0;
		
		if(totalVolume > 0 && totalPriceUsd > 0) {
			vwapPriceUsd = totalPriceUsd/totalVolume;
		}
		
		double totalVolumeByUsd = totalVolume * vwapPriceUsd;
		coinEntry.setName(coinId);
		DecimalFormat format = null;
		if(vwapPriceUsd < 1.0) {
			format = new DecimalFormat("0.000000");
		} else {
			format = new DecimalFormat("0.00");
		}
		coinEntry.setVwapPriceUsd(Double.valueOf(format.format(vwapPriceUsd)));
		
		if(coinRankingVolume.equals("")) {
			coinEntry.setTotalVolumeUsd24h(totalVolumeByUsd);
		} else {
			coinEntry.setTotalVolumeUsd24h(Double.valueOf(coinRankingVolume));
		}
		
		return coinEntry;
	}
	
	public HashMap<String, CoinPriceSummary> getAllCoinEntry() {
		HashMap<String, CoinPriceSummary> coinMap = new HashMap<String, CoinPriceSummary>();
		List<CommonTicker> tickers = commonTickerRepository.findAll();
		
		for(CommonTicker ticker: tickers) {
			CoinPriceSummary coinSum = getCoinEntry(ticker.getCoinId());
			coinMap.put(ticker.getCoinId(), coinSum);
		}
		return coinMap;
	}
	
	private double getBtcToUsd() {
		Sort sort = new Sort(Sort.Direction.DESC, "timestamp");
		double totalVolume = 0;
		double totalPriceUsd = 0;
		List<CommonTicker> coinsUsd = commonTickerRepository.findByCoinIdAndUnits("BTC", "USD", sort);
		for(CommonTicker ticker: coinsUsd) {
			double volume = Double.parseDouble(ticker.getBaseVolume24h());
			double total = volume * Double.parseDouble(ticker.getLast());
			totalVolume += volume;
			totalPriceUsd += total;
		}
		
		List<CommonTicker> coinsUsdt = commonTickerRepository.findByCoinIdAndUnits("BTC", "USDT", sort);
		for(CommonTicker ticker: coinsUsdt) {
			double volume = Double.parseDouble(ticker.getBaseVolume24h());
			double total = volume * Double.parseDouble(ticker.getLast());
			totalVolume += volume;
			totalPriceUsd += total;
		}
		double vwapPriceUsd = 0;
		if(totalVolume > 0 && totalPriceUsd > 0) {
			vwapPriceUsd = totalPriceUsd/totalVolume;
		}
		
		return vwapPriceUsd;
	}
	
	public void insertCoin(CommonTicker coin) {
		if(coin == null) {
			return;
		}
		List<CommonTicker> tickers = commonTickerRepository.findByCoinIdAndUnitsAndExchangeName(coin.getCoinId(), coin.getUnits(), coin.getExchangeName());
		if(tickers.isEmpty()) {
			commonTickerRepository.insert(coin);
		} else {
			CommonTicker ticker = tickers.get(0);
			String id = ticker.getId();
			coin.setId(id);
			commonTickerRepository.save(coin);
		}
	}
}
