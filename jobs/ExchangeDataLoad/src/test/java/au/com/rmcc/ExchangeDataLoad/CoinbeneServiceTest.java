package au.com.rmcc.ExchangeDataLoad;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import au.com.anymoove.common.model.CommonTicker;
import au.com.rmcc.dataload.model.CoinbeneParentTicker;
import au.com.rmcc.dataload.model.CoinbeneTicker;
import au.com.rmcc.dataload.service.CoinbeneService;

public class CoinbeneServiceTest {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	CoinbeneService service = null;
	
	@Before
	public void init() {
		service = new CoinbeneService();
	}
	
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
		assertTrue(Double.parseDouble(commonTicker.getAsk()) > 0);
	}
}
