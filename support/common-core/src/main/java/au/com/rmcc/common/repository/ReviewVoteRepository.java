package au.com.rmcc.common.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import au.com.rmcc.common.model.ReviewVote;

@Component
public interface ReviewVoteRepository extends MongoRepository<ReviewVote, String> {
	List<ReviewVote> findAllByReviewIdAndType(String coinSymbol, String type);
	
	List<ReviewVote> findAllByUserIdAndReviewId(String userId, String reviewId);
	
	List<ReviewVote> findAllByReviewId(String reviewId);
}
