package walnoot.rtsgame.popups.entitypopup;

import java.awt.Graphics;

import walnoot.rtsgame.popups.Popup;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.map.entities.Entity;

public abstract class EntityPopup extends Popup{
	public static final int EMPTY_SPACE = 32;
	public final Entity owner;
	public GameScreen screen;
	
	/**
	 * @param owner The owner of this PopUp
	 */
	public EntityPopup(Entity owner, GameScreen screen){
		this.owner = owner;
		this.screen = screen;
	}
	
	public abstract void render(Graphics g);
	public abstract void update(int translationX, int translationY, int mouseX, int mouseY);
	public abstract void onLeftClick(int mouseX, int mouseY);
	public abstract boolean isInPopup(int x, int y);
	
	/**
	 * @param g
	 * @param width width in pixels
	 * @param height height in pixels
	 * @param screenX
	 * @param screenY
	 */
	
	
	protected void drawBox(Graphics g, int width, int height){
		drawBox(g, width, height, getScreenX(), getScreenY());
	}
	
	public Entity getOwner(){
		return owner;
	}
	
	protected int getScreenX(){
		return owner.getScreenX();
	}
	
	protected int getScreenY(){
		return owner.getScreenY();
	}
}
