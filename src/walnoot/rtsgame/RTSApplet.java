package walnoot.rtsgame;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class RTSApplet extends Applet {
	private static final long serialVersionUID = 1L;
	private RTSComponent comp;
	
	public void init(){
		setPreferredSize(new Dimension(800, 600));
		setSize(new Dimension(800, 600));
		setLayout(new BorderLayout(0, 0));
		
		comp = new RTSComponent(this);
		add(comp, BorderLayout.CENTER);
		
		new Thread(comp, "Tribe main thread").start();
	}
}
