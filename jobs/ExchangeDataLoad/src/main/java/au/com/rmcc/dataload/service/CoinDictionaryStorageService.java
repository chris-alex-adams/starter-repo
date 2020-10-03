package au.com.rmcc.dataload.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CoinDictionaryStorageService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	public String getCoinName(String symbol) {

		return "";
	}
	
	public String getCoinImage(String symbol) {
		return "";
	}
}
