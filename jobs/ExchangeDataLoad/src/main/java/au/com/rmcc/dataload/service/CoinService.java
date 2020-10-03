package au.com.rmcc.dataload.service;

import java.util.List;

import au.com.anymoove.common.model.CommonTicker;

public interface CoinService <T> {
	public List<CommonTicker> getAllTickers();
	public CommonTicker parseToCommonTicker(T ticker);
}
