package stat.comm;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import stat.IStatResult;


/* TODO A more elegant implementation of this class would use an array of size
 * 2^D + 1 to represent the complete binary tree, expanding by a factor of 2 when 
 * necessary. The disadvantage is that there might be significant wasted space if
 * the community structure is very imbalanced; the advantage is that looking up a
 * community would be very efficient. For now, I'll leave it as is. */

public class Dendrogram implements IStatResult{
	
	public static void main(String[] args) {
		
		Set<String> vertices = new HashSet<String>();
		vertices.add("A");
		vertices.add("B");
		vertices.add("C");
		vertices.add("D");
		vertices.add("E");
		vertices.add("F");
		vertices.add("G");
		vertices.add("H");
		vertices.add("I");
		vertices.add("J");
		
		Dendrogram n = new Dendrogram(vertices);
		n.addAllFirst("A","C","E","I","G");
		n.addAllSecond("F","J");
		Dendrogram[] children = n.getChildren();
		children[0].addAllFirst("A","E");
		children[0].addAllSecond("I","G");
		children[1].addFirst("F");
		children[1].addSecond("J");
	}
	
	
	protected Set<String> names;
	protected int size;
	protected int depth;
	
	protected Dendrogram first;
	protected Dendrogram second;
	
	protected Dendrogram() {
		names = new HashSet<String>();
		size = 0;
		depth = 0;
	}
	
	protected Dendrogram(Set<String> names) {
		this.names = names;
		size = names.size();
		depth = 0;
	}
	
	public Set<String> getAllMembers() {
		Set<String> allMembers = new HashSet<String>();
		allMembers.addAll(names);
		if (first != null) {
			allMembers.addAll(first.getAllMembers());
		}
		if (second != null) {
			allMembers.addAll(second.getAllMembers());
		}
		return allMembers;
	}
	
 	public Dendrogram[] getChildren() {
 		if (isLeaf()) {
 			return null;
 		}
		return new Dendrogram[] {first,second};
	}
	
	public boolean isLeaf() {
		return first == null && second == null;
	}
	
	public void addName(String name) {
		names.add(name);
		size++;
	}
	
	public void addFirst(String name) {
		if (names.contains(name)) {
			names.remove(name);
			if (first == null) {
				first = new Dendrogram();
				first.depth = depth+1;
			}
			first.addName(name);
		}
	}
	
	public void addAllFirst(String...names) {
		for (String name : names) {
			addFirst(name);
		}
	}
	
	public void addAllSecond(String...names) {
		for (String name : names) {
			addSecond(name);
		}
	}
	
	public void addSecond(String name) {
		if (names.contains(name)) {
			names.remove(name);
			if (second == null) {
				second = new Dendrogram();
				second.depth = depth+1;
			}
			second.addName(name);
		}
	}
	
	public int size() {
		return size;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(" + size() + " members): " + names);
		if (!isLeaf()) {
			sb.append("\n");
			for (int i = 0; i < depth+1; i++) {
				sb.append("    ");
			}
			sb.append("First: " + first);
			sb.append("\n");
			for (int i = 0; i < depth+1; i++) {
				sb.append("    ");
			}
			sb.append("Second: " + second);
		}
		return sb.toString();
	}

	public String toString(Map<String,String> idMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("(" + size() + " members): [");
		for (String name : names) {
			sb.append(idMap.get(name) + " ; ");
		}
		sb.append("]");
		if (!isLeaf()) {
			sb.append("\n");
			for (int i = 0; i < depth+1; i++) {
				sb.append("    ");
			}
			sb.append("First: " + first.toString(idMap));
			sb.append("\n");
			for (int i = 0; i < depth+1; i++) {
				sb.append("    ");
			}
			sb.append("Second: " + second.toString(idMap));
		}
		return sb.toString();
	}

	
	@Override
	public int getType() {
		return IStatResult.ABSTRACT;
	}
	
}
