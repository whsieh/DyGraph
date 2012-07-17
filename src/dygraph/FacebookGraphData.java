package dygraph;

import gui.graph.GraphData;
import gui.graph.GraphData.IEdgeData;
import gui.graph.GraphData.IVertexData;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class FacebookGraphData extends GraphData <FacebookGraphData.FacebookVertexData,FacebookGraphData.FacebookEdgeData> {

	static final public double COMMENT_WEIGHT = 0.2;
	static final public double POST_WEIGHT = 1.0;
	
	@Override
	public void addVertexData(List<String> args) {
		if (args.size() >= 2) {
			getVertexInfo().add(new FacebookVertexData(args.get(0),args.get(1)));
		}
	}

	
	public void addVertexData(String id, String name) {
		getVertexInfo().add(new FacebookVertexData(id,name));
	}
	
	@Override
	public void addEdgeData(List<String> args) {
		
		if (args.size() >= 4 && !args.get(0).equals(args.get(1)) ) {
			getEdgeInfo().add(new FacebookEdgeData(args.get(0),args.get(1),args.get(2),Double.parseDouble(args.get(3))));
		}
	}
	
	public void addEdgeData(String user1, String user2, String message, double weight) {
		
		if (!user1.equals(user2)) {
			getEdgeInfo().add(new FacebookEdgeData(user1, user2, message,weight));
		}
	}
	
	public List<FacebookVertexData> getVertexData() {
		return (List<FacebookVertexData>)getVertexInfo();
	}
	
	class FacebookVertexData implements IVertexData {

		String id,name;		
		
		FacebookVertexData(String id, String name) {
			this.id = id;
			this.name = name;
		}
		
		@Override
		public String getID() {
			return id;
		}
		
		public String getName() {
			return name;
		}
	}

	class FacebookEdgeData implements IEdgeData {

		String messageID;
		String id,user1,user2;
		double weight;
		
		FacebookEdgeData(String user1, String user2, String mID, double weight) {
			
			if (user1.compareTo(user2) < 0) {
				id = user1 + "_" + user2;
				this.user1 = user1;
				this.user2 = user2;
			} else {
				id = user2 + "_" + user1;
				this.user1 = user2;
				this.user2 = user1;
			}
			messageID = mID;
			this.weight = weight;
		}
		
		@Override
		public String getID() {
			return id;
		}

		@Override
		public String[] getVertexID() {
			return new String[] {user1, user2};
		}
		
		public String getMessageID() {
			return messageID;
		}
		
		public void addCommentWeight() {
			weight += COMMENT_WEIGHT;
		}
		
		public void addPostWeight() {
			weight += POST_WEIGHT;
		}
		
		public void addWeight(double w) {
			weight += w;
		}

		@Override
		public double weight() {
			return weight;
		}
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("People to add to dygraph:\n");
		for (IVertexData v : getVertexInfo()) {
			FacebookVertexData fv = (FacebookVertexData)v;
			s.append("\tName: " + fv.getName() + "\n\t\tID: " + fv.getID() + "\n");
		}
		s.append("Messages to add to dygraph:\n");
		for (IEdgeData e : getEdgeInfo()) {
			FacebookEdgeData fe = (FacebookEdgeData)e;
			s.append("\tParticipant ID: " + fe.user1 + " and " + fe.user2 + 
					"\n\t\tMessage ID: " + fe.getMessageID() + "\n");
		}
		return s.toString();
	}
	
}

