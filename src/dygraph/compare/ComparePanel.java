package dygraph.compare;

import gui.virtual.ColorScheme;
import gui.virtual.ColorScheme.ColorSchemeData;
import gui.virtual.Identifiers.POS;
import gui.virtual.Identifiers.STATE;
import gui.virtual.VirtualLabel;
import gui.virtual.VirtualPanel;

import java.awt.Color;
import java.awt.Graphics;

import dygraph.DygraphResource;

import util.misc.ImageLibrary;

public class ComparePanel extends VirtualPanel {
	
	public final static ColorScheme DEFAULT_COLOR_SCHEME = new ColorScheme(
			new Color(150,150,150,0),
			new ColorSchemeData(POS.BACKGROUND | STATE.SELECTED, new Color(250,250,250,25)),
			new ColorSchemeData(POS.FOREGROUND, new Color(25,25,25,255)));
	
	private VirtualLabel titleLabel;
	private VirtualLabel descriptionLabel;
	
	/* Yes, the cat plugin is only temporary :\ */
	private VirtualLabel catLabel;
	private VirtualPanel catPanel;
	
	public ComparePanel(CompareFrame frame) {
		super(100,CompareFrame.DEFAULT_BAR_THICKNESS,frame.getWidth()-200,
				frame.getHeight()-CompareFrame.DEFAULT_BAR_THICKNESS,DEFAULT_COLOR_SCHEME);
		titleLabel = new VirtualLabel(5,5,"This feature under development!","Arial",1,getHeight()/20);
		add(titleLabel);
		catLabel = new VirtualLabel(5,titleLabel.getY()+titleLabel.getHeight(),"In the meantime, enjoy this picture of a cat.","Arial",1,getHeight()/40);
		add(catLabel);
		catPanel = new VirtualPanel(5,catLabel.getY()+catLabel.getHeight(),getWidth()-10,(int)(getHeight()*0.9f)){
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(DygraphResource.CAT_PICTURE,getWidth()/2-DygraphResource.CAT_PICTURE.getWidth(catPanel)/2
						,getHeight()/2-DygraphResource.CAT_PICTURE.getHeight(catPanel)/2,catPanel);
			}
		};
		add(catPanel);
	}

}
