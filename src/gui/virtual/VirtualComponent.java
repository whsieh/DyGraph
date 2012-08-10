package gui.virtual;

import gui.virtual.Identifiers.STATE;

import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;

public abstract class VirtualComponent extends JComponent {

	protected Set<VirtualComponent> children;
	protected ColorScheme colorScheme;

	volatile public int state;
	
	public VirtualComponent() {
		this(new ColorScheme());
	}
	
	public VirtualComponent(ColorScheme scheme) {
		this.children = new HashSet<VirtualComponent>();
		this.colorScheme = scheme;
		this.state = STATE.DEFAULT;
		setVisible(true);
	}
	
	public void initialize() {
		
	}

	public Set<VirtualComponent> getChildren() {
		return children;
	}
	
	public VirtualComponent addChild(VirtualComponent vc) {
		children.add(vc);
		return vc;
	}
	
	abstract public void paintComponent(Graphics g2d);
}
