package gr.uoa.di.scan.thesis.social;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.FeedOperations;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.api.PagingParameters;
import org.springframework.stereotype.Component;

@Component
public class FacebookUtils {
	
	private static final int POSTS_LIMIT = 10;
	
	public User getFbUser(String accessToken) {
		 
		FacebookTemplate facebook = new FacebookTemplate(accessToken);
		User profile = facebook.fetchObject("me", User.class, "id", "name", "first_name", "last_name","link", "email");

		return profile;
	}
	
	public String getLongLivedToken(String accessToken) {
		FacebookTemplate facebook = new FacebookTemplate(accessToken);
		String url = "/oauth/access_token?grant_type=fb_exchange_token&client_id={app-id}&client_secret={app-secret}&fb_exchange_token={short-lived-token}";
		Map<String,String> args = new HashMap<String,String>();
		args.put("app-id", "");
		args.put("app-secret", "");
		args.put("short-lived-token", accessToken);
		String result = facebook.restOperations().getForObject(url, String.class, args);
		System.out.println("Result:" + result);
		return "";
	}
	
	public List<Post> getPosts(List<String> accessTokenList){
		List<Post> posts = new ArrayList<>();
		
		for( String token : accessTokenList) {
			posts.addAll(retrieveUserPosts(token, null, null));
		}

		return posts;
	}
	
	public List<Post> getPosts(List<String> accessTokenList, Long since, Long until) {
		
		TreeSet<Post> allPosts = new TreeSet<Post>();
		
		for( String token : accessTokenList) {
			
			for (Post post : retrieveUserPosts(token, since, until)) {
				allPosts.add(post);
				if(allPosts.size() > POSTS_LIMIT)
					allPosts.pollLast();
			}
		}
		return new ArrayList<Post>(allPosts);
	}
	
	public List<Post> getNewPosts(List<String> accessTokenList, Long since, Long until) {
		
		List<Post> posts = new ArrayList<>();
		
		for( String token : accessTokenList) {
			
			posts.addAll(retrieveUserPosts(token, since, until));
		}

		return posts;
	}
	
	private List<Post> retrieveUserPosts(String accessToken, Long since, Long until) {
		 
		FacebookTemplate facebook = new FacebookTemplate(accessToken);
		FeedOperations feedOps = facebook.feedOperations();
		PagingParameters pagingParameters = new PagingParameters(POSTS_LIMIT, 0, since, until);
		List<Post> posts = feedOps.getFeed();
		return posts;
	}
	

}
