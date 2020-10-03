package au.com.rmcc.dataload.model;

import lombok.Data;

@Data
public class BinanceTicker {
	private String symbol;
	private String priceChange;
	private String priceChangePercent;
	private String weightedAvgPrice;
	private String prevClosePrice;
	private String lastPrice;
	private String lastQty;
	private String bidPrice;
	private String bidQty;
	private String askPrice;
	private String askQty;
	private String openPrice;
	private String highPrice;
	private String lowPrice;
	private String volume;
	private String quoteVolume;
	private String openTime;
	private String closeTime;
	private String firstId;
	private String lastId;
	private String count;
	
}
