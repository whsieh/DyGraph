package dygraph.compare.panels;

import gui.virtual.ColorScheme;
import gui.virtual.ColorScheme.ColorSchemeData;
import gui.virtual.Identifiers.POS;
import gui.virtual.Identifiers.STATE;
import gui.virtual.VirtualPanel;
import gui.virtual.impl.TextScroller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import dygraph.DygraphResource;
import dygraph.FacebookUtil;
import dygraph.ProfileQueryEngine;
import dygraph.compare.CompareFrame;
import dygraph.compare.KeywordData;

public class KeywordsPanel extends VirtualPanel {
	
	public final static ColorScheme DEFAULT_COLOR_SCHEME = new ColorScheme(
			new Color(150,150,150,0),
			new ColorSchemeData(POS.BACKGROUND | STATE.SELECTED, new Color(250,250,250,25)),
			new ColorSchemeData(POS.FOREGROUND, new Color(25,25,25,255)));
	
	protected TextScroller textScroll1;
	protected TextScroller textScroll2;
	
	protected JLabel loading1;
	protected JLabel loading2;

	protected ProfileQueryEngine prof1;
	protected ProfileQueryEngine prof2;

	protected KeywordData[] keywords1;
	protected KeywordData[] keywords2;
	
	public KeywordsPanel(CompareFrame frame, ProfileQueryEngine prof1, ProfileQueryEngine prof2) {
		super(CompareFrame.NAV_BAR_WIDTH,CompareFrame.DEFAULT_BAR_THICKNESS,frame.getWidth()-CompareFrame.NAV_BAR_WIDTH*2,
				frame.getHeight()-CompareFrame.DEFAULT_BAR_THICKNESS,DEFAULT_COLOR_SCHEME);
		textScroll1 = new TextScroller(0,55,getWidth()/2,getHeight()-55);
		textScroll2 = new TextScroller(getWidth()/2,55,getWidth()/2,getHeight()-55);
		this.prof1 = prof1;
		this.prof2 = prof2;
        
		
		if (DygraphResource.LOADING != null) {
			int iconWidth = DygraphResource.LOADING.getIconWidth();
			int iconHeight= DygraphResource.LOADING.getIconHeight();
	        loading1 = new JLabel(DygraphResource.LOADING);
	        loading2 = new JLabel(DygraphResource.LOADING);
	        loading1.setBounds(getWidth()/4-(iconWidth/2),getHeight()/2-(iconHeight/2),iconWidth,iconHeight);
	        loading2.setBounds(3*getWidth()/4-(iconWidth/2),getHeight()/2-(iconHeight/2),iconWidth,iconHeight);
	        add(loading1);
	        add(loading2);
		}
        
		initialize();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Graphics2D g2d = (Graphics2D)g;
		BufferedImage img1 = prof1.fetchPicture(),img2 = prof2.fetchPicture();
		int x1 = getWidth()/2-img1.getWidth()-2, x2 = getWidth()/2+2;
		int y = 5;
		g.drawImage(img1,x1,y,this);
		g.drawImage(img2,x2,y,this);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawRoundRect(x1, y, img1.getWidth(), img1.getHeight(), 5, 5);
		g.drawRoundRect(x2, y, img2.getWidth(), img2.getHeight(), 5, 5);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
	}
	
	final public void initialize() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				keywords1 = FacebookUtil.toKeywordData(prof1.fetchStatuses());		
				for (KeywordData data : keywords1) {
					textScroll1.addLabel(data.getKeyword(),"Arial",1,25);
				}
				add(textScroll1);
				textScroll1.initialize();
				if (loading1 != null) {
					loading1.getGraphics().dispose();
					remove(loading1);
				}
			}
		}).start();
		
		new Thread(new Runnable(){
			@Override
			public void run() {	
				keywords2 = FacebookUtil.toKeywordData(prof2.fetchStatuses());
				for (KeywordData data : keywords2) {
					textScroll2.addLabel(data.getKeyword(),"Arial",1,25);
				}
				add(textScroll2);
				textScroll2.initialize();
				if (loading2 != null) {
					loading2.getGraphics().dispose();
					remove(loading2);
				}
			}
		}).start();		
	}
	
	@Override
	public void finish() {
		textScroll1.finish();
		textScroll2.finish();
	}
	
}
