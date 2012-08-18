package example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.dict.Entry;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Post;
import com.restfb.types.StatusMessage;
import com.restfb.types.User;

import dygraph.ProfileQueryEngine;

public class FacebookStatusExample {
	
	public static void main(String[] args) {
		
	   /* Go to https://developers.facebook.com/tools/explorer to get an updated access token. */
	   String ACCESS_TOKEN = "AAACEdEose0cBAPrN1H617iAZBp7DDkp1avhnVND3x68XkLO3PV3xe0BCF0YHLXZCLQYzEIuFL3sxbQudj9IXlBcCybtbFNzIpK4ondqELhkOk8NWBT";
	   FacebookClient fb = new DefaultFacebookClient(ACCESS_TOKEN);
	   Map<String,String> friends = new HashMap<String,String>(300);
	   Connection<User> myFriends = fb.fetchConnection("me/friends", User.class);
	   for (List<User> friendList : myFriends) {
		   for (User friend : friendList) {
			   friends.put(friend.getId(),friend.getName());
		   }
	   }
	   System.out.println("Friend data loaded...");
	   long startTime = System.nanoTime();
	   Connection<Post> myStatusConnection = fb.fetchConnection("730147601/statuses",Post.class);
	   for (Post status : myStatusConnection.getData()) {
		   System.out.println("Status Message: " + removeTags(status.getMessage(),friends));
	   }
       
       System.out.println("Total time: " + (System.nanoTime() - startTime)/1000000f);
	}
	
	static private String removeTags(String message, Map<String,String> friends) {
		for (String friend : friends.values()) {
			message = message.replaceFirst(friend, "");
		}
		return message;
	}
}

