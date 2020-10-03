package au.com.rmcc.common.model;

import org.springframework.data.annotation.Id;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommonTicker {
    @Id
    public String id;
	private String bestAsk;
	private String bestBid;
	private String coinId;
	private String units;
	private String last;
	private String ask;
	private String bid;
	private String open24h;
	private String high24h;
	private String low24h;
	private String baseVolume24h;
	private String timestamp;
	private String quoteVolume24h;
	private String exchangeName;
}
