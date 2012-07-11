package walnoot.rtsgame.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.Util;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.Save;
import walnoot.rtsgame.map.entities.DeerEntity;
import walnoot.rtsgame.menubar.MenuBar;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.entities.PlayerEntity;
import walnoot.rtsgame.map.entities.SheepEntity;
import walnoot.rtsgame.map.structures.CampFireStructure;
import walnoot.rtsgame.map.structures.TentStructure;
import walnoot.rtsgame.map.structures.TreeStructure;
import walnoot.rtsgame.popups.entitypopup.EntityPopup;
import walnoot.rtsgame.popups.screenpopup.ScreenPopup;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupButton;

public class GameScreen extends Screen {
	private Map map;
	private int translationX, translationY;
	private EntityPopup entityPopup = null;
	
	private Entity selectedEntity;
	private Entity targetEntity; //the Entity the camera will go to
	
	private MenuBar bar;
	
	private ScreenPopup popup = null;
	
	boolean pause = false;
	
	
	
	public GameScreen(RTSComponent component, InputHandler input){
		super(component, input);
		
		map = new Map(256,false);
		
		int goodYPos;
		
		for(int i = 4;; i++){
			if(!map.getTile(4, i).isSolid()){
				selectedEntity = new PlayerEntity(map, 4, i);
				goodYPos = i;
				break;
			}
		}
		
		targetEntity = selectedEntity;
		map.addEntity(selectedEntity);
		
		bar = new MenuBar(input, this);
		
		
		map.addEntity(new DeerEntity(map, 4, goodYPos+1)); //voor de test, later weghalen
		map.addEntity(new SheepEntity(map, 4, goodYPos+2)); //voor de test, later weghalen
		map.addEntity(new TentStructure(map, 4, goodYPos + 3)); //voor de test, later weghalen
		map.addEntity(new CampFireStructure(map, 4, goodYPos + 5)); //voor de test, later weghalen
		map.addEntity(new TreeStructure(map, 4, goodYPos + 7)); //voor de test, later weghalen
		
		
		translationX = -selectedEntity.getScreenX();
		translationY = -selectedEntity.getScreenY();
		
	}

	public void render(Graphics g){
		Point translation = new Point((int) translationX, (int) translationY);
		
		map.render(g, translation, new Dimension(getWidth(), getHeight()), getWidth(), getHeight());
		bar.render(g, getWidth(), getHeight());
		
		g.translate(translation.x, translation.y);
		
		if(entityPopup != null) entityPopup.render(g);
		g.translate(-translation.x, -translation.y);
		int x = getMapX();
		int y = getMapY();
		
		g.setColor(Color.WHITE);
		
		font.drawBoldLine(g, x + ":" + y, 20, 20, Color.BLACK);
		
		if(selectedEntity != null){
			font.drawBoldLine(g, selectedEntity.getName(), 20, getHeight() - 40, Color.BLACK);
			font.drawBoldLine(g, "Health: " + selectedEntity.getHealth(), 20, getHeight() - 30, Color.BLACK);
		}
		
		if(popup!= null){
			popup.render(g);
		}
		
		if(input.escape.isTapped()){
			component.setTitleScreen();
		}
	}
	
	public Entity getSelectedEntity(){
		return selectedEntity;
	}
	
	public void save(){
		Save.save(map, "save1");
	}
	
	
	
	public void save(String fileName){
		Save.save(map, fileName);
	}
	
	public void load(){
		load("save1");
	}
	
	public void load(String nameFile){
		Map map = Save.load(nameFile);
		if(map != null)this.map = map;
	}
	
	public void update(){

		if(!pause){
			if(input.up.isPressed()) translationY += 5;
			if(input.down.isPressed()) translationY -= 5;
			if(input.left.isPressed()) translationX += 5;
			if(input.right.isPressed()) translationX -= 5;
	
			map.update((int) Math.floor(translationX), (int) Math.floor(translationY));
			bar.update(getWidth(), getHeight());
		
			if(input.space.isPressed()) targetEntity = selectedEntity;
		
			if(targetEntity != null){
				int dx = targetEntity.getScreenX() + (translationX - getWidth() / 2);
				int dy = targetEntity.getScreenY() + (translationY - getHeight() / 2);
				
				translationX -= dx / 10;
				translationY -= dy / 10;
			
				if(Util.abs(dx) < 10 && Util.abs(dy) < 10) targetEntity = null;
			}
		
			if(input.LMBTapped()){
				if(entityPopup != null){
					entityPopup.onLeftClick(input.getMouseX(), input.getMouseY());
				}
				if(bar.isInBar(input.getMouseX(), input.getMouseY())){
				
				}else if(entityPopup != null && !entityPopup.isInPopup(input.getMouseX(), input.getMouseY()) ){
					selectedEntity = map.getEntity(getMapX(), getMapY());
				}else if( entityPopup == null ){
					selectedEntity = map.getEntity(getMapX(), getMapY());
				}
			}
			
			if(entityPopup != null){
				entityPopup.update(translationX,translationY, input.getMouseX(), input.getMouseY());
				if(entityPopup.getOwner() != selectedEntity) entityPopup = null;
			}
			if(input.RMBTapped()){
				
				Entity rightClicked = map.getEntity(getMapX(), getMapY()); //the Entity that is right clicked, if any
				
				boolean canMove = true;
				if(rightClicked != null && selectedEntity != null) canMove = selectedEntity.onRightClick(rightClicked, this, input);
				
				if(canMove){
					if(selectedEntity instanceof MovingEntity){
						((MovingEntity) selectedEntity).moveTo(new Point(getMapX(), getMapY()));
						entityPopup = null;
					}
				}
			}
			if(!map.entities.contains(selectedEntity)) selectedEntity = null;
		}
		if (input.p.isTapped()&& popup == null){
			if(!pause)pause();
			else dePause();
		}
		if(popup != null){
			popup.update(translationX, translationY, input.getMouseX(), input.getMouseY());
		}
	}
	
	public void setEntityPopup(EntityPopup popup){
		this.entityPopup = popup;
	}

	public void removePopup(){
		entityPopup = null;
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
	
	public void pause(){
		pause = true;
		popup = new ScreenPopup((getWidth()-84)/2, (getHeight() - 20)/2, 84, 20, this);
		popup.addPart(new ScreenPopupButton("play", popup, input) {
			public void onLeftClick() {
				dePause();			
			}
		});
	}
	
	public void dePause(){
		popup = null;
		pause = false;
	}
	
	public void setPopup(ScreenPopup popup){
		this.popup = popup;
		if(popup == null){
			pause = false;
		}else pause = true;
	}
}
