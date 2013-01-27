package walnoot.rtsgame.screen.menus;

import java.awt.Graphics;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.popups.screenpopup.ScreenPopup;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupButton;
import walnoot.rtsgame.popups.screenpopup.TextInput;
import walnoot.rtsgame.screen.MPGameScreen;
import walnoot.rtsgame.screen.Screen;
import walnoot.rtsgame.screen.TitleScreen;

public class MainMenu extends MenuScreen {
	private Screen title;
	private float buttonTransparancy;
	
	private MenuButton startGame = new MenuButton("Start Game", 0, 100, -1, -1, this);
	private MenuButton newSPGame = new MenuButton("New Game", 0, 130, -1, -1, this);
	private MenuButton newMPGame = new MenuButton("Join MP Game", 0, 160, -1, -1, this);
	private MenuButton hostMPGame = new MenuButton("Host MP Game", 0, 190, -1, -1, this);
	private MenuButton exitGame = new MenuButton("Exit Game", 0, 220, -1, -1, this);
	
	public MainMenu(RTSComponent component, TitleScreen title, InputHandler input){
		super(component, input);
		this.title = title;
		
		ScreenPopup popup = new ScreenPopup((getWidth()-100)/2, (getHeight() - 100)/2, 100, 100, this);
		TextInput userNameInput = new TextInput(popup,input);
		userNameInput.setText("wouter");
		popup.addPart(userNameInput);
		popup.addPart(new ScreenPopupButton("OK", popup, input) {
			
			public void onLeftClick() {
				String username = owner.getTextInput(1).getOutput();
				if(!owner.screen.component.isMember(username)) owner.screen.component.stop();
				else setPopup(null);
			}
		});
		setPopup(popup);
	}
	
	public void render(Graphics g){
		title.render(g);
		
		super.makeTransparant(g, buttonTransparancy);
		
		super.render(g);
		
		
	}
	
	protected MenuButton[] getButtons(){
		MenuButton[] result = {startGame, newSPGame, newMPGame, exitGame, hostMPGame};
		
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
		else if(menuButton.equals(newSPGame)) component.setGameScreen(true);
		else if(menuButton.equals(newMPGame)){
			ScreenPopup popup = new ScreenPopup((component.getWidth() / 4 )- 50, (component.getHeight() / 4) - 50, 100, 100, title);
			TextInput IP = new TextInput(popup, input);
			IP.setText("localhost");
			popup.addPart(IP);
			TextInput port = new TextInput(popup, input);
			port.setText("1995");
			popup.addPart(port);
			popup.addPart(new ScreenPopupButton("OK", popup, input) {
				public void onLeftClick() {
					String ip = owner.getTextInput(1).getOutput();
					String port = owner.getTextInput(2).getOutput();
					try{ 
						owner.screen.component.setScreen(new MPGameScreen(owner.screen.component, input, Integer.parseInt(port), ip));
					}catch(Exception e){
						owner.getTextInput(2).clear();
						owner.getTextInput(2).setText("invalid");
						System.out.println(e);
					}
				}
			});
			setPopup(popup);
		}
		else if(menuButton.equals(hostMPGame)) component.setHostedGame(1995);
	}
}
