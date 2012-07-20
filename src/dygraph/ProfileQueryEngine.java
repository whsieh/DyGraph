package dygraph;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import util.dict.Entry;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.exception.FacebookNetworkException;
import com.restfb.types.Comment;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Post;
import com.restfb.types.User;

public class ProfileQueryEngine {
	
	final private static String DEFAULT_ACCESS_TOKEN = "AAAG4zd1akV4BAA1dEQlzuCIZBp46BrpO3nmGgodszuz4JBsVvqYe4ZBtkwkhVPJeV3YdwzJU5MHd3ZAJl0mb33LLr0JD0iOMLAqHDp98UOEnuNvWaVC";
	public static FacebookClient FB = new DefaultFacebookClient(DEFAULT_ACCESS_TOKEN);
	final static String DEFAULT_PICTURE_URL = "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-snc4/174597_20531316728_2866555_q.jpg";
	final public static Map<String,String> MY_FRIENDS = new HashMap<String,String>(300);
	public static Entry<String,String> CURRENT_USER;
	
	User user;
	String profileID;
	boolean isValid;
	Iterator<List<Post>> myFeed;
	
	public static void main(String[] args) {
		
		/* Finding friends */
		ProfileQueryEngine.fetchFriendData();
		for (String id : MY_FRIENDS.keySet()) {
			System.out.println(MY_FRIENDS.get(id) + " (ID:" + id + ")");
		}
		System.out.println("Total: " + MY_FRIENDS.size());
		
		/* Parsing post data */
		ProfileQueryEngine pqe = new ProfileQueryEngine("1110316640");
		List<Post> posts = pqe.fetchNextPosts();
		for (Post post : posts) {
			System.out.println(FacebookUtil.toGraphData(post) + "\n\n");
		}
	}
	
	ProfileQueryEngine(String profileID) {
		this.profileID = profileID;
		try {
			user = FB.fetchObject(profileID,User.class);
			isValid = true;
		} catch (FacebookNetworkException e) {
			isValid = false;
		}
		initialize();
	}
	
	private void initialize() {
		if (isValid) {
			myFeed = FB.fetchConnection(
					profileID + "/feed/",Post.class).iterator();
		}
	}
	
	public static void fetchFriendData() {
		User me = FB.fetchObject("me", User.class);
		ProfileQueryEngine.CURRENT_USER = new Entry<String,String>(me.getId(),me.getName());
		Connection<User> myFriends = FB.fetchConnection("me/friends", User.class);
		for (List<User> friendList : myFriends) {
			for (User friend : friendList) {
				MY_FRIENDS.put(friend.getId(),friend.getName());
			}
		}
	}
	
	public List<Post> fetchNextPosts() {
		if (myFeed.hasNext()) {
			List<Post> myWall = myFeed.next();
			return myWall;
		}
		return null;
	}
	
	public static BufferedImage fetchPicture(String profileID) {
		
		try {
			URL imgURL = new URL("https://graph.facebook.com/" + profileID + "/picture");
			try {
				return ImageIO.read(imgURL);
			} catch (IOException e) {
				return null;
			}
		} catch (MalformedURLException e) {
			return null;
		}
	}
	
}
