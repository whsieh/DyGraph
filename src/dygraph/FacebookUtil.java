package dygraph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gui.graph.GraphData;

import com.restfb.types.Comment;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Post;

final public class FacebookUtil {

	private FacebookUtil(){}
	
	public static FacebookGraphData toGraphData(Post post) {
		/* 'mentioned': stores the IDs of those who were mentioned so far in this post */
		Set<String> mentioned = new HashSet<String>();
		/* 'data': the GraphData object that the GraphViewer will 'ask' for information about
		 * which vertices and edges it needs to instantiate */
		FacebookGraphData data = new FacebookGraphData();
		if (post.getMessage() != null && !post.getMessage().contains("bday") && !post.getMessage().contains("birthday")) {
			/* 'from': this post's author */
			NamedFacebookType from = post.getFrom();
			/* If the author of this post is one of my friends... */
			if (ProfileQueryEngine.MY_FRIENDS.containsKey(from.getId())) {
				/* The author has been 'mentioned' */
				mentioned.add(from.getId());
				/* Add the author to the vertex data */
				data.addVertexData(from.getId(),from.getName());
				/* 'to': the recipients of this Facebook post */
				List<NamedFacebookType> to = post.getTo();
				/* For each recipient of the Facebook post... */
				for (NamedFacebookType user : to) {
					if (!mentioned.contains(user.getId()) &&
							(user.getId().equals(ProfileQueryEngine.CURRENT_USER.key()) || ProfileQueryEngine.MY_FRIENDS.containsKey(user.getId()))) {
						mentioned.add(user.getId());
						data.addVertexData(user.getId(),user.getName());
						data.addEdgeData(from.getId(),user.getId(),post.getMessage(),FacebookGraphData.POST_WEIGHT);
					}
				}
				for (Comment comment : post.getComments().getData()) {
					NamedFacebookType commentFrom = comment.getFrom();
					if (!mentioned.contains(commentFrom.getId()) && 
							(commentFrom.getId().equals(ProfileQueryEngine.CURRENT_USER.key()) || ProfileQueryEngine.MY_FRIENDS.containsKey(commentFrom.getId()))) {
						data.addVertexData(commentFrom.getId(),commentFrom.getName());
					}
					for (NamedFacebookType user : to) {
						if (!commentFrom.getId().equals(user.getId())) {
							data.addEdgeData(commentFrom.getId(), user.getId() , comment.getMessage(), FacebookGraphData.COMMENT_WEIGHT);
						}
					}
					if (!commentFrom.getId().equals(from.getId())) {
						data.addEdgeData(commentFrom.getId(), from.getId() , comment.getMessage(), FacebookGraphData.COMMENT_WEIGHT);
					}
				}
			}
		}
		return data;
		
	}
	
}
