
package util.misc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageLibrary {
    
    private ImageLibrary(){}
    
    private static Map<String,BufferedImage> images = new HashMap<String,BufferedImage>();
    
    static public BufferedImage grabImage(String imageName) {
    	BufferedImage i = null;
        if (!images.containsKey(imageName)) {
            try {
            	if (imageName.contains("http")) {
            		i = ImageIO.read(new URL(imageName));
            	} else {
            		i = ImageIO.read(new File(imageName));
            	}
                images.put(imageName, i);
            } catch (IOException e) {
                System.err.println("ERROR: image resource not found: "
                        + imageName);
                System.exit(1);
            }
        } else {
            i = images.get(imageName);
        }
        return i;
    }
    
}
