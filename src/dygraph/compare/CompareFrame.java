package dygraph.compare;

import gui.virtual.Animator;
import gui.virtual.VirtualFrame;
import gui.virtual.VirtualPanel;

import java.awt.Point;

import dygraph.ProfileQueryEngine;
import dygraph.compare.NavigationPanel.Direction;
import dygraph.compare.panels.KeywordsPanel;

public class CompareFrame extends VirtualFrame {
	
	final public static int NAV_BAR_WIDTH = 80;
	
	protected ProfileQueryEngine prof1;
	protected ProfileQueryEngine prof2;
	
	protected VirtualPanel previousPanel;
	protected VirtualPanel nextPanel;
	protected VirtualPanel contentPanel;
	
	public CompareFrame(ProfileQueryEngine prof1, ProfileQueryEngine prof2,
			int x, int y, int width, int height) {
		super(x,y,width,height);
		this.prof1 = prof1;
		this.prof2 = prof2;
	}
	
	@Override
	public void initialize() {
		previousPanel = new NavigationPanel(0,DEFAULT_BAR_THICKNESS,
				NAV_BAR_WIDTH,getHeight()-DEFAULT_BAR_THICKNESS,Direction.LEFT);
		add(previousPanel);
		nextPanel = new NavigationPanel(getWidth()-NAV_BAR_WIDTH,
				DEFAULT_BAR_THICKNESS,NAV_BAR_WIDTH,getHeight()-DEFAULT_BAR_THICKNESS,Direction.RIGHT);
		add(nextPanel);
		contentPanel = new KeywordsPanel(this,prof1,prof2);
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
	
	public void finish() {
		contentPanel.finish();
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
