package au.com.rmcc.dataload.model;

import java.util.List;

import lombok.Data;

@Data
public class CoinRankingData {
	private CoinRankingStats stats;
	private CoinRankingBase base;
	private List<CoinRankingTicker> coins;
	
}
