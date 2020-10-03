package au.com.rmcc.common_core;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import au.com.rmcc.common.model.CoinPriceSummary;
import au.com.rmcc.common.service.CommonTickerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommonCoreApplication.class})
@TestPropertySource(properties = {
	    "db.mongo.ip=127.0.0.1",
	    "db.mongo.port=27017",
	    "db.mongo.name=test"
	})
public class CommonTickerServiceTest {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	CommonTickerService service;
	
	@Test
	public void testTubeCoin() {
		CoinPriceSummary coin = service.getCoinEntry("TUBE");
		assertTrue(coin.getTotalVolumeUsd24h() > 0);
		logger.info("coin : {}",coin);
	}
}
