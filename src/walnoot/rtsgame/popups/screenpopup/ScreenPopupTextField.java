package walnoot.rtsgame.popups.screenpopup;

import java.awt.Color;
import java.awt.Graphics;

import walnoot.rtsgame.RTSFont;
import walnoot.rtsgame.screen.Screen;

public class ScreenPopupTextField extends ScreenPopupPart {
	private String text;
	private Color color;
	private static int EMPTY_SPACE = 5;
	
	public ScreenPopupTextField(String text){
		this.text = text;
		color = Color.BLACK;
		width = Screen.font.getLineWidth(text) + EMPTY_SPACE;
		height = RTSFont.HEIGHT;
	}
	
	public ScreenPopupTextField(String text, Color color){
		this.text = text;
		this.color = color;
		width = Screen.font.getLineWidth(text) + EMPTY_SPACE;
	}
	
	public void render(Graphics g) {
		Screen.font.drawLine(g, text, xPos, yPos, color);
	}
	
	public void update(int xPos, int yPos, int width) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		if(Screen.font.getLineWidth(text) + EMPTY_SPACE > width) width = Screen.font.getLineWidth(text) + EMPTY_SPACE;
	}
	
	public void setText(String text){
		this.text = text;
	}

}
