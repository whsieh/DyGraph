
package web;

import gui.graph.example.*;

import javax.swing.JApplet;

import com.restfb.DefaultFacebookClient;

import dygraph.ProfileQueryEngine;

public class WebLauncher extends JApplet{
    
	final static int DEFAULT_WIDTH = 1600;
	final static int DEFAULT_HEIGHT = 900;
	final static String DEBUG_TOKEN = "AAACEdEose0cBAKoy4zBiabmz1PPWm0tBrFC6urWMt1ewDN8C91jnrCorIMTOWECxUtWE9UsjC2l09kI4yDXdaDJjTvlXMDUSTYr0QLZBOwsna3bHk";
	
    BasicController c;
    
    @Override
    public void init() {
    	this.setSize(1600,900);
    	String access_token = getParameter("access_token");
    	if (access_token != null) {
        	ProfileQueryEngine.FB = new DefaultFacebookClient(access_token);
    	} else {
    		ProfileQueryEngine.FB = new DefaultFacebookClient(DEBUG_TOKEN);
    	}
    	ProfileQueryEngine.fetchFriendData();
        c = new BasicController(this);
        c.launch();
    }
    
    @Override
    public void start() {
    }
    
    @Override
    public void stop() {
    }
}
