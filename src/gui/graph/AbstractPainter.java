package gui.graph;

import gui.graph.util.Message;
import gui.graph.util.Data;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

abstract class AbstractPainter {
    
    static final int DEFAULT = 0;
    static final int FOCUSED = 1;
    static final int SELECTED = 2;
    
    int state;
    GViewer myParent;
    String label;
    
    abstract boolean contains(int x, int y);
    
    abstract void paint(Graphics g);
    
    abstract void inform(Message message, Data e);

    void setState(int state){
        this.state = state;
    }
    
    static Image transformWhiteToTransparency(BufferedImage image) {
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
