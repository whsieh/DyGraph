package gui.virtual;

import javax.swing.JPanel;

public abstract class Animator<T extends VirtualComponent> implements Runnable {
	
	static final int DEFAULT_STEP_RATE = 60;
	
	protected T comp;
	protected int durationMs;
	protected int stepMs;
	protected int elapsedMs;

	public static void run(Animator a) {
		new Thread(a).start();
	}
	
	public static void registerAnimations(final JPanel panel) {
		new Thread(new Runnable(){
			@Override
			public void run() {
				while(true) {
					try {
						panel.repaint();
						Thread.sleep(15);
					} catch (InterruptedException e) {}
				}
			}
		}).start();
	}
	
	public Animator(T component, int maxDurationMs, int stepRate) {
		this.comp = component;
		this.durationMs = maxDurationMs <= 0 ? 1 : maxDurationMs;
		this.stepMs = (int)Math.round(1000.0 / stepRate);
		this.elapsedMs = 0;
	}
	
	public Animator(T component) {
		this.comp = component;
		this.durationMs = Integer.MAX_VALUE;
		this.stepMs = (int)Math.round(1000.0 / DEFAULT_STEP_RATE);
		this.elapsedMs = 0;
	}
	
	public Animator(T component, int maxDurationMs) {
		this(component, maxDurationMs, DEFAULT_STEP_RATE);
	}
	
	@Override
	public void run() {
		long startTime,animationTime;
		while (!checkIsDone()) {
			startTime = System.nanoTime();
			step();
			animationTime = (System.nanoTime() - startTime) / 1000000;
			try {
				long remaining = stepMs - animationTime;
				if (remaining > 0) {
					Thread.sleep(remaining);
				}
				elapsedMs += stepMs;
			} catch (InterruptedException e){}
		}
		performOnFinish();
	}
	
	public void performOnFinish() {
	}
	
	public boolean checkIsDone() {
		return elapsedMs > durationMs;
	}
	abstract public void step();
}
