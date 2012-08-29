package walnoot.rtsgame.menubar;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.popups.screenpopup.ScreenPopup;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupButton;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupTextField;
import walnoot.rtsgame.popups.screenpopup.TextInput;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.SPGameScreen;

public class MenuBar {
	public InputHandler input;
	public static int  UITLOOP = 5, EXTRA_WIDTH = 30,EXTRA_HEIGHT = 30;
	public int X_POS_FROM_RIGHT, Y_POS_FROM_BOTTOM, WIDTH_BUTTON, HEIGHT_BUTTON;
	public GameScreen screen;
	public LinkedList <Button> buttons = new LinkedList<Button>();
	int xPosOnScreen = 0, yPosOnScreen = 0;
	private MenuBarPopup popup;
	public boolean showPopup = false;
	public int corner, width, height;
	/**
	 * @param input
	 * @param gameScreen
	 * @param X_POS_FROM_RIGHT
	 * @param Y_POS_FROM_BOTTOM
	 * @param WIDTH_BUTTON
	 * @param HEIGHT_BUTTON
	 * @param corner	top left withe te clock 1 , 2 , 3 , 4
	 */
	
	public MenuBar(InputHandler input, GameScreen gameScreen, int X_POS_FROM_RIGHT, int Y_POS_FROM_BOTTOM, int WIDTH_BUTTON, int HEIGHT_BUTTON, int corner) {
		this.input = input;
		this.screen = gameScreen;
		this.corner = corner;
		width = UITLOOP + X_POS_FROM_RIGHT;
		height = UITLOOP + HEIGHT_BUTTON + Y_POS_FROM_BOTTOM;
		this.X_POS_FROM_RIGHT = X_POS_FROM_RIGHT;
		this.WIDTH_BUTTON = WIDTH_BUTTON;
		this.HEIGHT_BUTTON = HEIGHT_BUTTON;
		this.Y_POS_FROM_BOTTOM = Y_POS_FROM_BOTTOM;
	}

	public boolean isInBar(int x, int y){
		if(x > xPosOnScreen && x < xPosOnScreen + width && y > yPosOnScreen && y < yPosOnScreen + height) return true;
		else if(showPopup){
			if(popup.isInPopup(x, y)) return true;
		}
		return false;
	}
	
	public void addButton(Button b){
		buttons.add(b);
		width += WIDTH_BUTTON;
	}
	
	public void showPopup(){
		showPopup = true;
	}
	
	public void hidePopup(){
		popup = null;
		showPopup = false;
	}
	
	public void render (Graphics g , int screenWidth, int screenHeight){
		g.setColor(Color.BLACK);
		if(corner == 3){
			g.fillRoundRect(xPosOnScreen, yPosOnScreen, buttons.size()*WIDTH_BUTTON + EXTRA_WIDTH, HEIGHT_BUTTON+EXTRA_HEIGHT, 7, 7);
			for(int i = 0; i < buttons.size(); i++){
				buttons.get(i).render(g, xPosOnScreen + UITLOOP + i*WIDTH_BUTTON , yPosOnScreen + UITLOOP);
			}
		}else if(corner == 1){
			g.fillRoundRect(xPosOnScreen - EXTRA_WIDTH, yPosOnScreen - EXTRA_HEIGHT, width + EXTRA_WIDTH, EXTRA_HEIGHT + height,7,7);
			for(int i = 0; i < buttons.size(); i++){
				buttons.get(i).render(g, xPosOnScreen + X_POS_FROM_RIGHT + i*WIDTH_BUTTON , yPosOnScreen + Y_POS_FROM_BOTTOM);
			}
		}
		if(popup!= null && showPopup){
			popup.render(g);
		}
	}
	
	public void update(int screenWidth, int screenHeight){
		if(corner == 3){
			yPosOnScreen = screenHeight - UITLOOP - HEIGHT_BUTTON - Y_POS_FROM_BOTTOM;
			xPosOnScreen = screenWidth - UITLOOP - X_POS_FROM_RIGHT - buttons.size()*WIDTH_BUTTON;
			
			if(popup != null) popup.update(xPosOnScreen + width - X_POS_FROM_RIGHT, yPosOnScreen + UITLOOP);
			
		}else if(corner == 1){
			xPosOnScreen = 0;
			yPosOnScreen = 0;
			
			if(popup!= null) {
				popup.update(X_POS_FROM_RIGHT, height);
			}
			
		}
		
		for(Button b: buttons){
			if(b instanceof MenubarTextField)((MenubarTextField) b).update();
		}
		
		if(input.LMBTapped() ){
			if(isOnlyInBar(input.getMouseX(), input.getMouseY())){
				int indexSelected = (input.getMouseX() - UITLOOP - xPosOnScreen) / WIDTH_BUTTON;
				if(indexSelected < buttons.size())buttons.get(indexSelected).onLeftClick();		
			}else if(isInBar(input.getMouseX(), input.getMouseY())){
				popup.onLeftClick(input.getMouseX(),input.getMouseY());
			}
		}
		
		
		
	}
	
	private boolean isOnlyInBar(int x, int y) {
		if(x > xPosOnScreen && x < xPosOnScreen + width && y > yPosOnScreen && y < yPosOnScreen + height)return true;
		return false;
	}

	public void setMenuBarPopup(MenuBarPopup p){
		popup = p;
	}
	
	public int getWidth(){
		return buttons.size()*WIDTH_BUTTON;
	}
	
	public int getHeight(){
		return HEIGHT_BUTTON;
	}
	
}
