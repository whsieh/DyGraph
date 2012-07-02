package util.misc;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

import javax.swing.ImageIcon;

final public class ImageUtil {

	private ImageUtil(){}
	
	
	public static BufferedImage toBufferedImage(Image image) {
		 
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
 
        
        image = new ImageIcon(image).getImage();
 
        
        boolean hasAlpha = hasAlpha(image);
        
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        try {
            
            int transparency = Transparency.OPAQUE;
            if (hasAlpha == true) {
                transparency = Transparency.BITMASK;
            }
 
            
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image
                    .getHeight(null), transparency);
        } catch (HeadlessException e) {
        }
 
        if (bimage == null) {
            
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha == true) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image
                    .getHeight(null), type);
        }
 
        
        Graphics g = bimage.createGraphics();
 
        
        g.drawImage(image, 0, 0, null);
        g.dispose();
 
        return bimage;
    }
 
    public static boolean hasAlpha(Image image) {
        
        if (image instanceof BufferedImage) {
            return ((BufferedImage) image).getColorModel().hasAlpha();
        }
 
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }
 
        return pg.getColorModel().hasAlpha();
    }
	
}
