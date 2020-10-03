package au.com.rmcc.dataload.model;

import lombok.Data;

@Data
public class CoinRankingStats {
	private double total;
	private double offset;
	private double limit;
	private String order;
	private String base;
}
