package au.com.rmcc.common.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;

import au.com.rmcc.common.model.Review;

@Component
public interface ReviewRepository extends MongoRepository<Review, String> {
	List<Review> findByAgeInSecondsLessThanOrderByReviewVoteSummaryUpCountDesc(int ageInSeconds, Pageable pageable);
	
	Page<Review> findAllByCoinSymbol(String coinSymbol, Pageable pageable);
	
	List<Review> findAllByUserIdAndCoinSymbol(String userId, String coinSymbol, Pageable pageable);
	
	// Integer countByCoinSymbolAndLastUpdatedDateAfter(String coinSymbol, Date date); // This should be review not reviewVote
	
	Integer countByCoinSymbolAndAgeInSecondsLessThan(String coinSymbol, long ageInSeconds);
	
	Page<Review> findAllByCoinSymbolAndAgeInSecondsLessThanOrderByReviewVoteSummaryUpCountDesc(String coinSymbol, int ageInSeconds, Pageable pageable);
}
