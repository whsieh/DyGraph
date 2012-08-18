package gui.virtual;

import gui.virtual.Identifiers.STATE;

import java.awt.Graphics;

import javax.swing.JComponent;

public abstract class VirtualComponent extends JComponent {

	private static final long serialVersionUID = 1L;
	protected ColorScheme colorScheme;

	volatile public int state;
	
	public VirtualComponent() {
		this(new ColorScheme());
	}
	
	public VirtualComponent(ColorScheme scheme) {
		this.colorScheme = scheme;
		this.state = STATE.DEFAULT;
		setVisible(true);
	}
	
	public void initialize() {
		
	}
	
	abstract public void paintComponent(Graphics g2d);

	public void finish() {
		
	}
}
