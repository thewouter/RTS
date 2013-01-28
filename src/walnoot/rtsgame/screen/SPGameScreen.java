package walnoot.rtsgame.screen;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.Save;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.entities.players.Shield;
import walnoot.rtsgame.map.entities.players.Soldier;
import walnoot.rtsgame.map.entities.players.professions.Founder;
import walnoot.rtsgame.map.structures.Structure;
import walnoot.rtsgame.map.structures.nonnatural.warrelated.Barracks;
import walnoot.rtsgame.map.structures.nonnatural.warrelated.DefenseTower;
import walnoot.rtsgame.map.structures.nonnatural.warrelated.WoodenWall;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.menubar.Button;
import walnoot.rtsgame.menubar.MenuBarPopupButton;
import walnoot.rtsgame.rest.MousePointer;
import walnoot.rtsgame.rest.Sound;
import walnoot.rtsgame.rest.Util;

public class SPGameScreen extends GameScreen {
	
	private boolean isReady = false;
	private Button levelUpButton;
	
	public SPGameScreen(RTSComponent component, InputHandler input){
		super(component, input);
		
		map = new Map(256, this);
		
		inventory.gold = 500;
		inventory.wood = 70;
		inventory.stone = 50;
		inventory.vegetables = 10;
		inventory.meat = 10;
		
		int goodYPos;
		
		
		
		levelUpButton = new Button(Images.buttons[2][1], statusBar) {
			public void onLeftClick() {
				levelUp();
			}
		};
		
		//targetEntity = selectedEntities.getFirst();
		PlayerEntity p = new PlayerEntity(map, this, 10, 10, null);
		p.setProfession(new Founder(p));
		map.addEntity(p);
		
		//translationX = -selectedEntities.getFirst().getScreenX();
		//translationY = -selectedEntities.getFirst().getScreenY();
		
		bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[0][7], this) {
			
			public void onLeftClick() {
				pointer = new MousePointer(map, this.screen.input, this.screen) {
					public Entity toBuild(Direction face) {
						return null;
					}
					public void afterBuild(){
						for(Entity e:selectedEntities){
							map.removeEntity(e);
						}
					}
				};
			}
			
			public String getName() {
				return "remove";
			}
		});
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
		Map map = Save.load(nameFile,this);
		if(map != null)this.map = map;
	}
	
	public void update(){
		if(!pause){
			if(input.up.isPressed()) translationY += 5;
			if(input.down.isPressed()) translationY -= 5;
			if(input.left.isPressed()) translationX += 5;
			if(input.right.isPressed()) translationX -= 5;
			
			if(pointer != null){
				pointer.update(); 
				if(bar.showPopup == false){
					pointer = null;
				}
			}
	
			map.update((int) Math.floor(translationX), (int) Math.floor(translationY), getWidth(), getHeight());
			
			bar.update(getWidth(), getHeight());
			
			statusBar.update(getWidth(), getHeight());
		
			if(input.space.isPressed() && selectedEntities != null && !selectedEntities.isEmpty()) targetEntity = selectedEntities.getFirst();
		
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
				}else selectedEntities.clear();
				
				if(bar.isInBar(input.getMouseX(), input.getMouseY())){
					
				}else if(entityPopup != null && !entityPopup.isInPopup(input.getMouseX(), input.getMouseY()) ){
					selectedEntities.clear();
					selectedEntities.addAll(map.getEntities(getMapX(), getMapY()));
				}else if( entityPopup == null ){
					selectedEntities.addAll(map.getEntities(getMapX(), getMapY()));
				}
			}
			
			if(input.wasDragging() && (popup == null || !popup.isInPopup(input.mouseX, input.mouseY))){
				int x1 = input.mouseXOnClick, y1 = input.mouseYOnClick, x2 = input.mouseX, y2 = input.mouseY;
				selectedEntities.clear();
				LinkedList<Entity> inRange = (map.getEntities(x1, y1 , x2, y2, new Dimension(translationX, translationY)));
				ArrayList<Entity> structures = new ArrayList<Entity>();
				for( Entity e:inRange){
					if(e instanceof MovingEntity){
						selectedEntities.add(e);
					}else if(e instanceof Structure){
						structures.add(e);
					}
				}
				if(selectedEntities.size() == 0){
					selectedEntities.addAll(structures);
				}
			}
			
			if(entityPopup != null){
				entityPopup.update(input.getMouseX(), input.getMouseY());
				if(!selectedEntities.contains(entityPopup.getOwner())) entityPopup = null;
			}
			
			if(input.RMBTapped()){
				
				Entity rightClicked = map.getEntity(getMapX(), getMapY()); //the Entity that is right clicked, if any
				boolean canMove = true;
				
				if(rightClicked != null && !selectedEntities.isEmpty()) {
					canMove = selectedEntities.getFirst().onRightClick(rightClicked, this, input);
				}
				
				if(canMove){
					if(!selectedEntities.isEmpty() && selectedEntities.getFirst() instanceof MovingEntity){
						if(((MovingEntity)selectedEntities.getFirst()).isMovable()){
							((MovingEntity) selectedEntities.getFirst()).moveTo(new Point(getMapX(), getMapY()));
							entityPopup = null;
						}
					}
				}
			}
			LinkedList<Entity> remove = new LinkedList<Entity>();
			for(Entity e: selectedEntities){
				if(!map.getEntities().contains(e)) remove.add(e);
			}
			
			
			selectedEntities.removeAll(remove);
			
			
			if(!isReady && isReadyForLevelUp()){
				statusBar.addButton(levelUpButton, 0);
				isReady = true;
			}
			
		}
		if (input.p.isTapped()&& popup == null){
			if(!pause)pause();
			else dePause();
		}
		if(input.escape.isTapped()){
			if(popup == null)component.setTitleScreen();
			else setPopup(null);
		}
		if(popup != null){
			popup.update(input.getMouseX(), input.getMouseY());
		}
		if(input.LMBTapped() || input.RMBTapped()) {
			new Sound("src/res/Sounds/klick.mp3").play();
		}
	}
	
	public void levelUp(){
		super.levelUp();
		if(!isReadyForLevelUp() && level != 1) {
			statusBar.removeButton(levelUpButton);
			isReady = false;
		}
	}
	
	public boolean isReadyForLevelUp(){
		switch (level) {
		case 1:
			if(inventory.gold >= 25 && inventory.meat >= 16)return true;
		case 2:
			if(inventory.gold >= 100 && inventory.meat >=  60 && inventory.wood >= 25) return true;
		}
		
		return false;
	}
	
	
}
