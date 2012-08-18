package dygraph;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import util.dict.Entry;
import util.misc.ImageLibrary;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.exception.FacebookNetworkException;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Post;
import com.restfb.types.StatusMessage;
import com.restfb.types.User;

public class ProfileQueryEngine {
	
	public static FacebookClient FB = new DefaultFacebookClient(DygraphApplet.DEBUG_TOKEN);
	final static String DEFAULT_PICTURE_URL = "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-snc4/174597_20531316728_2866555_q.jpg";
	final public static Map<String,String> MY_FRIENDS = new HashMap<String,String>(300);
	public static Entry<String,String> CURRENT_USER;
	
	final private static String[] FAVORITES_CATEGORIES = {"music","books","movies","activities","interests"};
	
	User user;
	StatusMessage[] myStatuses;
	String profileID;
	boolean isValid;
	Iterator<List<Post>> myFeed;
	BufferedImage image;
	
	public static void main(String[] args) {
		/* Sample code for using ProfileQueryEngine */
//		ProfileQueryEngine.fetchFriendData();
//		for (String id : MY_FRIENDS.keySet()) {
//			System.out.println(MY_FRIENDS.get(id) + " (ID:" + id + ")");
//		}
//		System.out.println("Total: " + MY_FRIENDS.size());
//		
//		ProfileQueryEngine pqe = new ProfileQueryEngine("1110316640");
//		List<Post> posts = pqe.fetchNextPosts();
//		for (Post post : posts) {
//			System.out.println(FacebookUtil.toGraphData(post) + "\n\n");
//		}
//		ProfileQueryEngine pqe = new ProfileQueryEngine("1110316640");
//		pqe.fetchFavorites();
	}
	
	public ProfileQueryEngine(String profileID) {
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
	
	public static void loadFriendData() {
		User me = FB.fetchObject("me", User.class);
		ProfileQueryEngine.CURRENT_USER = new Entry<String,String>(me.getId(),me.getName());
		Connection<User> myFriends = FB.fetchConnection("me/friends", User.class);
		for (List<User> friendList : myFriends) {
			for (User friend : friendList) {
				MY_FRIENDS.put(friend.getId(),friend.getName());
			}
		}
	}
	
	private String removeTags(String message, Map<String,String> friends) {
		for (String friend : friends.values()) {
			message = message.replaceFirst(friend, "");
		}
		return message;
	}
	
	public List<Post> fetchNextPosts() {
		try {
			List<Post> myWall = myFeed.next();
			return myWall;
		} catch (NullPointerException e) {
		}
		return null;
	}
	
	@Deprecated
	public void fetchFavorites() {
		for (String category : FAVORITES_CATEGORIES) {
			Connection<NamedFacebookType> itemConn = FB.fetchConnection(
					profileID + "/" + category,NamedFacebookType.class);
			for (List<NamedFacebookType> itemList : itemConn) {
				for (NamedFacebookType item : itemList) {
					System.out.println(category + ": " + item.getName() + " (" + item.getId() + ")");
				}
			}
		}
	}
	
	public StatusMessage[] fetchStatuses() {
		if (myStatuses == null) {
		    Connection<StatusMessage> myStatusConnection = 
		    		FB.fetchConnection(profileID + "/statuses",StatusMessage.class);
		    myStatuses = myStatusConnection.getData().toArray(
		    		new StatusMessage[myStatusConnection.getData().size()]);
		}
		return myStatuses;
	}
	
	public BufferedImage fetchPicture() {
		if (image == null) {
			image = ImageLibrary.grabImage("https://graph.facebook.com/" + profileID + "/picture");
		}
		return image;
	}
	
}
