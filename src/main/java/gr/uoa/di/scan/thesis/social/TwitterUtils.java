package gr.uoa.di.scan.thesis.social;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

public class TwitterUtils {
	
	@Autowired
	TwitterTemplate twitter;
	
	public TwitterUtils(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
		
		twitter =  new TwitterTemplate(consumerKey,consumerSecret,accessToken,accessTokenSecret);
	}
	
	
	private List<Tweet> retrieveTweets(String userName) {
		
		TimelineOperations timelineOps = twitter.timelineOperations();
	    List<Tweet> results = timelineOps.getUserTimeline(userName);
	    
	    return results;
	    
	}
	
}
