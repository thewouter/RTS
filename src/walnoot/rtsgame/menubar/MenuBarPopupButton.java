package walnoot.rtsgame.menubar;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.SPGameScreen;

public abstract class MenuBarPopupButton {
	BufferedImage image;
	public GameScreen screen;
	
	public MenuBarPopupButton(BufferedImage i, GameScreen screen){
		image = i;
		this.screen = screen;
	}
	
	public abstract void onLeftClick();
	
	public void render(Graphics g, int xPos, int yPos){
		g.drawImage(image,xPos,yPos,null);
	}
}
