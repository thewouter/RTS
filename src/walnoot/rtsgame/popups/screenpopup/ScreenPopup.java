package walnoot.rtsgame.popups.screenpopup;

import java.awt.Graphics;
import java.util.LinkedList;

import walnoot.rtsgame.popups.Popup;
import walnoot.rtsgame.screen.Screen;

public class ScreenPopup extends Popup{
	int xPos,yPos, width, height;
	private static int EMPTY_SPACE = 10;
	LinkedList<ScreenPopupPart> parts = new LinkedList<ScreenPopupPart>();
	public Screen screen;
	
	public ScreenPopup(int xPos, int yPos, int width, int height, Screen title){
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.screen = title;
	}
	

	public void render(Graphics g) {
		drawBox(g,width, height, xPos, yPos);
		for(ScreenPopupPart p:parts){
			p.render(g);
		}
	}

	public void update(int mouseX, int mouseY) {
		if(isInPopup(mouseX, mouseY));
		int totalHeight = 0;
		for(int i = 0; i < parts.size(); i++){
			parts.get(i).update((xPos * 2 + width)/2- (width - 2 * EMPTY_SPACE)/2, yPos + EMPTY_SPACE + i * EMPTY_SPACE + totalHeight, width - 2 * EMPTY_SPACE);
			totalHeight += parts.get(i).height;
		}
		if(this.height < (parts.size() + 1) * EMPTY_SPACE + totalHeight){
			this.height = (parts.size() + 1) * EMPTY_SPACE + totalHeight;
		}
		
	}

	public void onLeftClick(int mouseX, int mouseY) {
		
	}

	public boolean isInPopup(int x, int y) {
		if(x > xPos && x < xPos + width && y > yPos && y < yPos + height){
			return true;
		}
		return false;
	}
	
	public ScreenPopupPart getPart(int index){
		return parts.get(index);
	}
	
	public void addPart(ScreenPopupPart p){
		parts.add(p);
	}
	
	public TextInput getTextInput(int index){
		int counter = 0;
		for(ScreenPopupPart p: parts){
			if(p instanceof TextInput){
				counter++;
				if(index == counter)return (TextInput) p;
			}
		}
		return null;
	}
}
