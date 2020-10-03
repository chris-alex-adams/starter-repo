package au.com.rmcc.dataservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import au.com.anymoove.common.model.CoinProfile;
import au.com.anymoove.common.service.CoinProfileService;

@RestController
@CrossOrigin
public class CoinController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	CoinProfileService coinProfileService;
	
	@RequestMapping("/coin/{name}")
	public String getCoins(@PathVariable String name) {
		CoinProfile coinProfile = coinProfileService.getCoin(name);
		
	    Gson gson = new Gson();
	    String json = gson.toJson(coinProfile);
		return json;
		
	}
	
	@RequestMapping("/coins/orderByReviews")
	public String getAllCoinsOrderByReviews() {
		List<CoinProfile> coinProfiles = coinProfileService.getAllCoinsOrderByReviews();
		logger.info("number of coin profiles : {}", coinProfiles.size());
	    Gson gson = new Gson();
	    String json = gson.toJson(coinProfiles);
		return json;
	}
	
	@RequestMapping("/coins/")
	public String getAllCoins() {
		List<CoinProfile> coinProfiles = coinProfileService.getAllCoinsFilter();
		logger.info("number of coin profiles : {}", coinProfiles.size());
	    Gson gson = new Gson();
	    String json = gson.toJson(coinProfiles);
		return json;
	}
	
	@RequestMapping("/health")
	public String health() {
		return "DataService works!!!";
	}
	
	@RequestMapping("/coins/count")
	public long count() {
		return coinProfileService.countCoins();
	}

}
