package gui.graph;

import gui.graph.util.Data;
import gui.graph.util.Message;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

public abstract class AbstractPainter {
    
    protected static final int DEFAULT = 0;
    protected static final int HIGHLIGHTED = 1;
    protected static final int FOCUSED = 2;
    protected static final int ACCENTUATED = 3;
    protected static final int SELECTED = 4;
    
    protected int state;
    protected String id;
    
    protected abstract boolean contains(int x, int y);
    
    protected void paint(Graphics g) {
    	switch(state) {
	    	case DEFAULT:
	    		paintDefault(g);
	    		break;
	    	case HIGHLIGHTED:
	    		paintHighlighted(g);
	    		break;
	    	case FOCUSED:
	    		paintFocused(g);
	    		break;
	    	case ACCENTUATED:
	    		paintAccentuated(g);
	    		break;
	    	case SELECTED:
	    		paintSelected(g);
	    		break;
    	}
    }
    
    public String getID() {
    	return id;
    }
    
    public abstract void inform(Message message, Data e);

    protected void setState(int state){ 
        this.state = state;
    }
    
    protected abstract void paintHighlighted(Graphics g);
    
    protected abstract void paintDefault(Graphics g);
    
    protected abstract void paintFocused(Graphics g);
    
    protected abstract void paintAccentuated(Graphics g);
    
    protected abstract void paintSelected(Graphics g);
    
    public static Image transformWhiteToTransparency(BufferedImage image) {
        ImageFilter filter = new RGBImageFilter(){
            @Override
            public final int filterRGB(int x, int y, int rgb){
                if(rgb >= Color.WHITE.getRGB())
                    return 0x00FFFFFF & rgb;
                return rgb;
            }
        };

        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
    
}
