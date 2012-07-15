
package dygraph;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;

import gui.console.Console;
import gui.graph.example.*;

import javax.swing.JApplet;

import com.restfb.DefaultFacebookClient;


public class DygraphApplet extends JApplet{
    
	final static int DEFAULT_WIDTH = 1600;
	final static int DEFAULT_HEIGHT = 900;
	final static String DEBUG_TOKEN = "AAACEdEose0cBAHr3p1AUGJRKMiHSrHTCueL2IeWOWSXoTLiyYbmZAyRqotjuDR6aWk0XBG7sYOWfN2xmSJCo6KXOytR2cxidsbuUdgrJZBSwjtkVek";
									
	FacebookGraphController c;
	
    @Override
    public void init() {
    	try {
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
    
    private void launchNewController() {
    	final JApplet dygraph = this;
		dygraph.setSize(1600,900);
    	String access_token = getParameter("access_token");
    	if (access_token != null) {
    		System.out.println("access_token: " + access_token);
        	ProfileQueryEngine.FB = new DefaultFacebookClient(access_token);
    	} else {
    		System.out.println("dummy_token: " + DEBUG_TOKEN);
    		ProfileQueryEngine.FB = new DefaultFacebookClient(DEBUG_TOKEN);
    	}
    	ProfileQueryEngine.fetchFriendData();
        c = new FacebookGraphController(dygraph);
        c.launch();
        Console.getInstance().display();
    }
    
    @Override
    public void start() {
    }
    
    @Override
    public void stop() {
    }
}
