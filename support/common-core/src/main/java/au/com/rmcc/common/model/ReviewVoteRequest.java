package au.com.rmcc.common.model;

import au.com.rmcc.common.model.ReviewVote.VoteType;
import lombok.Data;

@Data
public class ReviewVoteRequest {
	private VoteType type;
	private String reviewId;
	private String coinSymbol;
}
