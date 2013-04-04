package walnoot.rtsgame.popups.screenpopup;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.InputHandler.Key;
import walnoot.rtsgame.rest.RTSFont;
import walnoot.rtsgame.screen.Screen;

public class TextInput extends ScreenPopupPart {
	private LinkedList<String> text = new LinkedList<String>();
	private static int EMPTY_SPACE = 5;
	public boolean isActive = true;
	
	public TextInput(ScreenPopup owner,InputHandler input){
		height = RTSFont.HEIGHT + 2 * EMPTY_SPACE;
		this.owner = owner;
		this.input = input;
	}
	

	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(xPos, yPos, width, height);
		g.setColor(Color.GRAY);
		g.drawRect(xPos, yPos, width, height);
		String result = "";
		for(String s:text){
			result += s;
		}
		
		Screen.font.drawLine(g, result  + (isActive ? "_" : ""), xPos + EMPTY_SPACE, yPos + EMPTY_SPACE);
	}
	
	public void update(int xPos,int yPos, int width) {
		if(isActive){
			for(Key a:input.inputKeys){
				if(a.isTapped()){
					text.add(a.getChars());
				}
			}
			if(input.backspace.isTapped() && !text.isEmpty()){
				text.removeLast();
			}
		}
		
		if(input.LMBTapped()){
			if(isInBox(input.mouseX, input.mouseY)){
				isActive = true;
			}else{
				isActive = false;
			}
		}
		
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
	}
	
	public boolean isInBox(int x, int y){
		if(x > xPos + EMPTY_SPACE && x < xPos +EMPTY_SPACE + width && y > yPos + EMPTY_SPACE && y < yPos + EMPTY_SPACE + height) return true;
		return false;
	}
	
	public String getOutput(){
		String uitput = "";
		for(String s:text){
			uitput += s;
		}
		return uitput;
	}
	
	public void clear(){
		text.clear();
	}
	
	public void setText(String text){
		char[] tekst = text.toCharArray();
		for(char c: tekst) this.text.add(Character.toString(c));
	}
}
