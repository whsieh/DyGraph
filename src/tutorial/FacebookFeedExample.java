package tutorial;

import java.util.List;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Comment;
import com.restfb.types.Post;

public class FacebookFeedExample {

    
    /**
     * This testing method will query the user's facebook feed for information about posts, for a maximum of  MAX_TIME_MS milliseconds,
     * and then output some basic details of each post.
     */
    public static void main(String[] args) {
        
        /* Go to https://developers.facebook.com/tools/explorer to get an updated access token with extended permissions to read friendlists
         and read stream. The one below is expired. This procedure will be used for testing until the proper redirect scripts are in place. */
        String ACCESS_TOKEN = "AAACEdEose0cBAF6ymPdw3ZBdSBqs1eDSXZB0FLncrinsfZCHwiDtZAprRQXut558qmX8wNpD3YsspvKU2P2rOJtAYYL4mHoCXUCdhbsrDoXR1AOxbh6ZB";
        FacebookClient fb = new DefaultFacebookClient(ACCESS_TOKEN);
        int MAX_TIME_MS = 5000;

        Connection<Post> myFeed = fb.fetchConnection("me/feed",Post.class);

        long starttime = System.currentTimeMillis();
        query: {
            for (List<Post> myWall : myFeed) {
                for (Post post : myWall) {
                    System.out.println("Type: " + post.getType() + " ID: " + post.getId());
                    System.out.println("Author: " + post.getFrom().getName() + " (ID:" + post.getFrom().getId() + ")");
                    System.out.println("Message: " + post.getMessage());
                    System.out.println("Comments: ");
                    for (Comment comment : post.getComments().getData()) {
                        System.out.println("    " + comment.getFrom().getName() + ": " + comment.getMessage());
                    }
                    System.out.println("Time: " + post.getCreatedTime() + "\n");					
                }
                System.out.println("NEXT WALL --->");
                if (System.currentTimeMillis() - starttime > MAX_TIME_MS) {
                    System.out.println("TIME IS UP!!!");
                    break query;
                }
            }
        }


    }
	
	
}

