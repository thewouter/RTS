package walnoot.rtsgame.menubar;

import java.awt.Color;
import java.awt.Graphics;

import walnoot.rtsgame.RTSFont;
import walnoot.rtsgame.screen.Screen;

public abstract class MenubarTextField extends Button{

	String text = "";

	public MenubarTextField(MenuBar bar) {
		super(null, bar);
	}
	
	public void onLeftClick() {}
	
	public void update(){
		this.text = getText();
	}
	
	public abstract String getText();
	
	public void render(Graphics g, int xPos, int yPos){
		g.setColor(Color.WHITE);
		Screen.font.drawLine(g, text, xPos + 1, yPos + (bar.HEIGHT_BUTTON - RTSFont.HEIGHT) / 2);
	}
	
}
