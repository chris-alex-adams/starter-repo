package au.com.rmcc.common.model;

import au.com.rmcc.common.model.Review.Recommendation;
import lombok.Data;

@Data
public class ReviewRequest {
	private String title;
	private String coinSymbol;
	private String description;
	private Recommendation recommendation; 
}
