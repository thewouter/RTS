package walnoot.rtsgame.screen.menus;

import java.awt.Graphics;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.screen.Screen;
import walnoot.rtsgame.screen.TitleScreen;

public class MainMenu extends MenuScreen {
	private Screen title;
	private float buttonTransparancy;
	
	private MenuButton startGame = new MenuButton("Start Game", 0, 100, -1, -1, this);
	private MenuButton exitGame = new MenuButton("Exit Game", 0, 160, -1, -1, this);
	private MenuButton newGame = new MenuButton("New Game", 0, 130, -1, -1, this);
	
	public MainMenu(RTSComponent component, TitleScreen title, InputHandler input){
		super(component, input);
		this.title = title;
	}
	
	public void render(Graphics g){
		title.render(g);
		
		super.makeTransparant(g, buttonTransparancy);
		super.render(g);
		
	}
	
	protected MenuButton[] getButtons(){
		MenuButton[] result = {startGame, newGame, exitGame};
		
		return result;
	}
	
	public void update(){
		super.update();
		
		if(buttonTransparancy < 1.0f){
			buttonTransparancy += (float) RTSComponent.MS_PER_TICK / 1000;
		}
		if(buttonTransparancy > 1.0f){
			buttonTransparancy = 1.0f;
		}
	}
	
	public void buttonPressed(MenuButton menuButton){
		if(menuButton.equals(startGame))super.component.setGameScreen(false);
		else if(menuButton.equals(exitGame)) component.stop();
		else if(menuButton.equals(newGame)) component.setGameScreen(true);
	}
}
