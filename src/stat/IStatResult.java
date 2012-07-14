package stat;

import gui.graph.AbstractPainter;

import java.awt.Graphics;

import model.graph.Vertex;

public interface IStatResult {

	/* A global graph metric such as diameter */
	public final static int SCALAR = 0;
	/* A vertex-based metric such as clustering */
	public final static int VECTOR = 1;
	/* An edge-based metric such as edge centrality */
	public final static int MATRIX = 2;
	/* A miscellaneous statistic */
	public final static int ABSTRACT = 3;
	
	/* Return the type of statistic (see above) */
	public int getType();
	
}
