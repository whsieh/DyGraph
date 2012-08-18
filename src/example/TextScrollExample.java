package example;

import gui.virtual.*;
import gui.virtual.impl.TextScroller;

import javax.swing.*;

public class TextScrollExample {

	public static void main(String[] args) {
		
		final JFrame frame = new JFrame();
		frame.setBounds(100,100,1200,800);
		frame.setLayout(null);
		final VirtualFrame subFrame = new VirtualFrame(20,20,1160,740);
		subFrame.setTitle("Scrolling test");
		frame.add(subFrame);
		frame.setVisible(true);
		
		final TextScroller scroller = new TextScroller(25,VirtualFrame.DEFAULT_BAR_THICKNESS+25,400,650);
		subFrame.add(scroller);
		scroller.addLabel("A________________","Arial",1,25);
		scroller.addLabel("B________________","Arial",1,15);
		scroller.addLabel("C________________","Arial",1,20);
		scroller.addLabel("D________________","Arial",1,25);
		scroller.addLabel("E________________","Arial",1,15);
		scroller.addLabel("F________________","Arial",1,20);
		scroller.addLabel("G________________","Arial",1,25);
		scroller.addLabel("H________________","Arial",1,15);
		scroller.addLabel("I________________","Arial",1,20);
		scroller.addLabel("J________________","Arial",1,25);
		scroller.addLabel("K________________","Arial",1,15);
		scroller.addLabel("L________________","Arial",1,20);
		scroller.addLabel("M________________","Arial",1,25);
		scroller.addLabel("N________________","Arial",1,15);
		scroller.addLabel("O________________","Arial",1,20);
		scroller.addLabel("P________________","Arial",1,25);
		scroller.addLabel("Q________________","Arial",1,15);
		scroller.addLabel("R________________","Arial",1,20);
		scroller.addLabel("S________________","Arial",1,25);
		scroller.addLabel("T________________","Arial",1,15);
		scroller.addLabel("U________________","Arial",1,20);
		scroller.addLabel("V________________","Arial",1,25);
		scroller.addLabel("W________________","Arial",1,15);
		scroller.addLabel("X________________","Arial",1,20);
		scroller.addLabel("Y________________","Arial",1,25);
		scroller.addLabel("Z________________","Arial",1,15);
		scroller.addLabel("AA_______________","Arial",1,25);
		scroller.addLabel("BB_______________","Arial",1,15);
		scroller.addLabel("CC_______________","Arial",1,20);
		scroller.addLabel("DD_______________","Arial",1,25);
		scroller.addLabel("EE_______________","Arial",1,15);
		scroller.addLabel("FF_______________","Arial",1,20);
		scroller.addLabel("GG_______________","Arial",1,25);
		scroller.addLabel("HH_______________","Arial",1,15);
		scroller.addLabel("II_______________","Arial",1,20);
		scroller.initialize();
	}	
	
	
	
}
