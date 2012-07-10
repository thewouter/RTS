package walnoot.rtsgame.popups.screenpopup;

import java.awt.Graphics;

import walnoot.rtsgame.InputHandler;

public abstract class ScreenPopupPart{
	int xPos, yPos, width, height;
	InputHandler input;
	public ScreenPopup owner;
	
	public abstract void render(Graphics g);
	public abstract void update(int xPos,int yPos, int width);
	
}
