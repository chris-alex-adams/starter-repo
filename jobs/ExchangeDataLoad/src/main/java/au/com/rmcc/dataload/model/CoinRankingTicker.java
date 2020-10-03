package au.com.rmcc.dataload.model;

import java.util.List;

import lombok.Data;

@Data
public class CoinRankingTicker {
	private double id;
	private String slug;
	private String symbol;
	private String name;
	private String description;
	private String color;
	private String iconType;
	private String iconUrl;
	private String websiteUrl;
	private boolean confirmedSupply;
	private String type;
	private double volume;
	private double marketCap;
	private double price;
	private double circulatingSupply;
	private double totalSupply;
	private double firstSeen;
	private double change;
	private int rank;
	private List<String> history;
	private CoinRankingAllTimeHigh allTimeHigh;
	private boolean penalty;
	 
}
