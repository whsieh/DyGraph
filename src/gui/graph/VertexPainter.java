package gui.graph;

import gui.graph.physics.IMassController;
import gui.graph.util.Data;
import gui.graph.util.Message;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import util.dict.CoordinateTable2D;
import util.list.InvalidNodeException;
import util.list.ListNode;
import util.misc.Vector2D;

public class VertexPainter extends AbstractPainter implements IMassController,
		IMouseContainer {

	protected final static int RADIUS = 12;
	protected final static Color[][] STATE_COLORS = new Color[][] {
			{ Color.WHITE, Color.DARK_GRAY }, { Color.WHITE, Color.BLUE },
			{ Color.WHITE, Color.RED }, };

	/* Graph-related components */
	protected ListNode<VertexPainter> myListNode;
	protected CoordinateTable2D<VertexPainter> myTable;
	protected List<EdgePainter> myEdges;
	protected String displayName;
	
	volatile protected int x;
	volatile protected int y;
	volatile protected Point curRegion;

	/* Physics-related components */
	volatile protected Vector2D position;
	volatile protected Vector2D velocity;
	volatile protected Vector2D acceleration;

	protected VertexPainter(GraphViewer graphPane, int xPos, int yPos,
			String id, String displayName) {

		/* Initialize graph-related components */
		this.state = 0;
		this.x = xPos;
		this.y = yPos;
		this.myParent = graphPane;
		this.myListNode = null;
		this.myEdges = new CopyOnWriteArrayList<EdgePainter>();
		this.myTable = graphPane.vertexTable;
		this.id = id;
		this.displayName = displayName;

		/* Initialize physics-related components */
		this.position = new Vector2D(0, 0, Vector2D.CARTESIAN,
				Vector2D.POSITION);
		this.velocity = new Vector2D(0, 0, Vector2D.CARTESIAN,
				Vector2D.VELOCITY);
		this.acceleration = new Vector2D(0, 0, Vector2D.CARTESIAN,
				Vector2D.ACCELERATION);
		this.curRegion = myTable.findRegion(new Point(x, y));

	}

	protected VertexPainter(GraphViewer graphPane, int xPos, int yPos, String id) {
		this(graphPane, xPos, yPos, id, id);
	}

	@Override
	public void inform(Message message, Data d) {
		switch (message) {

		case MOUSE_OVER:
			if (!(state == AbstractPainter.SELECTED)) {
				setState(FOCUSED);
			}
			break;

		case MOUSE_EXITED:
			if (!(state == AbstractPainter.SELECTED)) {
				setState(DEFAULT);
			}
			break;

		case MOUSE_CLICKED:
			setState(SELECTED);
			break;

		case MOUSE_DRAGGED:
			moveTo(((MouseEvent) d.info()).getPoint());
			break;

		case MOUSE_DESELECTED:
			setState(DEFAULT);
			break;

		default:
			System.err.println("Warning: NodePainter received unrecognized"
					+ " user feedback.");
			break;
		}
	}

	protected void remove() {
		myParent.vertexTable.remove(new Point(x, y));
		try {
			for (EdgePainter ep : myEdges) {
				ep.remove();
			}
			myListNode.remove();
		} catch (InvalidNodeException e) {
			System.err.println("Error: Failed to remove NodePainter due to "
					+ "invalid LinkedList reference.");
		}
	}

	protected void moveTo(int xPos, int yPos) {
		Point old_p = new Point(x, y);
		Point new_p = new Point(xPos, yPos);
		if (myParent.bounds.contains(new_p)) {
			Point newRegion = myTable.findRegion(old_p);
			if (!newRegion.equals(curRegion)) {
				myTable.remove(old_p, this);
				myTable.insert(new_p, this);
			}
			this.x = xPos;
			this.y = yPos;
			for (EdgePainter ep : myEdges) {
				ep.inform(Message.REQUEST_UPDATE, null);
			}
		}
	}

	protected void moveTo(Vector2D pos) {
		moveTo((int) pos.x() + x, (int) pos.y() + y);
	}

	protected void moveTo(Point p) {
		moveTo(p.x, p.y);
	}

	@Override
	protected boolean contains(int x, int y) {
		return Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2) <= VertexPainter.RADIUS
				* VertexPainter.RADIUS;
	}

	@Override
	public boolean contains(Point p) {
		return contains(p.x, p.y);
	}

	@Override
	protected void paintDefault(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		paintCircularVertex(g2d, STATE_COLORS[AbstractPainter.DEFAULT][0],
				STATE_COLORS[AbstractPainter.DEFAULT][1]);
	}

	@Override
	protected void paintFocused(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		paintCircularVertex(g2d, STATE_COLORS[AbstractPainter.FOCUSED][0],
				STATE_COLORS[AbstractPainter.FOCUSED][1]);
	}

	@Override
	protected void paintSelected(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		paintCircularVertex(g2d, STATE_COLORS[AbstractPainter.SELECTED][0],
				STATE_COLORS[AbstractPainter.SELECTED][1]);
	}

	private void paintCircularVertex(Graphics2D g2d, Color cInner, Color cOuter) {

		Stroke s = g2d.getStroke();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setColor(cInner);
		g2d.fillOval(x - RADIUS, y - RADIUS, 2 * (RADIUS), 2 * (RADIUS));

		g2d.setColor(cOuter);
		g2d.setStroke(new BasicStroke(4));
		g2d.drawOval(x - RADIUS, y - RADIUS, 2 * (RADIUS), 2 * (RADIUS));
		g2d.drawString(displayName, x + RADIUS, y);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
		g2d.setStroke(s);
	}

	@Override
	public String toString() {
		String s = id + " @(" + x + "," + y + ")";
		return s;
	}

	@Override
	public float mass() {
		if (state == SELECTED || myEdges.isEmpty()) {
			return 0;
		}
		return myParent.controller.unitMass;
	}

	@Override
	public Vector2D position() {
		return position;
	}

	@Override
	public Vector2D velocity() {
		return velocity;
	}

	@Override
	public Vector2D acceleration() {
		return acceleration;
	}

	@Override
	public float kineticEnergy() {
		return 0.5f * mass() * (float) Math.pow(velocity.calcMagnitude(), 2);
	}

	@Override
	public boolean inEquilibrium() {
		return velocity.isZero();
	}

	@Override
	public void updateAcceleration(Vector2D force) {
		acceleration.add(force.scaleTo(1 / mass()));
		acceleration.add(velocity
				.scaleTo(IMassController.ENERGY_LOSS_COEFFICIENT));
	}

	@Override
	public void calc(float dt) {
		if (state != FOCUSED) {
			moveTo(position);
		}
		// System.out.println("    Move by: " + position);
		position.setZero();
		position.add(velocity.scaleTo(dt));
		velocity.add(acceleration.scaleTo(dt));
		if (!velocity.isValid()) {
			velocity.setZero();
		}
		acceleration.setZero();
	}

}
