package walnoot.rtsgame.map.structures.nonnatural;

import java.util.LinkedList;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.popups.entitypopup.EntityOptionsPopup;
import walnoot.rtsgame.popups.entitypopup.Option;
import walnoot.rtsgame.screen.GameScreen;

public class TentIStructure extends BasicStructure {
	
	public final static int ID = 201, TIME_TO_SPAWN_A_PLAYER = 120, MAX_PLAYERS = 8;
	private int time = 0;
	LinkedList<PlayerEntity> players = new LinkedList<PlayerEntity>();
	public TentIStructure(Map map, GameScreen screen, int xPos, int yPos){
		super(map, screen, xPos, yPos,  0, 0,ID);
	}
	
	public TentIStructure(Map map, GameScreen screen ,int xPos, int yPos, int health){
		super(map,screen,xPos,yPos, 0, 0, ID);
		this.health = health;
	}
	
	protected int getHeadSpace(){
		return 2;
	}
	
	public int getSize(){
		return 2;
	}
	
	public void update(){
		LinkedList<Entity> toBeAddedToTheMap = new LinkedList<Entity>();
		if(players.size() < MAX_PLAYERS)time++;
		if(time >= TIME_TO_SPAWN_A_PLAYER && players.size() < MAX_PLAYERS){
			toBeAddedToTheMap.add(new PlayerEntity(map, screen, xPos + 3, yPos + 3));
			screen.inventory.meat-=5;
			screen.inventory.gold--;
			time = 0;
		}
		LinkedList<PlayerEntity> toBeRemoved = new LinkedList<PlayerEntity>();
		for(PlayerEntity p : players){
			if(!map.entities.contains(p) && !map.notOnMap.contains(p)){
				toBeRemoved.add(p);
			}
		}
		players.removeAll(toBeRemoved);
		map.addEntity(toBeAddedToTheMap);
		for(Entity e: toBeAddedToTheMap){
			if(e instanceof PlayerEntity){
				players.add((PlayerEntity) e);
			}
		}
		
		
	}
	
	public int getMaxHealth(){
		return 20;
	}
	
	public String getName(){
		return "Tent";
	}

	public int getExtraOne() {
		return 0;
	}
	
	public int getCosts(){
		return 60;
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		EntityOptionsPopup popup = new EntityOptionsPopup(this, screen);
		popup.addOption(new Option("switch",popup) {
			
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				
			}
		});
		screen.setEntityPopup(popup);
		return false;
	}
}
