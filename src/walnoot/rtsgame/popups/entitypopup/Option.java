package walnoot.rtsgame.popups.entitypopup;

import java.awt.Color;
import java.awt.Graphics;

import walnoot.rtsgame.rest.RTSFont;
import walnoot.rtsgame.screen.Screen;

public abstract class Option {
	public String name;
	public EntityOptionsPopup owner;
	
	
	public Option(String name, EntityOptionsPopup owner){
		this.name = name;
		this.owner = owner;
	}
	
	public String getName(){
		return name;
	}
	
	public void render(Graphics g, int index){
		Screen.font.drawLine(g, name, owner.screenX + 16, owner.screenY + 16 + index * RTSFont.HEIGHT);
	}
	
	public void renderInColor(Graphics g, int index, Color c){
		Screen.font.drawLineAndShadow(g, name, owner.screenX + 16, owner.screenY + 16 + index * RTSFont.HEIGHT, c);
	}
	
	public EntityOptionsPopup getPopup(){
		return owner;
	}
	
	public abstract void onClick();
}
