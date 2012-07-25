package dygraph;

import java.io.IOException;

import gui.graph.*;
import java.awt.image.BufferedImage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import util.misc.ImageLibrary;

public class FacebookVertexPainter extends VertexPainter{

	// TODO profile pic painting, (EDITED: don't worry about the mouseover effect thing. That'll make things
	// too complicated. A context menu is enough)
	
    protected BufferedImage image;
    protected int width;
    protected int height;
    
    protected volatile boolean isLoading;
	private int degree = 0;
    
	FacebookVertexPainter(GraphViewer graphPane, int xPos, int yPos, String id) {
		this(graphPane, xPos, yPos, id, id);
	}
	
    FacebookVertexPainter(GraphViewer graphPane, int xPos, int yPos, String id, String displayName) {
        super(graphPane, xPos, yPos, id, displayName);
        image = ProfileQueryEngine.fetchPicture(id);
        this.width = image.getWidth();
        this.height = image.getHeight();
    }
    
	protected String getDisplayName() {
		return displayName;
	}
	
	public void setLoading(boolean isLoading) {
		this.isLoading = isLoading;
		if (!isLoading) {
			degree = 0;
		}
	}
	
	public boolean isLoading() {
		return isLoading;
	}
    
    @Override
    protected void paintDefault(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        paintProfilePicture(g2d, STATE_COLORS[AbstractPainter.DEFAULT][0],
        STATE_COLORS[AbstractPainter.DEFAULT][1]);
    }

    @Override
    protected void paintFocused(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        paintProfilePicture(g2d, STATE_COLORS[AbstractPainter.FOCUSED][0],
        STATE_COLORS[AbstractPainter.FOCUSED][1]);
    }
    
    @Override
    protected void paintSelected(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        paintProfilePicture(g2d, STATE_COLORS[AbstractPainter.SELECTED][0],
        STATE_COLORS[AbstractPainter.SELECTED][1]);
    }
    
    @Override
    protected boolean contains(int xPos, int yPos) {
    	int halfX = width/2;
    	int halfY = height/2;
    	return (x - halfX < xPos && xPos < x + halfX) && (y - halfY < yPos && yPos < y + halfY);
    }
    
    private void paintProfilePicture(Graphics2D g2d, Color cInner, Color cOuter) {
    	
        Color c = g2d.getColor();
        Stroke s = g2d.getStroke();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

        int halfWidth = width/2;
        int halfHeight = height/2;
        
        g2d.setColor(cInner);
        g2d.drawRoundRect(x - 2 - halfWidth, y - 2 - halfHeight, width+2, height+2,5,5);
        
        g2d.setColor(cOuter);
    	
        if (isLoading) {
        	g2d.setStroke(new BasicStroke(8));
        } else {
        	g2d.setStroke(new BasicStroke(4));
        }
        
        g2d.drawRoundRect(x - 2 - halfWidth, y - 2 - halfHeight, width+2, height+2,5,5);
        
        g2d.drawImage(image, x - halfWidth, y - halfHeight,	myParent);
        
        if (isLoading) {
        	g2d.drawImage(ImageLibrary.grabImage("http://dygraph.herobo.com/img/green_plus.png", true), x+halfWidth, y-halfHeight-25, myParent);
        }
        g2d.setFont(new Font("Arial", 1, 16));
        g2d.drawString(displayName, x - halfWidth - 15, y + halfHeight + 15);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setStroke(s);
        g2d.setColor(c);
    }

}
