package au.com.rmcc.common.model;

import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Review {
    @Id
    private String id;
	private Date date;
	private String userId;
	private String coinSymbol;
	private String coinTitle;
	private String title;
	private String description;
	private String image;
	private long ageInSeconds = 0;
	private Recommendation recommendation; 
	private ReviewVoteSummary reviewVoteSummary;
	private ReviewVote userVote;
	
	public enum Recommendation {
	    SELL,
	    HOLD,
	    BUY;
	}

}
