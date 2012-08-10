package gui.virtual;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class ColorScheme {

	final static Color TRANSPARENT = new Color(255,255,255,0);  
	
	static public class ColorSchemeData {
		
		private int context;
		private Color color;
		
		public ColorSchemeData(int context, Color color) {
			this.context = context;
			this.color = color;
		}
	}
	
	private Map<Integer,Color> colors;
	private Color defaultColor;
	
	public ColorScheme(ColorSchemeData...data) {
		this(TRANSPARENT,data);
	}
	
	public ColorScheme(Color defaultColor,ColorSchemeData...data) {
		colors = new HashMap<Integer,Color>();
		for (ColorSchemeData csd : data) {
			colors.put(csd.context,csd.color);
		}
		this.defaultColor = defaultColor;
	}
	
	public Color getColor(int context) {
		Color c = colors.get(context);
		return c == null ? defaultColor : c;
	}
	
}
