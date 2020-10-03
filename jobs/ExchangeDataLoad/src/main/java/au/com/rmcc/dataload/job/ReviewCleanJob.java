package au.com.rmcc.dataload.job;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.com.anymoove.common.model.Review;
import au.com.anymoove.common.repository.ReviewRepository;
import au.com.anymoove.common.service.ReviewService;

@Component
public class ReviewCleanJob implements Job {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	ReviewService service;
	
	@Autowired
	ReviewRepository repo;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("Job ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());
		int pageNum = 0;
		List<Review> reviews = service.getAllNewReviews(pageNum);
		while(!reviews.isEmpty()) {
			pageNum++;
			List<Review> cleanedReviews = service.clean(reviews);
			repo.saveAll(cleanedReviews);
			reviews = service.getAllNewReviews(pageNum);
		}
		
		logger.info("Next job scheduled @ {}", context.getNextFireTime());
	}

}
