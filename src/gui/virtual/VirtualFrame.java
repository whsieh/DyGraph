package gui.virtual;

import gui.virtual.ColorScheme.ColorSchemeData;
import gui.virtual.Identifiers.POS;
import gui.virtual.Identifiers.STATE;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VirtualFrame extends VirtualComponent {
	
	public final static ColorScheme DEFAULT_COLOR_SCHEME = new ColorScheme(
			new Color(150,150,150,0),
			new ColorSchemeData(POS.BACKGROUND | STATE.DEFAULT, new Color(250,250,250,200)),
			new ColorSchemeData(POS.BORDER | STATE.DEFAULT, new Color(0,0,0)));
	public final static int DEFAULT_ROUND_MARGIN = 15;
	public final static int DEFAULT_BAR_THICKNESS = 25;
	
	volatile public boolean isCollapsed;
	volatile protected String title = "";
	
	protected int curX;
	protected int curY;
	protected boolean isBeingDragged;
	protected int previousHeight;
	
	public VirtualFrame(int x, int y, int width, int height, ColorScheme scheme) {
		super(scheme);
		isCollapsed = true;
		setOpaque(false);
		setBounds(x,y,width,height);
		/*
		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				// Handle double click
				if (e.getClickCount() > 1) {
					if (isCollapsed) {
						expand(previousHeight);
					} else {
						collapse(DEFAULT_BAR_THICKNESS+20);
					}
				}
			}
		});
		*/
		addMouseMotionListener(new MouseAdapter(){
			@Override
			public void mouseMoved(MouseEvent e) {
				curX = e.getX();
				curY = e.getY();
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				if (isBeingDragged) {
					setLocation(getX()+(e.getX()-curX),getY()+(e.getY()-curY));
				}
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (titleBarContains(e.getX(),e.getY())) {
					isBeingDragged = true;
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				isBeingDragged = false;
			}
		});
		isBeingDragged = false;
	}
	
	public void setBounds(int x, int y, int width, int height, boolean updateHeight) {
		if (updateHeight) {
			previousHeight = height;
		}
		super.setBounds(x, y, width, height);
	}
	
	public void setBounds(int x, int y, int width, int height) {
		setBounds(x,y,width,height,true);
	}
	
	public VirtualFrame(int x, int y, int width, int height) {
		this(x,y,width,height,DEFAULT_COLOR_SCHEME);
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	protected void paintBorder(Graphics2D g2d) {
		int x = getX(),y = getY(), width = getWidth(), height = getHeight();
		g2d.setColor(colorScheme.getColor(state | POS.BORDER));
		
		g2d.fillRoundRect(0,0, width,DEFAULT_BAR_THICKNESS,
				DEFAULT_ROUND_MARGIN,DEFAULT_ROUND_MARGIN);
		g2d.fillRect(0,DEFAULT_ROUND_MARGIN,width,
				DEFAULT_BAR_THICKNESS-DEFAULT_ROUND_MARGIN);
		g2d.setFont(new Font("Arial",1,16));
		g2d.setColor(new Color(255,255,255,255));
		g2d.drawString(title,5,DEFAULT_BAR_THICKNESS-5);
	}
	
	protected void paintBackground(Graphics2D g2d) {
		// System.out.println()
		g2d.setColor(colorScheme.getColor(state | POS.BACKGROUND));
		g2d.fillRoundRect(0,0,getWidth(),getHeight(),15,15);
	}
	
	public void paintComponent(Graphics g) {
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		Stroke s = g2d.getStroke();
		Color c = g2d.getColor();
		
		paintBackground(g2d);
		paintBorder(g2d);
		
		g2d.setStroke(s);
		g2d.setColor(c);
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
	}
	
	public boolean titleBarContains(int x, int y) {
		return 0 <= x && x <= getWidth() &&
				0 <= y && y <= DEFAULT_BAR_THICKNESS;
	}
	
	
	public void collapse(final int height) {
		if (!isCollapsed) {
			Animator.run(new Animator<VirtualFrame>(this, 1000, 120) {
				@Override
				public void step() {
					comp.setBounds(getX(),getY(),getWidth(),getHeight()-15,false);
				}
				@Override
				public boolean checkIsDone() {
					return super.checkIsDone() || comp.getHeight() < height;
				}
				@Override
				public void performOnFinish() {
					comp.isCollapsed = true;
				}
			});
		}
	}
	public void expand(final int height) {
		if (isCollapsed) {
			Animator.run(new Animator<VirtualFrame>(this,1000, 120) {
				@Override
				public void step() {
					comp.setBounds(getX(),getY(),getWidth(),getHeight()+15,false);
				}
				@Override
				public boolean checkIsDone() {
					return super.checkIsDone() || comp.getHeight() > height;
				}
				@Override
				public void performOnFinish() {
					comp.isCollapsed = false;
				}
			});
		}
	}
	
	public void expand(final int w, final int h) {
		Animator.run(new Animator<VirtualFrame>(this,500,250) {
			@Override
			public void step() {
				comp.setLocation(comp.getX()-10,comp.getY()-6);
				comp.setBounds(comp.getX(),comp.getY(),comp.getWidth()+20,comp.getHeight()+12,false);
			}
			@Override
			public boolean checkIsDone() {
				return comp.getWidth() > w || comp.getHeight() > h;
			}
			@Override
			public void performOnFinish() {
				comp.isCollapsed = false;
			}
		});
	}
}
