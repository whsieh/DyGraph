package example;

import java.awt.Graphics;
import java.net.*;
import javax.swing.*;
  
public class AnimatedImageExample {
	
    public static void main(String[] args) throws MalformedURLException {
 
        URL url = new URL("http://dygraph.herobo.com/img/loading.gif");
        Icon icon = new ImageIcon(url);
        JLabel label = new JLabel(icon) {
        	@Override
        	public void paintComponent(Graphics g) {
        		super.paintComponent(g);
        		System.out.println("test");
        	}
        };
  
        JFrame f = new JFrame("Animation");
        f.getContentPane().add(label);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}