package au.com.rmcc.dataservice.model;

import java.util.List;

import au.com.anymoove.common.model.Review;
import lombok.Data;

@Data
public class ReviewListResponse {
	List<Review> reviews;
	int totalCount = 0;
	
}
