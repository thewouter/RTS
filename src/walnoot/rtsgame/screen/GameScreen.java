package walnoot.rtsgame.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.Util;
import walnoot.rtsgame.menubar.Button;
import walnoot.rtsgame.menubar.MenuBarPopup;
import walnoot.rtsgame.menubar.MenuBarPopupButton;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.DeerEntity;
import walnoot.rtsgame.menubar.MenuBar;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.entities.PlayerEntity;
import walnoot.rtsgame.map.entities.SheepEntity;
import walnoot.rtsgame.map.structures.CampFireStructure;
import walnoot.rtsgame.map.structures.TentStructure;
import walnoot.rtsgame.map.tribes.Tribe;
import walnoot.rtsgame.popups.Popup;
import walnoot.rtsgame.popups.TextPopup;

public class GameScreen extends Screen {
	private Map map;
	private int translationX, translationY;
	private Popup popup = null;
	
	private Entity selectedEntity;
	private Entity targetEntity; //the Entity the camera will go to
	
	private Tribe tribe;
	private MenuBar bar;
	
	public GameScreen(RTSComponent component, InputHandler input){
		super(component, input);
		
		map = new Map(256);
		
		int goodYPos;
		
		for(int i = 4;; i++){
			if(!map.getTile(4, i).isSolid()){
				selectedEntity = new PlayerEntity(map, 4, i, null);
				goodYPos = i;
				break;
			}
		}
		targetEntity = selectedEntity;
		map.addEntity(selectedEntity);
		
		bar = new MenuBar(input, this);
		
		tribe = new Tribe("My Tribe", Color.BLUE);
		selectedEntity.setTribe(tribe);
		
		map.addEntity(new DeerEntity(map, 4, goodYPos+1)); //voor de test, later weghalen
		map.addEntity(new SheepEntity(map, 4, goodYPos+2)); //voor de test, later weghalen
		map.addEntity(new TentStructure(map, 4, goodYPos + 10, tribe)); //voor de test, later weghalen
		map.addEntity(new CampFireStructure(map, 5, goodYPos + 12, tribe)); //voor de test, later weghalen
		
		translationX = -selectedEntity.getScreenX();
		translationY = -selectedEntity.getScreenY();
	}

	public void render(Graphics g){
		Point translation = new Point((int) translationX, (int) translationY);
		
		map.render(g, translation, new Dimension(getWidth(), getHeight()));
		bar.render(g, getWidth(), getHeight());
		
		g.translate(translation.x, translation.y);
		if(popup != null) popup.render(g);
		g.translate(-translation.x, -translation.y);
		int x = getMapX();
		int y = getMapY();
		
		g.setColor(Color.WHITE);
		font.drawBoldLine(g, x + ":" + y, 20, 20, Color.BLACK);
		
		if(selectedEntity != null){
			font.drawBoldLine(g, selectedEntity.getName(), 20, getHeight() - 40, Color.BLACK);
			font.drawBoldLine(g, "Health: " + selectedEntity.getHealth(), 20, getHeight() - 30, Color.BLACK);
		}
	}
	
	public Entity getSelectedEntity(){
		return selectedEntity;
	}
	
	public void update(){
		map.update((int) Math.floor(translationX), (int) Math.floor(translationY));
		bar.update(getWidth(), getHeight());
		
		if(input.up.isPressed()) translationY += 5;
		if(input.down.isPressed()) translationY -= 5;
		if(input.left.isPressed()) translationX += 5;
		if(input.right.isPressed()) translationX -= 5;
		
		if(input.space.isPressed()) targetEntity = selectedEntity;
		
		if(targetEntity != null){
			int dx = targetEntity.getScreenX() + (translationX - getWidth() / 2);
			int dy = targetEntity.getScreenY() + (translationY - getHeight() / 2);
			
			translationX -= dx / 10;
			translationY -= dy / 10;
			
			if(Util.abs(dx) < 10 && Util.abs(dy) < 10) targetEntity = null;
		}
		
		if(input.LMBTapped()){
			if(popup != null){
				popup.onLeftClick(input.getMouseX(), input.getMouseY());
			}
			if(bar.isInBar(input.getMouseX(), input.getMouseY())){
				
				
			}else if(popup != null && !popup.isInPopup(input.getMouseX(), input.getMouseY()) ){
				selectedEntity = map.getEntity(getMapX(), getMapY());
			}else if( popup == null ){
				selectedEntity = map.getEntity(getMapX(), getMapY());
			}
		}
		
		if(popup != null){
			popup.update(translationX,translationY, input.getMouseX(), input.getMouseY());
			if(popup.getOwner() != selectedEntity) popup = null;
		}
		if(input.RMBTapped()){
			
			Entity rightClicked = map.getEntity(getMapX(), getMapY()); //the Entity that is right clicked, if any
			
			boolean canMove = true;
			if(rightClicked != null && selectedEntity != null) canMove = selectedEntity.onRightClick(rightClicked, this, input);
			
			if(canMove){
				if(selectedEntity instanceof MovingEntity){
					((MovingEntity) selectedEntity).moveTo(new Point(getMapX(), getMapY()));
					popup = null;
				}
			}
		}
		if(!map.entities.contains(selectedEntity)) selectedEntity = null;
	}
	
	public void setPopup(Popup popup){
		this.popup = popup;
	}

	public void removePopup(){
		popup = null;
	}
	
	private int getMapX(){
		return Util.getMapX(input.getMouseX() - translationX, input.getMouseY() - translationY);
	}
	
	private int getMapY(){
		return Util.getMapY(input.getMouseX() - translationX, input.getMouseY() - translationY);
	}
	
	public void deselectEntity(){
		selectedEntity = null;
	}
}
