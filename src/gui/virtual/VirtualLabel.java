package gui.virtual;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JLabel;

public class VirtualLabel extends VirtualComponent {

	protected static final long serialVersionUID = 1L;
	protected JLabel myLabel;
	
	public VirtualLabel(int x, int y, String text, String fontStyle, int fontModifier, int fontSize) {
		this(x,y,text,new Font(fontStyle,fontModifier,fontSize));
	}
	
	public VirtualLabel(int x, int y, String text, Font font) {
		myLabel = new JLabel(text);
		myLabel.setFont(font);
		Dimension size = myLabel.getPreferredSize();
		myLabel.setBounds(x,y,size.width,size.height);
		this.setBounds(x,y,size.width,size.height);
	}
	
	public void setText(String text) {
		myLabel.setText(text);
		Dimension size = myLabel.getPreferredSize();
		myLabel.setBounds(getX(),getY(),size.width,size.height);
		this.setBounds(getX(),getY(),size.width,size.height);
	}
	
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		myLabel.setLocation(x,y);
	}
	
	public void setLocation(Point p) {
		setLocation(p.x,p.y);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		myLabel.paint(g);
	}
}
