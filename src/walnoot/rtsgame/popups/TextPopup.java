package walnoot.rtsgame.popups;

import java.awt.Color;
import java.awt.Graphics;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.screen.RTSFont;
import walnoot.rtsgame.screen.Screen;

public class TextPopup extends Popup {
	private final String[] text;
	private int width, height;

	public TextPopup(Entity owner, String...text){
		super(owner);
		this.text = text;
		
		for(String line: text){
			int lineWidth = Screen.font.getLineWidth(line);
			
			if(lineWidth > width) width = lineWidth;
		}
		
		height = RTSFont.HEIGHT * text.length + EMPTY_SPACE;
		width += EMPTY_SPACE;
	}
	
	public void render(Graphics g){
		drawBox(g, width, height);
		
		g.setColor(Color.BLACK);
		
		for(int i = 0; i < text.length; i++){
			//g.drawString(text[i], getscreenX() + 16, getScreenY() + 16 + i * RTSFont.HEIGHT);
			Screen.font.drawLine(g, text[i], getScreenX() + EMPTY_SPACE / 2, getScreenY() + EMPTY_SPACE / 2 + i * RTSFont.HEIGHT);
		}
	}
	
	public void update(int translationX, int translationY, int mouseX, int mouseY){}

	public void onLeftClick(int mouseX, int mouseY) {}
	public boolean isInPopup(int x , int y){
		return false;}
}
