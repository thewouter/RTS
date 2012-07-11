package walnoot.rtsgame.menubar;

import java.awt.Graphics;
import java.util.LinkedList;

import walnoot.rtsgame.popups.Popup;

public class MenuBarPopup extends Popup {
	LinkedList<MenuBarPopupButton> buttons = new LinkedList<MenuBarPopupButton>();
	private int xPos, yPos, width, height;
	public int index;
	/**
	 * @param xPos x position of the button that created this popup
	 * @param yPos y position of the button that created this popup
	 */
	
	public static int WIDTH_BUTTON = 16, HEIGHT_BUTTON = 16, EMPTY_SPACE = 20, BUTTONS_PER_ROW = 5;
	
	public MenuBarPopup(int xPos, int yPos, int index){
		this.xPos = xPos;
		this.yPos = yPos;
		this.index = index;
	}
	
	public void render(Graphics g){
		int screenX = xPos - width;
		int screenY = yPos - height;
		
		drawBox(g,width	, height, screenX, screenY);
		
		for(int i = 0; i < buttons.size(); i++){
			int x = i % 5;
			int y  = i/5;
			buttons.get(i).render(g,xPos - width + EMPTY_SPACE/2 + x * WIDTH_BUTTON, yPos - height + EMPTY_SPACE/2 + y *HEIGHT_BUTTON);
		}
		
	}
	
	public void addButton(MenuBarPopupButton b){
		if(b != null) buttons.add(b);
	}
	
	public boolean isInPopup(int x, int y){
		if(x > xPos - width && x < xPos && y > yPos - height && y < yPos)return true;
		return false;
	}
	
	public void onLeftClick(int mouseX, int mouseY){
		int x = (mouseX - xPos + width - EMPTY_SPACE/2) / WIDTH_BUTTON + 1;
		int y = (mouseY - yPos + height -EMPTY_SPACE/2) / HEIGHT_BUTTON + 1;
		try{
			getButton(x, y).onLeftClick();
		}catch(Exception e){
			System.out.println("mislukt  ;(");
		}
	}
	
	public void update(int xPos, int yPos){
		this.xPos = xPos;
		this.yPos = yPos;
		height =((int) Math.floor(buttons.size()/BUTTONS_PER_ROW)+1) * HEIGHT_BUTTON + EMPTY_SPACE;
		width = BUTTONS_PER_ROW * WIDTH_BUTTON +EMPTY_SPACE;
	}
	
	public MenuBarPopupButton getButton(int x, int y){
		if(5*(y-1)+x <= buttons.size()) return buttons.get(5*(y-1)+x-1);
		return null;
	}
	
	public void update(int translationX, int translationY, int mouseX, int mouseY) {}
}
