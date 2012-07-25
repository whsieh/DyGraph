
package dygraph;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.util.HashSet;
import java.util.Set;

import gui.graph.example.*;

import javax.swing.JApplet;

import com.restfb.DefaultFacebookClient;


final public class DygraphApplet extends JApplet{
    
	final static int DEFAULT_WIDTH = 1600;
	final static int DEFAULT_HEIGHT = 900;
	final static String DEBUG_TOKEN = "AAACEdEose0cBABIgPBoGcpZCy9DZA6mkUP0x9niZByMQQS3rDBtbHuQLiIpIOc7xr1HibTFzneJBjMzzVPKUOPL4e8UkAdX5LGY0NWocqF7vxI93keD";
	DygraphController c;
	
    @Override
    public void init() {
    	try {
    		this.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent arg0) {
					if (c != null && c.getView() != null) {
						c.getView().updateMidpoint();
					}
				}
				@Override
				public void componentMoved(ComponentEvent arg0) {
					if (c != null && c.getView() != null) {
						c.getView().updateMidpoint();
					}
				}
			});
	    	AccessController.doPrivileged(
				new PrivilegedAction(){
					public Object run() {
						launchNewController();
						return null;
					}
				}
	        );
    	} catch (Exception e) {
    		System.err.println("An error has occurred. See below for details:");
    		e.printStackTrace();
    		throw e;
    	}
    }
    
    public void popURL(URL url) {
		getAppletContext().showDocument(url, "_blank");
    }
    
    private void launchNewController() {
		setSize(1600,900);
    	String access_token = getParameter("access_token");
    	if (access_token != null) {
    		System.out.println("access_token: " + access_token);
        	ProfileQueryEngine.FB = new DefaultFacebookClient(access_token);
    	} else {
    		System.out.println("dummy_token: " + DEBUG_TOKEN);
    		ProfileQueryEngine.FB = new DefaultFacebookClient(DEBUG_TOKEN);
    	}
    	ProfileQueryEngine.fetchFriendData();
        c = new DygraphController(this);
        c.launch();
        DyGraphConsole.getInstance().display();
        DyGraphConsole.getInstance().setController(c);
    }
    
    @Override
    public void start() {
    }
    
    @Override
    public void stop() {
    }
}
