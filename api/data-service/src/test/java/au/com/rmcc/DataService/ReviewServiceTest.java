package au.com.rmcc.DataService;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.anymoove.common.model.ManifestCoin;
import au.com.anymoove.common.model.Review;
import au.com.anymoove.common.model.ReviewVote;
import au.com.anymoove.common.repository.ReviewRepository;
import au.com.anymoove.common.repository.ReviewVoteRepository;
import au.com.anymoove.common.service.ManifestCoinService;
import au.com.anymoove.common.service.ReviewService;

@RunWith(MockitoJUnitRunner.class)
public class ReviewServiceTest
{
	
  private Logger logger = LoggerFactory.getLogger(getClass());
	
   @Mock
   ReviewRepository reviewRepo;
   
   @Mock
   ManifestCoinService manifestService;
   
   @InjectMocks 
   ReviewService reviewService = new ReviewService();
   
   @Mock
   ReviewVoteRepository voteRepo;
   
   @Before
   public void setUp(){
       MockitoAnnotations.initMocks(this);
       Mockito.when(voteRepo.findAllByReviewIdAndType(Mockito.any(), Mockito.any())).thenReturn(getDummyReviewVote());
       
   }
   
   @Test
   public void testGetReviewById(){
      Mockito.when(reviewRepo.findById("0")).thenReturn(getDummyReview());
      Mockito.when(manifestService.getCoin("XMR")).thenReturn(getDummyManifestCoin());
      Review review = reviewService.getReview("0");
      logger.info("review: {}", review);
      assertEquals("Coin symbols don't match", review.getCoinSymbol(), "XMR");
   }
   
   private Optional<Review> getDummyReview() {
	   Review review = new Review();
	   review.setCoinSymbol("XMR");
	   review.setDate(new Date());
	   review.setDescription("This is a description for monero");
	   review.setRecommendation(Review.Recommendation.BUY);
	   Optional<Review> optionalReview = Optional.of(review);
	   return optionalReview;
   }
   
   private List<ReviewVote> getDummyReviewVote() {
	   List<ReviewVote> votes = new ArrayList<>();
	   votes.add(new ReviewVote());
	   
	   return votes;
   }
   
   private ManifestCoin getDummyManifestCoin() {
	   ManifestCoin coin = new ManifestCoin();
	   coin.setName("Monero");
	   coin.setImage("");
	   return coin;
   }
   
   
}
