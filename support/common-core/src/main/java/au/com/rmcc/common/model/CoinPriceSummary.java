package au.com.rmcc.common.model;

import lombok.Data;

@Data
public class CoinPriceSummary {
	private String name;
	private double vwapPriceUsd;
	private double totalVolumeUsd24h;
}
