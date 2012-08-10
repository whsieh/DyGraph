package dygraph.compare;

import gui.virtual.Animator;
import gui.virtual.VirtualFrame;
import gui.virtual.VirtualPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import dygraph.compare.NavigationPanel.Direction;

public class CompareFrame extends VirtualFrame {
	
	VirtualPanel previousPanel;
	VirtualPanel nextPanel;
	
	VirtualPanel contentPanel;
	
	public CompareFrame(int x, int y, int width, int height) {
		super(x,y,width,height);
	}
	
	@Override
	public void initialize() {
		previousPanel = new NavigationPanel(0,DEFAULT_BAR_THICKNESS,100,getHeight()-DEFAULT_BAR_THICKNESS,Direction.LEFT);
		add(previousPanel);
		nextPanel = new NavigationPanel(getWidth()-100,DEFAULT_BAR_THICKNESS,100,getHeight()-DEFAULT_BAR_THICKNESS,Direction.RIGHT);
		add(nextPanel);
		contentPanel = new ComparePanel(this);
		add(contentPanel);
	}
	
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x,y,width,height);
	}
	
	public int getWidth() {
		return super.getWidth();
	}
	
	public int getHeight() {
		return super.getHeight();
	}
	
	public Point getLocation() {
		return super.getLocation();
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
				initialize();
			}
		});
	}
}
