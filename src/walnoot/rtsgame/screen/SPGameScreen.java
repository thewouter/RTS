package walnoot.rtsgame.screen;

import java.awt.Point;
import java.util.LinkedList;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.Save;
import walnoot.rtsgame.map.entities.DeerEntity;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.entities.SheepEntity;
import walnoot.rtsgame.map.entities.players.HunterEntity;
import walnoot.rtsgame.map.entities.players.MinerEntity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.structures.natural.GoldMine;
import walnoot.rtsgame.map.structures.natural.TreeStructure;
import walnoot.rtsgame.map.structures.nonnatural.CampFireStructure;
import walnoot.rtsgame.map.structures.nonnatural.LumberJackerSchool;
import walnoot.rtsgame.map.structures.nonnatural.MinerIISchool;
import walnoot.rtsgame.map.structures.nonnatural.MinerISchool;
import walnoot.rtsgame.map.structures.nonnatural.StoneMine;
import walnoot.rtsgame.map.structures.nonnatural.TentIStructure;
import walnoot.rtsgame.menubar.Button;
import walnoot.rtsgame.rest.Util;

public class SPGameScreen extends GameScreen {
	
	private boolean isReady = false;
	private Button levelUpButton;
	
	public SPGameScreen(RTSComponent component, InputHandler input){
		super(component, input);
		
		map = new Map(256);
		
		inventory.gold = 500;
		
		int goodYPos;
		
		for(int i = 4;; i++){
			if(!map.getTile(4, i).isSolid()){
				selectedEntities.add(new PlayerEntity(map,this, 4, i, null));
				goodYPos = i;
				break;
			}
		}
		
		levelUpButton = new Button(Images.buttons[2][1], statusBar) {
			public void onLeftClick() {
				levelUp();
			}
		};
		targetEntity = selectedEntities.getFirst();
		map.addEntity(selectedEntities.getFirst());
		
		map.addEntity(new DeerEntity(map, this,4, goodYPos+1)); //voor de test, later weghalen
		map.addEntity(new SheepEntity(map,this, 4, goodYPos+2)); //voor de test, later weghalen
		map.addEntity(new TentIStructure(map,this, 4, goodYPos + 3)); //voor de test, later weghalen
		map.addEntity(new CampFireStructure(map,this, 4, goodYPos + 5)); //voor de test, later weghalen
		map.addEntity(new TreeStructure(map,this, 4, goodYPos + 7)); //voor de test, later weghalen
		map.addEntity(new HunterEntity(map,this, 4, goodYPos + 9, null)); // etc...
		map.addEntity(new GoldMine(map,null , 10, 10, 3));
		map.addEntity(new MinerEntity(map,this, 10, 20, null));
		map.addEntity(new LumberJackerSchool(map, this, 4, goodYPos + 12));
		map.addEntity(new StoneMine(map, this, 4, goodYPos + 15));
		map.addEntity(new MinerIISchool(map, this, 4, goodYPos + 20));
		map.addEntity(new MinerISchool(map, this, 12, goodYPos + 20));
		
		
		translationX = -selectedEntities.getFirst().getScreenX();
		translationY = -selectedEntities.getFirst().getScreenY();
		
		
		
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
						//for(Entity m:selectedEntities){
							if(((MovingEntity)selectedEntities.getFirst()).isMovable()){
								((MovingEntity) selectedEntities.getFirst()).moveTo(new Point(getMapX(), getMapY()));
								entityPopup = null;
							}
						//}
					}
				}
			}
			LinkedList<Entity> remove = new LinkedList<Entity>();
			for(Entity e: selectedEntities){
				if(!map.entities.contains(e)) remove.add(e);
			}
			
			
			selectedEntities.removeAll(remove);
			if(pointer != null){
				pointer.update(); 
				if(bar.showPopup == false){
					pointer = null;
				}
			}
			
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
	}
	
	public void levelUp(){
		super.levelUp();
		isReady = false;
		if(!isReadyForLevelUp() && level != 1) statusBar.removeButton(levelUpButton);
	}
	
	public boolean isReadyForLevelUp(){
		
		switch (level) {
		case 1:
			if(inventory.gold >= 1 && inventory.meat >= 1)return true;
		case 2:
			if(inventory.gold >= 300 && inventory.meat >=  60) return true;
		}
		
		return false;
	}
	
	
}
