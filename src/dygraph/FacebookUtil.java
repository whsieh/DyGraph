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
		Set<String> mentioned = new HashSet<String>();
		FacebookGraphData data = new FacebookGraphData();
		NamedFacebookType from = post.getFrom();
		if (ProfileQueryEngine.MY_FRIENDS.containsKey(from.getId())) {
			mentioned.add(from.getId());
			List<NamedFacebookType> to = post.getTo();
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
		return data;
	}
	
}
