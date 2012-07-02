package dygraph;

import gui.graph.GraphData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FacebookGraphData extends GraphData {

	@Override
	public void addVertexData(List<String> args) {
		if (args.size() >= 2) {
			vertexInfo.add(new FacebookVertexData(args.get(0),args.get(1)));
		}
	}
	
	public void addVertexData(String id, String name) {
		vertexInfo.add(new FacebookVertexData(id,name));
	}
	
	@Override
	public void addEdgeData(List<String> args) {
		
		if (args.size() >= 3 && !args.get(0).equals(args.get(1)) ) {
			edgeInfo.add(new FacebookEdgeData(args.get(0),args.get(1),args.get(2)));
		}
	}
	
	public void addEdgeData(String user1, String user2, String message) {
		
		if (!user1.equals(user2)) {
			edgeInfo.add(new FacebookEdgeData(user1, user2, message));
		}
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
		
		FacebookEdgeData(String user1, String user2, String mID) {
			
			double u1 = Double.parseDouble(user1);
			double u2 = Double.parseDouble(user2);
			if (u1 < u2) {
				id = user1 + "_" + user2;
				this.user1 = user1;
				this.user2 = user2;
			} else {
				id = user2 + "_" + user1;
				this.user1 = user2;
				this.user2 = user1;
			}
			messageID = mID;
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
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("People to add to dygraph:\n");
		for (IVertexData v : vertexInfo) {
			FacebookVertexData fv = (FacebookVertexData)v;
			s.append("\tName: " + fv.getName() + "\n\t\tID: " + fv.getID() + "\n");
		}
		s.append("Messages to add to dygraph:\n");
		for (IEdgeData e : edgeInfo) {
			FacebookEdgeData fe = (FacebookEdgeData)e;
			s.append("\tParticipant ID: " + fe.user1 + " and " + fe.user2 + 
					"\n\t\tMessage ID: " + fe.getMessageID() + "\n");
		}
		return s.toString();
	}
	
}

