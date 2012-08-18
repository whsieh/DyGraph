package dygraph;

import gui.graph.AbstractPainter;
import gui.graph.EdgePainter;
import gui.graph.GraphViewer;
import gui.graph.VertexPainter;
import gui.graph.util.Data;
import gui.graph.util.Message;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.List;

import util.misc.ImageUtil;
import dygraph.DygraphController.Mode;

public class FacebookVertexPainter extends VertexPainter{
	
	private final static AlphaComposite ALPHA_COMP = 
			AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f);
	
    protected BufferedImage displayPic;
    protected int width;
    protected int height;
    protected ProfileQueryEngine pqe;
    protected DygraphViewer dView;
    
    protected volatile boolean isLoading;

	public FacebookVertexPainter(GraphViewer graphPane, int xPos, int yPos, String id, ProfileQueryEngine pqe) {
		this(graphPane, xPos, yPos, id, id, pqe);
	}
	
    public FacebookVertexPainter(GraphViewer graphPane, int xPos, int yPos, String id, String displayName, ProfileQueryEngine pqe) {
        super(graphPane, xPos, yPos, id, displayName);
        this.pqe = pqe;
        if (id.equals(ProfileQueryEngine.CURRENT_USER.key())) {
        	pqe.fetchPicture();
        	displayPic = ImageUtil.scalePicture(pqe.image);
        	
        } else {
        	displayPic = pqe.fetchPicture();
        }
        this.width = displayPic.getWidth();
        this.height = displayPic.getHeight();
		if (graphPane instanceof DygraphViewer) {
			dView = (DygraphViewer)graphPane;
		}
    }
    
	@Override
	public void inform(Message message, Data d) {
		java.util.Set<String> messages = new java.util.HashSet<String>(); 
		if (message == Message.MOUSE_CLICKED) {
			for (EdgePainter ep : myEdges) {
				List<String> postData = (List<String>)ep.getEdge().getData("post");
				if (postData != null) {
					for (String msg : postData) {
						messages.add(msg);
					}
				}
			}
			for (String msg : messages) {
				System.out.println("\n\n\n" + msg);
			}
		}
		super.inform(message, d);
	}
    
	protected String getDisplayName() {
		return displayName;
	}
	
	public void setLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}
	
	public boolean isLoading() {
		return isLoading;
	}
	
	@Override
	public void remove() {
		super.remove();
		if (dView != null) {
			dView.profiles.remove(id);
		}
	}
    
    @Override
    protected void paintDefault(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        paintProfilePicture(g2d, STATE_COLORS[AbstractPainter.DEFAULT][0],
        STATE_COLORS[AbstractPainter.DEFAULT][1]);
    }
    
	@Override
	protected void paintHighlighted(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		paintProfilePicture(g2d, STATE_COLORS[AbstractPainter.HIGHLIGHTED][0],
				STATE_COLORS[AbstractPainter.HIGHLIGHTED][1]);
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
    public boolean contains(int xPos, int yPos) {
    	int halfX = width/2;
    	int halfY = height/2;
    	return (x - halfX < xPos && xPos < x + halfX) && (y - halfY < yPos && yPos < y + halfY);
    }
    
    private void paintProfilePicture(Graphics2D g2d, Color cInner, Color cOuter) {
    	
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
    	
        Color c = g2d.getColor();
        Stroke s = g2d.getStroke();

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
        if (dView.dController.getMode() == Mode.COMPARE) {
        	Composite composite = g2d.getComposite();
        	g2d.fillOval(x - 8, y - 8, 16, 16);
        	g2d.setComposite(ALPHA_COMP);
            g2d.drawRoundRect(x - 2 - halfWidth, y - 2 - halfHeight, width+2, height+2,5,5);
            g2d.drawImage(displayPic, x - halfWidth, y - halfHeight,	myParent);
            g2d.setComposite(composite);
        } else {
	        g2d.drawRoundRect(x - 2 - halfWidth, y - 2 - halfHeight, width+2, height+2,5,5);
	        g2d.drawImage(displayPic, x - halfWidth, y - halfHeight,	myParent);
        }
        
        if (isLoading) {
        	g2d.drawImage(DygraphResource.GREEN_PLUS, x+halfWidth, y-halfHeight-25, myParent);
        }
        g2d.setFont(new Font("Arial", 1, 16));
        g2d.drawString(displayName, x - halfWidth - 15, y + halfHeight + 15);
        g2d.setStroke(s);
        g2d.setColor(c);
        
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
        
    }

}
