package au.com.rmcc.ExchangeDataLoad;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import au.com.rmcc.dataload.model.CoinbeneParentTicker;
import au.com.rmcc.dataload.service.CoinDictionaryStorageService;

public class CoinDictionaryStorageServiceTest {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	CoinDictionaryStorageService service = null;
	
	@Before
	public void init() {
		service = new CoinDictionaryStorageService();
	}
	
	@Test
	public void test() {
		service.getCoinName("");
	}
	
	/**
	 * 
	 * @return
	 
	
	private CoinbeneParentTicker getTestTicker() {
		String fileName = getClass().getResource("/coinbeneTicker2").getPath();
		
		try {
			String tickerData = TestingUtils.readFile(fileName);
			Gson gson = new Gson();
			logger.info(tickerData);
			CoinbeneParentTicker tickers = gson.fromJson(tickerData, CoinbeneParentTicker.class);
			return tickers;
		} catch (IOException e) {
			logger.error("Can't load the test file with the error: {}", e);
		}
		return null;
	}
	
	@Test
	public void testParsing() {
		CoinbeneParentTicker parentTicker = getTestTicker();
		if(parentTicker == null) {
			fail();
		}
		List<CoinbeneTicker> tickers = parentTicker.getTicker();
		if(tickers == null || tickers.size() == 0) {
			logger.error("tickers are empty or null: {}", parentTicker);
			fail();
		}
		logger.debug("tickers : {}", parentTicker);
		CommonTicker commonTicker = service.parseToCommonTicker(tickers.get(0));
		assertEquals("ABBC",commonTicker.getCoinId());
		assertEquals("BTC",commonTicker.getUnits());
		assertEquals("0.00001779",commonTicker.getAsk());
	}
	*/
}
