package gui.virtual.impl;

import gui.virtual.VirtualLabel;

public class LoadingLabel extends VirtualLabel {
	
	protected static final long serialVersionUID = 1L;
	
	public final static String LOADING_STRING = "Loading";
	public final static int MAX_LOAD_COUNT = 3;
	public final static int WAIT_PERIOD_MS = 500;
	
	protected Thread textUpdator;
	protected int loadCount;
	protected boolean isFinished;
	
	public LoadingLabel(int x, int y, String fontStyle,
			int fontModifier, int fontSize) {
		super(x, y, LOADING_STRING, fontStyle, fontModifier, fontSize);
		loadCount = 1;
		isFinished = false;
		textUpdator = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					while (!isFinished) {
						Thread.sleep(WAIT_PERIOD_MS);
						updateText();
					}
				} catch (InterruptedException e) {
				}
			}
		});
	}
	
	public void updateText() {
		String punctuation = ".";
		for (int i = 0; i < loadCount%MAX_LOAD_COUNT; i++) {
			punctuation += ".";
		}
		setText(LOADING_STRING + punctuation);
		loadCount++;
		
	}

	@Override
	public void initialize() {
		textUpdator.start();
	}
	
	@Override
	public void finish() {
		isFinished = true;
	}
	
}
