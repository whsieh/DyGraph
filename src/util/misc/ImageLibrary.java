
package util.misc;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class ImageLibrary {
    
    private ImageLibrary(){}
    
    private static Map<String,Image> images = new HashMap<String,Image>();
    
    static public Image transformToTransparency(BufferedImage image) {
        return transformToTransparency(image,Color.WHITE);
    }
    
    static public Image transformToTransparency(BufferedImage image,final Color color) {
        ImageFilter filter = new RGBImageFilter(){
            @Override
            public final int filterRGB(int x, int y, int rgb){
                if(rgb >= color.getRGB())
                    return 0x00FFFFFF & rgb;
                return rgb;
            }
        };
        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
    
    static public Image grabImage(String imageName, boolean transparent) {
        Image i = null;
        if (!images.containsKey(imageName)) {
            try {
            	if (imageName.contains("http")) {
            		i = ImageIO.read(new URL(imageName));
            	} else {
            		i = ImageIO.read(new File(imageName));
            	}
                if (transparent) {
                    i = transformToTransparency((BufferedImage)i);
                }
                images.put(imageName, i);
            } catch (IOException e) {
                System.err.println("ERROR: image resource not found."
                        + " Exiting application.");
                System.exit(1);
            }
        } else {
            i = images.get(imageName);
        }
        return i;
    }
    
    static public Image grabImage(String imageName) {
        return grabImage(imageName,false);
    }
    
}
