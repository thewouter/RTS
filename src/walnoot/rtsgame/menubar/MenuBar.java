package walnoot.rtsgame.menubar;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.popups.screenpopup.ScreenPopup;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupButton;
import walnoot.rtsgame.popups.screenpopup.TextInput;
import walnoot.rtsgame.screen.GameScreen;

public class MenuBar {
	public InputHandler input;
	public static int X_POS_FROM_RIGHT = 5, Y_POS_FROM_BOTTOM = 5, WIDTH_BUTTON = 16, HEIGHT_BUTTON = 16, UITLOOP = 5, EXTRA_WIDTH = 30,EXTRA_HEIGHT = 30;
	public GameScreen screen;
	LinkedList <Button> buttons = new LinkedList<Button>();
	int xPosOnScreen = 0, yPosOnScreen = 0;
	private MenuBarPopup popup;
	public boolean showPopup = false;
	
	public MenuBar(InputHandler input, GameScreen screen) {
		this.input = input;
		this.screen = screen;
		

		
		
		addButton(new Button(Images.buttons[0][0], this) {
			public void onLeftClick() {
				MenuBarPopup popup = new MenuBarPopup(xPosOnScreen + UITLOOP + (buttons.indexOf(this) + 1)*WIDTH_BUTTON, yPosOnScreen + UITLOOP, buttons.indexOf(this));
				
				popup.addButton(new MenuBarPopupButton(Images.buttons[3][0]) {
					public void onLeftClick() {
						System.out.println("test!");
						
					}
				});
				setMenuBarPopup(popup);
				showPopup = !showPopup;
				
			}
		});
		
		addButton(new Button(Images.buttons[0][1], this){
			public void onLeftClick() {
				MenuBarPopup popup = new MenuBarPopup(xPosOnScreen + UITLOOP + (buttons.indexOf(this) + 1)*WIDTH_BUTTON, yPosOnScreen + UITLOOP, buttons.indexOf(this));
				popup.addButton(new MenuBarPopupButton(Images.buttons[1][0]) {
					public void onLeftClick() {
						ScreenPopup popup = new ScreenPopup((bar.screen.getWidth() - 100)/2, (bar.screen.getHeight() - 100)/2, 100, 100, bar.screen);
						popup.addPart(new TextInput(popup, bar.input));
						popup.addPart(new ScreenPopupButton("load", popup, bar.input){
							public void onLeftClick() {
								bar.screen.load(owner.getTextInput().getOutput());
								bar.screen.setPopup(null);
							}
						});
						popup.addPart(new ScreenPopupButton("cancel", popup, bar.input) {
							public void onLeftClick() {
								bar.screen.setPopup(null);
								System.out.println("test");
							}
						});
						bar.screen.setPopup(popup);
						
					}
					
				});
				popup.addButton(new MenuBarPopupButton(Images.buttons[2][0]) {
					public void onLeftClick() {
						ScreenPopup popup = new ScreenPopup((bar.screen.getWidth() - 100)/2, (bar.screen.getHeight() - 100)/2, 100, 100, bar.screen);
						popup.addPart(new TextInput(popup, bar.input));
						popup.addPart(new ScreenPopupButton("Save", popup, bar.input){
							public void onLeftClick() {
								bar.screen.save(owner.getTextInput().getOutput());
								bar.screen.setPopup(null);
							}
						});
						popup.addPart(new ScreenPopupButton("cancel", popup, bar.input) {
							public void onLeftClick() {
								bar.screen.setPopup(null);
							}
						});
						bar.screen.setPopup(popup);
					}
				});
				setMenuBarPopup(popup);
				showPopup = !showPopup;
			}
		});
		
		addButton(new Button(Images.buttons[3][0], this) {
			public void onLeftClick() {
				this.bar.screen.component.setTitleScreen();
			}
		});
	}

	public boolean isInBar(int x, int y){
		if(x > xPosOnScreen && y > yPosOnScreen) return true;
		else if(showPopup){
			if(popup.isInPopup(x, y)) return true;
		}
		return false;
	}
	
	public void addButton(Button b){
		buttons.add(b);
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
		g.fillRoundRect(xPosOnScreen, yPosOnScreen, buttons.size()*WIDTH_BUTTON + EXTRA_WIDTH, HEIGHT_BUTTON+EXTRA_HEIGHT, 7, 7);
		for(int i = 0; i < buttons.size(); i++){
			buttons.get(i).render(g, xPosOnScreen + UITLOOP + i*WIDTH_BUTTON , yPosOnScreen + UITLOOP);
		}
		if(popup!= null && showPopup)popup.render(g);
	}
	
	public void update(int screenWidth, int screenHeight){
		yPosOnScreen = screenHeight - UITLOOP - HEIGHT_BUTTON - Y_POS_FROM_BOTTOM;
		xPosOnScreen = screenWidth - UITLOOP - X_POS_FROM_RIGHT - buttons.size()*WIDTH_BUTTON;
		
		if(input.LMBTapped() ){
			if(isOnlyInBar(input.getMouseX(), input.getMouseY())){
				int indexSelected = (int) ((input.getMouseX()- (screen.getWidth() - X_POS_FROM_RIGHT - buttons.size()*WIDTH_BUTTON)) / WIDTH_BUTTON);
				if(indexSelected < buttons.size())buttons.get(indexSelected).onLeftClick();		
			}else if(isInBar(input.getMouseX(), input.getMouseY())){
				popup.onLeftClick(input.getMouseX(),input.getMouseY());
			}
		}
		
		if(popup != null) popup.update(xPosOnScreen + UITLOOP + (popup.index + 1)*WIDTH_BUTTON, yPosOnScreen + UITLOOP);
		
		
	}
	
	private boolean isOnlyInBar(int x, int y) {
		if(x > xPosOnScreen )if(y > yPosOnScreen)return true;
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
