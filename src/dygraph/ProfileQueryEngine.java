package dygraph;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.exception.FacebookNetworkException;
import com.restfb.types.Post;
import com.restfb.types.User;

public class ProfileQueryEngine {
	
	final private static String DEFAULT_ACCESS_TOKEN = "AAACEdEose0cBAFmWozoq2uZApTPKrWuWQVskge5MU3ZA38PMY7M7zD1gWtAD2xH8zORzUQLQxk0KDfB6bMg4KPk4HD76zd1BNiPl7UsKRaPcBcvuaK";
	public static FacebookClient FB = new DefaultFacebookClient(DEFAULT_ACCESS_TOKEN);
	final static String DEFAULT_PICTURE_URL = "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-snc4/174597_20531316728_2866555_q.jpg"; 
	final public static Set<User> MY_FRIENDS = new HashSet<User>(300);
	static {
		fetchPersonalFriends();
	}
	
	User me;
	String profileID;
	boolean isValid;
	Iterator<List<Post>> myFeed;
	
	public static void main(String[] args) {
		for (User friend : MY_FRIENDS) {
			System.out.println(friend.getName() + " (ID:" + friend.getId() + ")");
		}
		System.out.println("Total: " + MY_FRIENDS.size());
	}
	
	ProfileQueryEngine() {
		this.profileID = "1110316640";
		try {
			me = FB.fetchObject(profileID,User.class);
			isValid = true;
		} catch (FacebookNetworkException e) {
			isValid = false;
		}
		initialize();
	}
	
	public ProfileQueryEngine(String profileID) {
		
		this.profileID = profileID;
		User user = FB.fetchObject(profileID,User.class);
		if (user.getName().equals("")) {
			isValid = false;
		} else {
			isValid = true;
		}
		initialize();
	}
	
	private void initialize() {
		if (isValid) {
			myFeed = FB.fetchConnection(
					profileID + "/feed/",Post.class).iterator();
		}
	}
	
	static void fetchPersonalFriends() {
		Connection<User> myFriends = FB.fetchConnection("me/friends", User.class);
		for (List<User> friendList : myFriends) {
			for (User friend : friendList) {
				MY_FRIENDS.add(friend);
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
	
	public BufferedImage fetchPicture() {
		
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
