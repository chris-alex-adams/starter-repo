package au.com.rmcc.ExchangeDataLoad;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import au.com.anymoove.common.model.CommonTicker;
import au.com.rmcc.dataload.model.OkexTicker;
import au.com.rmcc.dataload.service.OkexService;

public class OkexServiceTest {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	OkexService okexService = null;
	OkexTicker okexTicker = null;
	OkexTicker ticker = null;
	@Before
	public void init() {
		okexService = new OkexService();
	}
	
	private OkexTicker getTestTicker() {
		String fileName = getClass().getResource("/okexTicker.txt").getPath();
		
		try {
			String tickerData = TestingUtils.readFile(fileName);
			Gson gson = new Gson();
			logger.info(tickerData);
			OkexTicker ticker = gson.fromJson(tickerData, OkexTicker.class);
			return ticker;
		} catch (IOException e) {
			logger.error("Can't load the test file with the error: {}", e);
		}
		return null;
	}
	
	@Test
	public void testParsing() {
		OkexTicker ticker = getTestTicker();
		if(ticker == null) {
			fail();
		}
		CommonTicker commonTicker = okexService.parseToCommonTicker(ticker);
		assertEquals("LTC",commonTicker.getCoinId());
		assertEquals("BTC",commonTicker.getUnits());
		assertTrue(Double.parseDouble(commonTicker.getBestAsk()) > 0);
	}
}
