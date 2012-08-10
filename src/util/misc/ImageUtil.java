package util.misc;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

final public class ImageUtil {

	private ImageUtil(){}
    
	public static BufferedImage createImage(JComponent component) {
		Dimension d = component.getSize();

		if (d.width == 0 || d.height == 0) {
			d = component.getPreferredSize();
			component.setSize(d);
		}

		Rectangle region = new Rectangle(0, 0, d.width, d.height);
		return ImageUtil.createImage(component, region);
	}
	
	public static BufferedImage createImage(JComponent component, Rectangle region) {
		
		if (!component.isDisplayable()) {
			Dimension d = component.getSize();
			if (d.width == 0 || d.height == 0) {
				d = component.getPreferredSize();
				component.setSize(d);
			}
			layoutComponent(component);
		}

		BufferedImage image = new BufferedImage(region.width, region.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();

		if (!component.isOpaque()) {
			g2d.setColor( component.getBackground() );
			g2d.fillRect(region.x, region.y, region.width, region.height);
		}
		g2d.translate(-region.x, -region.y);
		component.paint(g2d);
		g2d.dispose();
		return image;
	}
	
	static void layoutComponent(Component component) {
		synchronized (component.getTreeLock()) {
			component.doLayout();
    	    if (component instanceof Container) {
            	for (Component child : ((Container)component).getComponents()) {
    	            layoutComponent(child);
        	    }
	        }
    	}
    }
	
	@Deprecated
	public static BufferedImage getGhostImage(BufferedImage image) {
		BufferedImage image_ghost = deepCopy(image);
		for (int y = 0; y < image_ghost.getHeight(); y++) {
		    for (int x = 0; x < image_ghost.getWidth(); x++) {
	           int rgb = image_ghost.getRGB(x, y);	           
	           image_ghost.setRGB(x, y, rgb | 0xFF000000);
		    }
		}
		return image_ghost;
	}
	
	public static BufferedImage deepCopy(BufferedImage bi) {
		
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	
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
