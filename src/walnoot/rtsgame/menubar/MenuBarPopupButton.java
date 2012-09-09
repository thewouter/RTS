package walnoot.rtsgame.menubar;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.Screen;

public abstract class MenuBarPopupButton {
	BufferedImage image;
	public GameScreen screen;
	
	
	public MenuBarPopupButton(BufferedImage i, GameScreen screen){
		image = i;
		this.screen = screen;
	}
	
	public abstract void onLeftClick();
	
	public abstract String getName();
	
	public void render(Graphics g, int xPos, int yPos){
		g.drawImage(image,xPos,yPos,null);
	}
	
	public void renderHoverOver(Graphics g, int xPos, int yPos){
		Screen.font.drawLine(g, getName(), xPos, yPos - image.getHeight());
	}
}
