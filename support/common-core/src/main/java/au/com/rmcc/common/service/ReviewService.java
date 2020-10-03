package au.com.rmcc.common.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import au.com.rmcc.common.model.ManifestCoin;
import au.com.rmcc.common.model.Review;
import au.com.rmcc.common.model.ReviewRequest;
import au.com.rmcc.common.model.ReviewVote;
import au.com.rmcc.common.model.ReviewVoteRequest;
import au.com.rmcc.common.model.ReviewVoteSummary;
import au.com.rmcc.common.model.User;
import au.com.rmcc.common.repository.ReviewRepository;
import au.com.rmcc.common.repository.ReviewVoteRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReviewService {
	
	private final int PAGE_SIZE = 15;
	
	@Autowired
	ReviewRepository reviewRepo;
	
	@Autowired
	MongoUserDetailsService userService;
	
	@Autowired
	ManifestCoinService manifestService;
	
	@Autowired
	ReviewVoteRepository voteRepo;
	
	public List<Review> getAllNewReviews(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by(new Order(Direction.DESC, "date")));
		Page<Review> page = reviewRepo.findAll(pageable);
		List<Review> reviews = clean(page.getContent());

	    return reviews;
	}
	
	public List<Review> getAllHotReviews(int pageNumber, int ageInSeconds) {
		Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
		List<Review> reviews = clean(reviewRepo.findByAgeInSecondsLessThanOrderByReviewVoteSummaryUpCountDesc(ageInSeconds, pageable));
	    return reviews;
	}
	
	public List<Review> getAllNewReviews(int pageNumber, String coinSymbol) {
		Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by(new Order(Direction.DESC, "date")));
		Page<Review> page = reviewRepo.findAllByCoinSymbol(coinSymbol, pageable);
		List<Review> reviews = clean(page.getContent());

	    return reviews;
	}
	
	public List<Review> getAllHotReviews(int pageNumber, String coinSymbol, int ageInSeconds) {
		Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
		Page<Review> page = reviewRepo.findAllByCoinSymbolAndAgeInSecondsLessThanOrderByReviewVoteSummaryUpCountDesc(coinSymbol, ageInSeconds, pageable);
		List<Review> reviews = clean(page.getContent());

	    return reviews;
	}
	
	public Review getReview(String id) {
		Optional<Review> review = reviewRepo.findById(id);
		if(review.isPresent()) {
			return clean(review.get());
		} else {
			
			return null;
		}
	}
	
	public List<Review> getReviewsByUser(String coinSymbol) {
		String userId = userService.getCurrentUserId();
		return getReviewsByUser(userId, coinSymbol);
	}
	
	public List<Review> getReviewsByUser(String userId, String coinSymbol) {
		Pageable pageable = PageRequest.of(0, PAGE_SIZE, Sort.by(new Order(Direction.DESC, "date")));
		List<Review> reviews = reviewRepo.findAllByUserIdAndCoinSymbol(userId, coinSymbol, pageable);
		return clean(reviews);
	}
	
	public List<Review> clean(List<Review> reviews) {
		List<Review> cleanedReviews = new ArrayList<Review>();
		for(Review review: reviews) {
			Review cleanedReview = clean(review);
			if(cleanedReview != null) {
				cleanedReviews.add(cleanedReview);
			}
		}
		
		return cleanedReviews;
	}
	
	private Review clean(Review review) {
		Review cleanedReview = review;
		ManifestCoin manifestCoin = manifestService.getCoin(review.getCoinSymbol());
		if(manifestCoin != null) {
			cleanedReview.setCoinTitle(manifestCoin.getName());
			cleanedReview.setImage(manifestCoin.getIconImage());
			cleanedReview.setDescription(review.getDescription().trim());
			LocalDateTime postDateTime = review.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			LocalDateTime currentDateTime = LocalDateTime.now();
			long ageInSeconds = ChronoUnit.SECONDS.between(postDateTime, currentDateTime);
			cleanedReview.setAgeInSeconds(ageInSeconds);
			cleanedReview.setReviewVoteSummary(getReviewVoteSummary(review.getId()));
			if(review.getUserId() != null) {
				User user = userService.findById(review.getUserId());
				if(user != null) {
					cleanedReview.setUserId(user.getUsername());
				}
			} else {
				cleanedReview.setUserId("Satoshi Nakamoto");
			}

			
		} else {
			return null;
		}
		
		return cleanedReview;
	}
	
	public Review saveReview(ReviewRequest reviewRequest) {
		String userId = userService.getCurrentUserId();
		log.info("saving review to user: {}", userId);
		if(!userId.isEmpty()) {
	    	Review review = new Review();
	    	review.setCoinSymbol(reviewRequest.getCoinSymbol());
	    	review.setDate(new Date());
	    	review.setDescription(reviewRequest.getDescription());
	    	review.setTitle(reviewRequest.getTitle());
	    	review.setRecommendation(reviewRequest.getRecommendation());
	    	review.setUserId(userId);
	    	
			return reviewRepo.save(review);
		}
		return null;
	}
	
	public void saveReviewVote(ReviewVoteRequest request) {
		String userId = userService.getCurrentUserId();
		List<ReviewVote> votes  = voteRepo.findAllByUserIdAndReviewId(userId, request.getReviewId());
		
		if(!userId.isEmpty()) {
			ReviewVote vote = new ReviewVote();
			vote.setType(request.getType());
			if(!votes.isEmpty()) {
				vote.setId(votes.get(0).getId());
				if(votes.get(0) != null) {
					if(votes.get(0).getType().equals(request.getType())) {
						vote.setType(ReviewVote.VoteType.NONE);
					}
				}
			}
			vote.setLastUpdatedDate(new Date());
			vote.setReviewId(request.getReviewId());
			vote.setUserId(userId);
			vote.setCoinSymbol(request.getCoinSymbol());
			voteRepo.save(vote);
		} else {
			log.warn("user id for the review vote request is empty");
		}
	}
	
	public ReviewVote getUserReviewVote(String reviewId) {
		ReviewVote reviewVote = new ReviewVote();
		reviewVote.setType(ReviewVote.VoteType.NONE);
		String userId = userService.getCurrentUserId();
		if(StringUtils.isEmpty(reviewId) || StringUtils.isEmpty(userId)) {
			return reviewVote;
		}
		List<ReviewVote> votes = voteRepo.findAllByUserIdAndReviewId(userId, reviewId);
		if(!votes.isEmpty()) {
			return votes.get(0);
		}
		return reviewVote;
	}
	
	public ReviewVoteSummary getReviewVoteSummary(String reviewId) {
		ReviewVoteSummary voteSum = new ReviewVoteSummary();
		voteSum.setCoinSymbol(reviewId);
		voteSum.setDownCount(voteRepo.findAllByReviewIdAndType(reviewId, ReviewVote.VoteType.DOWN.toString()).size());
		voteSum.setUpCount(voteRepo.findAllByReviewIdAndType(reviewId, ReviewVote.VoteType.UP.toString()).size());
		
		return voteSum;
	}
	
	public int countReviewBySymbolLastDay(String coinSymbol) {
		return reviewRepo.countByCoinSymbolAndAgeInSecondsLessThan(coinSymbol, 86400);
	}

}
