package au.com.rmcc.common.model;

import lombok.Data;

@Data
public class ReviewVoteSummary {
	int upCount;
	int downCount;
	private String coinSymbol;
}
