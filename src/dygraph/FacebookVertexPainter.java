package dygraph;

import java.io.IOException;

import gui.graph.*;
import java.awt.image.BufferedImage;

public class FacebookVertexPainter extends VertexPainter{

	// TODO profile pic painting, add mouseover effect that makes buttons fade in next to the
	// vertex. (Ex. button to delete node, button to view the person's Facebook profile, and a button
	// to expand the node's connections.)
        protected BufferedImage image;
	protected final static int WIDTH = 10;
        
	FacebookVertexPainter(GraphViewer graphPane, int xPos, int yPos, String id) {
		this(graphPane, xPos, yPos, id, id);
	}
        
        FacebookVertexPainter(GraphViewer graphPane, int xPos, int yPos, String id, String displayName) {
            super(graphPane, xPos, yPos, id, displayName);
            ProfileQueryEngine engine = new ProfileQueryEngine(id);
            image = engine.fetchPicture();
        }
        
        @Override
        void paintDefault(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        paintProfilePicture(g2d, STATE_COLORS[AbstractPainter.DEFAULT][0],
        STATE_COLORS[AbstractPainter.DEFAULT][1]);
        }

        @Override
        void paintFocused(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        paintProfilePicture(g2d, STATE_COLORS[AbstractPainter.FOCUSED][0],
        STATE_COLORS[AbstractPainter.FOCUSED][1]);
        }

        @Override
        void paintSelected(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        paintProfilePicture(g2d, STATE_COLORS[AbstractPainter.SELECTED][0],
        STATE_COLORS[AbstractPainter.SELECTED][1]);
        }
        
        private void paintProfilePicture(Graphics2D g2d, Color cInner, Color cOuter) {
            Stroke s = g2d.getStroke();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(cInner);
            g2d.fillRect(x - 2 - 0.5*WIDTH, y - 2 - 0.5*WIDTH, WIDTH+2, (int)(whRatio*WIDTH)+2);
            
            g2d.setColor(cOuter);
            g2d.setStroke(new BasicStroke(4));
            g2d.drawRect(x - 2 - 0.5*WIDTH, y - 2 - 0.5*WIDTH, WIDTH+2, (int)(whRatio*WIDTH)+2);
            
            int width = image.getWidth();
            int height = image.getHeight();
            float whRatio = width / height;
            int type = image.getType();
            BufferedImage resizedImage = new BufferedImage(WIDTH, (int)(whRatio*WIDTH), type);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(Image, x - 0.5*WIDTH, y - 0.5*WIDTH, WIDTH, (int)(whRatio*WIDTH), null);
            g.dispose();
            
            g2d.drawString(displayName, x + RADIUS, y);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_OFF);
            g2d.setStroke(s);
        }
	
}
