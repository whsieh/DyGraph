
package gui.virtual.impl;

import gui.virtual.ColorScheme;
import gui.virtual.VirtualLabel;
import gui.virtual.VirtualPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TextScroller extends VirtualPanel {

	static public final int DEFAULT_LEFT_MARGIN = 25;
	static public final int DEFAULT_TIMESTEP_MS = 20;
	static public final int DEFAULT_AUTO_SCROLL_VEL = 1;
	static public final int DEFAULT_MOUSE_SCROLL_VEL = 10;
	static public final int DEFAULT_LABEL_MARGIN = 10;
	
	protected List<VirtualLabel> labels;
	protected Thread scrollSimulator;
	protected boolean isDisposed;
	protected boolean isPaused;
	protected int curX,curY;
	protected boolean initialized;
	
	public TextScroller(int x, int y, int width, int height) {
		this(x, y, width, height, VirtualPanel.DEFAULT_COLOR_SCHEME);
	}
	
	public TextScroller(int x, int y, int width, int height,ColorScheme scheme) {
		super(x, y, width, height, scheme);
		labels = new CopyOnWriteArrayList<VirtualLabel>();
		isDisposed = false;
		initialized = false;
		curX = getWidth()/2;
		curY = getHeight()/2;
		scrollSimulator = new Thread(new Runnable(){
			@Override
			public void run() {
				while (!isDisposed) {
					if (!isPaused) {
						try {
							int timeTaken = scrollComponents();
							int sleepTime = DEFAULT_TIMESTEP_MS - timeTaken;
							if (sleepTime > 0) {
								Thread.sleep(sleepTime);
							}
						} catch (InterruptedException e) {
						}
					}
				}
				System.out.println("finished");
			}
		});
	}
	
	protected void pauseScroll() {
		isPaused = true;
	}
	
	protected void resumeScroll() {
		isPaused = false;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		super.paintComponent(g);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawRoundRect(0,0,getWidth()-1,getHeight()-1,15,15);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
	}
	
	protected int getPreferredHeight() {
		int height = DEFAULT_LABEL_MARGIN;
		for (VirtualLabel label : labels) {
			height += label.getHeight() + DEFAULT_LABEL_MARGIN;
		}
		return height;
	}
	
	public VirtualLabel addLabel(String text, String font, int modifier, int size) {
		VirtualLabel label = new VirtualLabel(DEFAULT_LEFT_MARGIN,getPreferredHeight(),text,font, modifier, size);
		add(label);
		labels.add(label);
		return label;
	}
	
	protected int scrollComponents() {
		return scrollComponents(DEFAULT_AUTO_SCROLL_VEL);
	}
	
	protected int scrollComponents(int deltaY) {
		long start = System.nanoTime();
		for (VirtualLabel label : labels) {
			label.setLocation(label.getX(),label.getY()+deltaY);
		}
		if (labels.size() > 0) {
			VirtualLabel last = labels.get(labels.size()-1);
			int pHeight = getPreferredHeight();
			int height = getHeight();
			if (last.getY() > (pHeight < height ? height : pHeight)) {
				VirtualLabel first = labels.get(0);
				int fHeight = first.getY();
				last.setLocation(last.getX(),(fHeight < 0 ? fHeight : 0)-last.getHeight()-DEFAULT_LABEL_MARGIN);
				labels.remove(labels.size()-1);
				labels.add(0, last);
			}
		}
		return (int)((System.nanoTime() - start)/1000000.0);
	}
	
	public void finish() {
		isDisposed = true;
	}
	
	@Override
	public void initialize() {
		initialized = true;
		scrollSimulator.start();
		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e) {
				if (initialized) {
					pauseScroll();
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (initialized) {
					resumeScroll();
				}
			}
		});
		addMouseMotionListener(new MouseAdapter(){
			@Override
			public void mouseDragged(MouseEvent e) {
				if (initialized) {
					scrollComponents(e.getY()-curY);
					curX = e.getX();
					curY = e.getY();
				}
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				if (initialized) {
					curX = e.getX();
					curY = e.getY();
				}
			}
		});
		addMouseWheelListener(new MouseWheelListener(){
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				scrollComponents(e.getUnitsToScroll()*DEFAULT_MOUSE_SCROLL_VEL);
			}
		});
	}

}
