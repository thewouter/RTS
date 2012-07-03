package walnoot.rtsgame.menubar;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Button {
	BufferedImage image;
	public MenuBar bar;
	
	public Button(BufferedImage i,MenuBar bar){
		image = i;
		this.bar = bar;
	}
	



	public abstract void onLeftClick();
	
	public void render(Graphics g, int xPos, int yPos){
		g.drawImage(image, xPos, yPos, null);
		
	}
}
