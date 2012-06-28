package walnoot.rtsgame.menubar;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class MenuBarPopupButton {
	BufferedImage image;
	
	public MenuBarPopupButton(BufferedImage i){
		image = i;
	}
	
	public abstract void onLeftClick();
	
	public void render(Graphics g, int xPos, int yPos){
		g.drawImage(image,xPos,yPos,null);
	}
}
