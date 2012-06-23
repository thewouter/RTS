package walnoot.rtsgame.screen.menus;

import java.awt.Graphics;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.screen.Screen;

public class PauseScreen extends MenuScreen {
	private final Screen overlappedScreen;
	
	private MenuButton resumeGame = new MenuButton("Resume Game", 0, 20, -1, -1, this);
	
	public PauseScreen(RTSComponent component, Screen overlappedScreen, InputHandler input){
		super(component, input);
		this.overlappedScreen = overlappedScreen;
	}
	
	public void render(Graphics g){
		super.render(g);
	}
	
	protected MenuButton[] getButtons(){
		MenuButton[] result = {};
		
		return result;
	}
	
	public void buttonPressed(MenuButton menuButton){
		if(menuButton == resumeGame) component.setScreen(overlappedScreen);
	}
}
