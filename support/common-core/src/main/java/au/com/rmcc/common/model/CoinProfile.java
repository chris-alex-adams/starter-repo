package au.com.rmcc.common.model;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoinProfile {
    @Id
    private String id;
	private String symbol;
	private CoinPriceSummary coinSummary;
	private ManifestCoin manifestCoin;
	private int reviewsLastDay;
}
