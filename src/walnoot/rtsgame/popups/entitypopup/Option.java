package walnoot.rtsgame.popups.entitypopup;

import java.awt.Color;
import java.awt.Graphics;

import walnoot.rtsgame.RTSFont;
import walnoot.rtsgame.screen.Screen;

public abstract class Option {
	public String name;
	public EntityPopup popup;
	private EntityOptionsPopup owner;
	
	
	public Option(String name, EntityOptionsPopup owner){
		this.name = name;
		this.owner = owner;
	}
	
	public String getName(){
		return name;
	}
	
	public void render(Graphics g, int index){
		Screen.font.drawLine(g, name, owner.getScreenX() + 16, owner.getScreenY() + 16 + index * RTSFont.HEIGHT);
	}
	
	public void renderInColor(Graphics g, int index, Color c){
		Screen.font.drawLineAndShadow(g, name, owner.getScreenX() + 16, owner.getScreenY() + 16 + index * RTSFont.HEIGHT, c);
	}
	
	public EntityPopup getPopup(){
		return popup;
	}
	
	public abstract void onClick();
}
