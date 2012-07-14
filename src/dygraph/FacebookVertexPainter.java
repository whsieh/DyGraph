package dygraph;

import java.io.IOException;

import gui.graph.*;

public class FacebookVertexPainter extends VertexPainter{

	// TODO profile pic painting, add mouseover effect that makes buttons fade in next to the
	// vertex. (Ex. button to delete node, button to view the person's Facebook profile, and a button
	// to expand the node's connections.)
	
	FacebookVertexPainter(GraphViewer graphPane, int xPos, int yPos, String id) {
		super(graphPane, xPos, yPos, id);
	}
	
}
