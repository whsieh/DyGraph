package dygraph;

import java.awt.Image;

import util.misc.ImageLibrary;

public final class DygraphResource {

	private DygraphResource() {
		/* Should never access this constructor */
	}
	
	public static void loadAll() {
		GREEN_PLUS = ImageLibrary.grabImage("http://dygraph.herobo.com/img/green_plus.png", true);
		CAT_PICTURE = ImageLibrary.grabImage("http://dygraph.herobo.com/img/cat_picture.jpg");
	}
	
	public static Image GREEN_PLUS = null;
	public static Image CAT_PICTURE = null;
	
}
