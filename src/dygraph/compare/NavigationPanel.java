package dygraph.compare;

import gui.virtual.ColorScheme.ColorSchemeData;
import gui.virtual.Identifiers.POS;
import gui.virtual.Identifiers.STATE;
import gui.virtual.ColorScheme;
import gui.virtual.VirtualPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import util.misc.Graphics2DUtil;

public class NavigationPanel extends VirtualPanel {

	public final static ColorScheme DEFAULT_COLOR_SCHEME = new ColorScheme(
			new Color(0,0,0,0),
			new ColorSchemeData(POS.BACKGROUND | STATE.SELECTED, new Color(175,175,175,255)),
			new ColorSchemeData(POS.BACKGROUND | STATE.FOCUSED, new Color(200,200,200,255)),
			new ColorSchemeData(POS.BACKGROUND | STATE.DEFAULT, new Color(225,225,225,255)),
			new ColorSchemeData(POS.FOREGROUND, new Color(100,100,100,255)));
	
	protected static enum Direction {
		RIGHT,
		LEFT
	}
	
	protected Direction direction;
	
	public NavigationPanel(int x, int y, int width, int height, Direction direction) {
		super(x, y, width, height,DEFAULT_COLOR_SCHEME);
		this.direction = direction;
		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e) {
				if (state != STATE.SELECTED) {
					state = STATE.FOCUSED;
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (state != STATE.SELECTED) {
					state = STATE.DEFAULT;
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				state = STATE.SELECTED;
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				state = STATE.FOCUSED;
			}
		});
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintTriangle((Graphics2D)g);
	}
	
	private void paintLeftTriangle(Graphics2D g) {
		int width = getWidth(), height = getHeight();
	    int xpoints[] = {width/4,3*width/4,3*width/4,width/4};
	    int ypoints[] = {height/2,height/2 + 30,height/2 - 30, height/2};
	    int npoints = 3;
	    g.fill(Graphics2DUtil.getRoundedGeneralPath(new Polygon(xpoints,ypoints,npoints)));
	}
	
	private void paintRightTriangle(Graphics2D g) {
		int width = getWidth(), height = getHeight();
	    int xpoints[] = {width/4,width/4,3*width/4,width/4};
	    int ypoints[] = {height/2 + 30,height/2 - 30,height/2,height/2 + 30};
	    int npoints = 3;
	    g.fill(Graphics2DUtil.getRoundedGeneralPath(new Polygon(xpoints,ypoints,npoints)));
	}
	
	protected void paintTriangle(Graphics2D g) {
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		Color c = g.getColor();
		g.setColor(colorScheme.getColor(POS.FOREGROUND));
		
		switch(direction) {
		
		case RIGHT:
			paintRightTriangle(g);
			break;
			
		case LEFT:
			paintLeftTriangle(g);
			break;
		}
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
		g.setColor(c);
	}
	
	
}
