package walnoot.rtsgame;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import walnoot.rtsgame.rest.Options;

public class GameApplet extends Applet {
	private static final long serialVersionUID = -4657281424449019830L;

	public void init(){

		JFrame frame = new JFrame("Tribe");

		setPreferredSize(new Dimension(800, 600));
		setSize(getPreferredSize());
		setLayout(new BorderLayout(0, 0));

		RTSComponent component = new RTSComponent(frame);
		add(component, BorderLayout.CENTER);

		new Thread(component, "Tribe").start();
	}
}