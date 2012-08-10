package gui.virtual;

import gui.virtual.ColorScheme.ColorSchemeData;
import gui.virtual.Identifiers.POS;
import gui.virtual.Identifiers.STATE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class VirtualPanel extends VirtualComponent {
	
	public final static ColorScheme DEFAULT_COLOR_SCHEME = new ColorScheme(
			new Color(0,0,0,0));
	
	public VirtualPanel(int x, int y, int width, int height,ColorScheme scheme) {
		super(scheme);
		setOpaque(false);
		setBounds(x,y,width,height);
	}
	
	public VirtualPanel(int x, int y, int width, int height) {
		this(x,y,width,height,DEFAULT_COLOR_SCHEME);
	}
	
	protected void paintBackground(Graphics2D g2d) {
		Color c = g2d.getColor();
		g2d.setColor(colorScheme.getColor(state | POS.BACKGROUND));
		g2d.fillRect(0,0,getWidth(),getHeight());
		g2d.setColor(c);
	}

	@Override
	public void paintComponent(Graphics g) {
		paintBackground((Graphics2D)g);
	}

}
