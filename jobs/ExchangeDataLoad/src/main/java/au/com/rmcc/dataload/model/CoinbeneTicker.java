package au.com.rmcc.dataload.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.ToString;

@ToString
public class CoinbeneTicker {

    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("24hrHigh")
    private String _24hrHigh;
    @JsonProperty("last")
    private String last;
    @JsonProperty("24hrVol")
    private String _24hrVol;
    @JsonProperty("ask")
    private String ask;
    @JsonProperty("24hrLow")
    private String _24hrLow;
    @JsonProperty("bid")
    private String bid;
    @JsonProperty("24hrAmt")
    private String _24hrAmt;

    public CoinbeneTicker() {
    }

    /**
     * 
     * @param _24hrLow
     * @param last
     * @param symbol
     * @param _24hrVol
     * @param _24hrHigh
     * @param ask
     * @param _24hrAmt
     * @param bid
     */
    public CoinbeneTicker(String symbol, String _24hrHigh, String last, String _24hrVol, String ask, String _24hrLow, String bid, String _24hrAmt) {
        super();
        this.symbol = symbol;
        this._24hrHigh = _24hrHigh;
        this.last = last;
        this._24hrVol = _24hrVol;
        this.ask = ask;
        this._24hrLow = _24hrLow;
        this.bid = bid;
        this._24hrAmt = _24hrAmt;
    }

    @JsonProperty("symbol")
    public String getSymbol() {
        return symbol;
    }

    @JsonProperty("symbol")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty("24hrHigh")
    public String get24hrHigh() {
        return _24hrHigh;
    }

    @JsonProperty("24hrHigh")
    public void set24hrHigh(String _24hrHigh) {
        this._24hrHigh = _24hrHigh;
    }

    @JsonProperty("last")
    public String getLast() {
        return last;
    }

    @JsonProperty("last")
    public void setLast(String last) {
        this.last = last;
    }

    @JsonProperty("24hrVol")
    public String get24hrVol() {
        return _24hrVol;
    }

    @JsonProperty("24hrVol")
    public void set24hrVol(String _24hrVol) {
        this._24hrVol = _24hrVol;
    }

    @JsonProperty("ask")
    public String getAsk() {
        return ask;
    }

    @JsonProperty("ask")
    public void setAsk(String ask) {
        this.ask = ask;
    }

    @JsonProperty("24hrLow")
    public String get24hrLow() {
        return _24hrLow;
    }

    @JsonProperty("24hrLow")
    public void set24hrLow(String _24hrLow) {
        this._24hrLow = _24hrLow;
    }

    @JsonProperty("bid")
    public String getBid() {
        return bid;
    }

    @JsonProperty("bid")
    public void setBid(String bid) {
        this.bid = bid;
    }

    @JsonProperty("24hrAmt")
    public String get24hrAmt() {
        return _24hrAmt;
    }

    @JsonProperty("24hrAmt")
    public void set24hrAmt(String _24hrAmt) {
        this._24hrAmt = _24hrAmt;
    }
}
