package dygraph;

import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import util.misc.ImageLibrary;
import util.misc.ImageUtil;

public final class DygraphResource {

	private DygraphResource() {
		/* Should never access this constructor */
	}
	
	public static void loadAll() {
		GREEN_PLUS = ImageUtil.transformToTransparency(ImageLibrary.grabImage("http://dygraph.netne.net/img/green_plus.png"));
		CAT_PICTURE = ImageLibrary.grabImage("http://dygraph.netne.net/img/cat_picture.jpg");
		try {
			URL loadURL = new URL("http://dygraph.netne.net/img/loading.gif");
			LOADING = new ImageIcon(loadURL);
		} catch (MalformedURLException e) {
			System.err.println("Could not load loading.gif :(");
		}
	}
	
	public static Image GREEN_PLUS = null;
	public static Image CAT_PICTURE = null;
	public static Icon LOADING = null;
	
}
