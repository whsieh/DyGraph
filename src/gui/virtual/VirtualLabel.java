package gui.virtual;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;

public class VirtualLabel extends VirtualComponent {

	private JLabel myLabel;
	
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
	
	@Override
	public void paintComponent(Graphics g) {
		myLabel.paint(g);
	}
}
