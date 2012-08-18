package dygraph;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.misc.XMLUtil;

import com.restfb.types.NamedFacebookType;
import com.restfb.types.Post;
import com.restfb.types.StatusMessage;
import com.restfb.types.User;

import dygraph.compare.KeywordData;

final public class FacebookUtil {

	final static public String API_KEY = "[REDACTED]";
	final static public String ENDPOINT = "http://access.alchemyapi.com/calls/text/TextGetRankedKeywords";
	final static public String PARAMS = "?apikey=" + API_KEY + "&keywordExtractMode=strict&text=";
	
	private FacebookUtil(){}
	
	public static FacebookGraphData toGraphData(Post post) {
		return toGraphData(null,post);
	}
	
	public static FacebookGraphData toGraphData(User root, Post post) {
		/* 'mentioned': stores the IDs of those who were mentioned so far in this post */
		Set<String> mentioned = new HashSet<String>();
		/* 'data': the GraphData object that the GraphViewer will 'ask' for information about
		 * which vertices and edges it needs to instantiate */
		FacebookGraphData data = new FacebookGraphData();
		String msg = post.getMessage();
		if (msg != null && !msg.toLowerCase().contains("bday") && !msg.toLowerCase().contains("birthday")) {
			/* 'from': this post's author */
			NamedFacebookType from = post.getFrom();
			/* If the author of this post is one of my friends... */
			if (ProfileQueryEngine.MY_FRIENDS.containsKey(from.getId())) {
				/* Add the author to 'mentioned' */
				mentioned.add(from.getId());
				/* Add the author to the vertex data */
				data.addVertexData(from.getId(),from.getName());
				/* 'to': the recipients of this Facebook post */
				List<NamedFacebookType> to = post.getTo();
				/* For each recipient of the Facebook post... */
				for (NamedFacebookType recipient : to) {
					/* If the recipient has not yet been mentioned and either: the recipient is the current user or one of
					 * the current user's friends: */
					if (!mentioned.contains(recipient.getId()) &&
							(recipient.getId().equals(ProfileQueryEngine.CURRENT_USER.key()) || ProfileQueryEngine.MY_FRIENDS.containsKey(recipient.getId()))) {
						/* Add the message recipient to 'mentioned' */
						mentioned.add(recipient.getId());
						/* Add vertex information for the recipient */
						data.addVertexData(recipient.getId(),recipient.getName());
						/* Add an edge from the sender to the recipient */
						data.addEdgeData(from.getId(),recipient.getId(),msg,FacebookGraphData.POST_WEIGHT);
					}
				}
				/** The following code is used to add users based also on comments of the post. Removed due to people
				 *  having lengthy conversations over Facebook comments -.- **/
				/*
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
				*/
				/** NOTE: the following was added to deal with posts which included a 'with' reference **/
				/* If this method was called with respect to a root user... */
				if (root != null) {
					/* If the root user has not already been mentioned... */
					if (!mentioned.contains(root)) {
						/* Add the root user to 'mentioned' */
						mentioned.add(root.getId());
						/* Add vertex data for the root user */
						data.addVertexData(root.getId(),root.getName());
						/* Add an edge from the sender to the root user */
						data.addEdgeData(from.getId(), root.getId(), msg, FacebookGraphData.POST_WEIGHT);
					}
				}
			}
		}
		return data;
	}
	
	public static KeywordData[] toKeywordData(StatusMessage[] statuses) {
		
		List<KeywordData> keywordDataList = new LinkedList<KeywordData>();
		StringBuilder dataBuilder = new StringBuilder();
		String myText = extractText(statuses);
		String queryURI = ENDPOINT + PARAMS + myText;
		URL url;
		InputStream is = null;
		DataInputStream dis;
		int intChar;
		try {
		    url = new URL(queryURI);
		    is = url.openStream();  // throws an IOException
		    dis = new DataInputStream(new BufferedInputStream(is));
		    while ((intChar = dis.read()) != -1) {
		        dataBuilder.append(((char)intChar));
		    }
		} catch (MalformedURLException mue) {
		     mue.printStackTrace();
		} catch (IOException ioe) {
		     ioe.printStackTrace();
		} finally {
		    try {
		        is.close();
		    } catch (IOException ioe) {
		    }
		}
		
		String xmlString = dataBuilder.toString();
		Document doc = XMLUtil.loadXML(xmlString);
		doc.getDocumentElement().normalize();
		Element results = doc.getDocumentElement();
		NodeList keywords = results.getElementsByTagName("keyword");
		for (int i = 0; i < keywords.getLength(); i++) {
			Node keywordNode = keywords.item(i);
			if (keywordNode.getNodeType() == Node.ELEMENT_NODE) {
				Element keywordElement = (Element) keywordNode;
				keywordDataList.add(new KeywordData(
						XMLUtil.getTagValue("text", keywordElement),statuses));
			}
		}
		return keywordDataList.toArray(new KeywordData[keywordDataList.size()]);
	}
	
	private static String extractText(StatusMessage[] statuses) {
		
		final String divider = " ;";
		StringBuilder statusText = new StringBuilder();
		
		/* We need to change this. I don't know any way to find tagged users in
		 * statuses, so this will have to do for now. */
		for (StatusMessage status : statuses) {
			String text = status.getMessage();
			for (String friend : ProfileQueryEngine.MY_FRIENDS.values()) {
				text = text.replaceFirst(friend, "");
			}
			statusText.append(text + divider);
		}
		String text = statusText.toString();
		try {
			return URLEncoder.encode(text,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return text;
		}
	}
	
}
