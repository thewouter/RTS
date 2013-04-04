package walnoot.rtsgame.screen;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.popups.screenpopup.ScreenPopup;
import walnoot.rtsgame.rest.RTSFont;

/**
 * Een Screen staat voor een deel van het spel, zoals het hoofdmenu, of game
 * scherm
 */
public abstract class Screen {
	public RTSComponent component;
	public static RTSFont font = new RTSFont(Images.font);
	public InputHandler input;
	public ScreenPopup popup = null;
	
	public Screen(RTSComponent component, InputHandler input){
		this.component = component;
		this.input = input;
	}
	
	public void setScreen(Screen screen){
		component.setScreen(screen);
	}
	
	public void render(Graphics g){
		if(popup!= null){
			popup.render(g);
		}
	}
	
	public void update(){
		if(input.escape.isTapped()) {
			if(popup == null)component.setTitleScreen();
			else if(!popup.isForced()) setPopup(null);
		}
		if(popup!= null) {
			popup.update(input.getMouseX(), input.getMouseY());
			input.update();
		}
	}
	
	/** @param transparancy hoe transparant, max 1.0, min 0.0 */
	public void makeTransparant(Graphics g, float transparancy){
		Graphics2D g2d = (Graphics2D) g;
		Composite alphaComp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparancy);
		g2d.setComposite(alphaComp);
	}
	
	public Point getMouseLocation(){
		return new Point(input.getMouseX(), input.getMouseY());
	}
	
	public int getWidth(){
		return component.getWidth() / RTSComponent.SCALE;
	}
	
	public int getHeight(){
		return component.getHeight() / RTSComponent.SCALE;
	}
	
	public void setPopup(ScreenPopup popup){
		this.popup = popup;
	}
	
	public void quit(){}
}
