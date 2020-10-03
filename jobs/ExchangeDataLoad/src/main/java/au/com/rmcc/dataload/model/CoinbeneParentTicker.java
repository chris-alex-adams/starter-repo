package au.com.rmcc.dataload.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.ToString;

@ToString
public class CoinbeneParentTicker {
	@JsonProperty("ticker")
	List<CoinbeneTicker> ticker;
	
    @JsonProperty("ticker")
    public List<CoinbeneTicker> getTicker() {
        return ticker;
    }

    @JsonProperty("ticker")
    public void setTicker(List<CoinbeneTicker> ticker) {
        this.ticker = ticker;
    }
}
