package au.com.rmcc.common.model;

import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class ReviewVote {
    @Id
    private String id;
	private VoteType type;
	private String userId;
	private String reviewId;
	private String coinSymbol;
	private Date lastUpdatedDate;
	
	public enum  VoteType {
	    UP,
	    DOWN,
	    NONE
	}
}
