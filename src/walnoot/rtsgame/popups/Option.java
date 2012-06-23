package walnoot.rtsgame.popups;

import java.awt.Color;
import java.awt.Graphics;

import walnoot.rtsgame.screen.RTSFont;
import walnoot.rtsgame.screen.Screen;

public abstract class Option {
	public String name;
	public Popup popup;
	
	
	public Option(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void render(Graphics g, OptionsPopup popup, int index){
		Screen.font.drawLine(g, name, popup.getScreenX() + 16, popup.getScreenY() + 16 + index * RTSFont.HEIGHT);
	}
	
	public void renderInColor(Graphics g, OptionsPopup popup, int index, Color c){
		Screen.font.drawLineAndShadow(g, name, popup.getScreenX() + 16, popup.getScreenY() + 16 + index * RTSFont.HEIGHT, c);
	}
	
	public Popup getPopup(){
		return popup;
	}
	
	public abstract void onClick();
}
