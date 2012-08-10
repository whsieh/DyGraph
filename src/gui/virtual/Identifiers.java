package gui.virtual;

public final class Identifiers {

	private Identifiers() { }
	
	public static class POS {
	
		public final static int BORDER = 1 << 0;
		public final static int BACKGROUND = 1 << 1;
		public final static int TITLE_BAR = 1 << 2;
		public final static int FOREGROUND = 1 << 3;
	
	}
	
	public static class STATE { 
	
		public final static int DEFAULT = 1 << 10;
		public final static int HIGHLIGHTED = 1 << 11;
		public final static int FOCUSED = 1 << 12;
		public final static int ACCENTUATED = 1 << 13;
		public final static int SELECTED = 1 << 14;
		
	}
	
	public static class EVENT {
	
		public final static int MOUSE_DRAGGED = 1 << 10;
		public final static int MOUSE_PRESSED = 1 << 11;
		public final static int MOUSE_RELEASED = 1 << 12;
		public final static int MOUSE_ENTERED = 1 << 13;
		public final static int MOUSE_EXITED = 1 << 14;
		public final static int MOUSE_OVER = 1 << 15;
		public final static int MOUSE_WHEEL = 1 << 16;
		public final static int KEY_PRESSED = 1 << 17;
		public final static int KEY_RELEASED = 1 << 18;
	
	}
}
