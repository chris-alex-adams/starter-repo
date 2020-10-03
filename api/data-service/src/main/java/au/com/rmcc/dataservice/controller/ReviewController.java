package au.com.rmcc.dataservice.controller;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import au.com.anymoove.common.model.Review;
import au.com.anymoove.common.model.ReviewRequest;
import au.com.anymoove.common.model.ReviewVote;
import au.com.anymoove.common.model.ReviewVoteRequest;
import au.com.anymoove.common.service.ReviewService;
import au.com.rmcc.dataservice.model.ResponseMessage;

@RestController
@CrossOrigin
public class ReviewController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	ReviewService service;
	
	@RequestMapping("/reviews/new/{pageNum}")
	public String getAllNewReviews(@PathVariable @NotNull int pageNum) {
		List<Review> reviews = service.getAllNewReviews(pageNum);
		
	    Gson gson = new Gson();
	    String json = gson.toJson(reviews);
		return json;
	}
	
	@RequestMapping("/reviews/hot/{pageNum}")
	public String getAllHotReviews(@PathVariable @NotNull int pageNum) {
		List<Review> reviews = service.getAllHotReviews(pageNum, 86400);
		if(reviews.isEmpty()) {
			reviews = service.getAllHotReviews(pageNum, 604800);
			if(reviews.isEmpty()) {
				reviews = service.getAllHotReviews(pageNum, 2592000);
			}
		}
		
	    Gson gson = new Gson();
	    String json = gson.toJson(reviews);
		return json;
	}
	
	@RequestMapping("/reviews/weekly/{pageNum}")
	public String getAllWeeklyReviews(@PathVariable @NotNull int pageNum) {
		List<Review> reviews = service.getAllHotReviews(pageNum, 604800);
		
	    Gson gson = new Gson();
	    String json = gson.toJson(reviews);
		return json;
	}
	
	@RequestMapping("/reviews/monthly/{pageNum}")
	public String getAllMonthlyReviews(@PathVariable @NotNull int pageNum) {
		List<Review> reviews = service.getAllHotReviews(pageNum, 2592000);
		
	    Gson gson = new Gson();
	    String json = gson.toJson(reviews);
		return json;
	}
	
	@RequestMapping(value = "review/{id}")
    public String findById(@PathVariable @NotNull String id) {
		Review review = service.getReview(id);
		
	    Gson gson = new Gson();
	    String json = gson.toJson(review);
		return json;
	}
	
	@RequestMapping(value = "reviewVote/{id}")
    public String findReviewVoteByReviewId(@PathVariable @NotNull String id) {
		ReviewVote reviewVote = service.getUserReviewVote(id);
		
	    Gson gson = new Gson();
	    String json = gson.toJson(reviewVote);
		return json;
	}
	
	@RequestMapping(value = "reviews/{coinSymbol}/new/{pageNum}")
    public String findByCoinNew(@PathVariable @NotNull String coinSymbol, @PathVariable @NotNull int pageNum) {
		List<Review> reviews = service.getAllNewReviews(pageNum, coinSymbol);
		
	    Gson gson = new Gson();
	    String json = gson.toJson(reviews);
		return json;
	}
	
	@RequestMapping(value = "reviews/{coinSymbol}/hot/{pageNum}")
    public String findByCoinHot(@PathVariable @NotNull String coinSymbol, @PathVariable @NotNull int pageNum) {
		List<Review> reviews = service.getAllHotReviews(pageNum, coinSymbol, 86400);
		
	    Gson gson = new Gson();
	    String json = gson.toJson(reviews);
		return json;
	}
	
	@RequestMapping(value = "reviews/{coinSymbol}/weekly/{pageNum}")
    public String findByCoinWeekly(@PathVariable @NotNull String coinSymbol, @PathVariable @NotNull int pageNum) {
		List<Review> reviews = service.getAllHotReviews(pageNum, coinSymbol, 604800);
		
	    Gson gson = new Gson();
	    String json = gson.toJson(reviews);
		return json;
	}
	
	public boolean isUserAllowedToSubmitReview(String coinSymbol) {
		List<Review> reviews = service.getReviewsByUser(coinSymbol);
		if(!reviews.isEmpty()) {
			Review review = reviews.get(0);
			if(review.getAgeInSeconds() < 86400) {
				return false;
			}
		}
	    return true;
	}
	
	@RequestMapping(value = "reviews/{coinSymbol}/monthly/{pageNum}")
    public String findByCoinMonthly(@PathVariable @NotNull String coinSymbol, @PathVariable @NotNull int pageNum) {
		List<Review> reviews = service.getAllHotReviews(pageNum, coinSymbol, 2592000);
		
	    Gson gson = new Gson();
	    String json = gson.toJson(reviews);
		return json;
	}
	
    @PostMapping("/review/create")
    public ResponseEntity<String> createReview(@RequestBody ReviewRequest request) {
    	Gson gson = new Gson();
    	if(isUserAllowedToSubmitReview(request.getCoinSymbol())) {
        	logger.info("request: {}", request);
    	    return ResponseEntity.ok(gson.toJson(service.saveReview(request)));
    	} else {
    		ResponseMessage message = new ResponseMessage();
    		message.setStatus(HttpStatus.FORBIDDEN.value());
    		message.setMessage("Only one review per coin allowed a day. Please wait 24 hours before submitting again.");
    		
    		return ResponseEntity
    	            .status(HttpStatus.FORBIDDEN)
    	            .body(gson.toJson(message));
    	}

    }
    
    @PostMapping("/reviewVote/create")
    public ResponseEntity<String> createReviewVote(@RequestBody ReviewVoteRequest requestVote) {
    	logger.info("request: {}", requestVote);
    	service.saveReviewVote(requestVote);
    	return ResponseEntity.ok("Review vote Created");
    }
}
