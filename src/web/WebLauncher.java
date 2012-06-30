
package web;

import gui.graph.impl.*;
import javax.swing.JApplet;

public class WebLauncher extends JApplet{
    
    BasicController c;
    
    @Override
    public void init() {
    	String accessToken = getParameter("access_token");
    	if (accessToken.length() > 20) {
	        c = new BasicController(this);
	        c.launch();
    	}
    }
    
    @Override
    public void start() {
    }
    
    @Override
    public void stop() {
    }
}
