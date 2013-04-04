package walnoot.rtsgame.popups.entitypopup;

import java.awt.Color;
import java.awt.Graphics;

import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.rest.RTSFont;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.Screen;

public class EntityTextPopup extends EntityPopup {
	private final String[] text;
	private int width, height;
	private int screenX;
	private int screenY;

	public EntityTextPopup(Entity owner, GameScreen screen, String...text){
		super(owner,screen );
		this.text = text;
		
		for(String line: text){
			int lineWidth = Screen.font.getLineWidth(line);
			
			if(lineWidth > width) width = lineWidth;
		}
		
		height = RTSFont.HEIGHT * text.length + EMPTY_SPACE;
		width += EMPTY_SPACE;
	}
	
	public void render(Graphics g){
		drawBox(g, width, height, screenX, screenY);
		
		g.setColor(Color.BLACK);
		
		for(int i = 0; i < text.length; i++){
			//g.drawString(text[i], getscreenX() + 16, getScreenY() + 16 + i * RTSFont.HEIGHT);
			Screen.font.drawLine(g, text[i], screenX + EMPTY_SPACE / 2, screenY + EMPTY_SPACE / 2 + i * RTSFont.HEIGHT);
		}
	}
	
	public void update(int mouseX, int mouseY){
		screenX = owner.getScreenX() + screen.translationX;
		screenY = owner.getScreenY() + screen.translationY;
		
	}

	public void onLeftClick(int mouseX, int mouseY) {}
	
	public boolean isInPopup(int x , int y){
		return false;
	}
}
