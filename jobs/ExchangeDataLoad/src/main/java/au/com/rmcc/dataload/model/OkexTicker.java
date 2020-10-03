package au.com.rmcc.dataload.model;

import lombok.Data;

@Data
public class OkexTicker {
	public String best_ask;
	public String best_bid;
	public String instrument_id;
	public String product_id;
	public String last;
	public String ask;
	public String bid;
	public String open_24h;
	public String high_24h;
	public String low_24h;
	public String base_volume_24h;
	public String timestamp;
	public String quote_volume_24h;
}
