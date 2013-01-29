package walnoot.rtsgame.map.structures.nonnatural;

import java.util.HashMap;
import java.util.LinkedList;

import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.MPGameScreen;

public class TentIStructure extends BasicStructure {
	
	public final static int ID = 201, TIME_TO_SPAWN_A_PLAYER = 120, MAX_PLAYERS = 2;
	private int time = 0;
	public LinkedList<PlayerEntity> players = new LinkedList<PlayerEntity>();
	public TentIStructure(Map map, GameScreen screen, int xPos, int yPos, Direction front){
		super(map, screen, xPos, yPos,  0, 0,ID, front);
	}
	
	public TentIStructure(Map map, GameScreen screen ,int xPos, int yPos, int health, Direction front){
		super(map,screen,xPos,yPos, 0, 0, ID, front);
		this.health = health;
	}
	
	public int getHeadSpace(){
		return 2;
	}
	
	public int getSize(){
		return 2;
	}
	
	public void update(){
		LinkedList<Entity> toBeAddedToTheMap = new LinkedList<Entity>();
		if(players.size() < MAX_PLAYERS)time++;
		if(time >= TIME_TO_SPAWN_A_PLAYER && players.size() < MAX_PLAYERS && screen != null && !(screen instanceof MPGameScreen)){
			toBeAddedToTheMap.add(new PlayerEntity(map, screen, xPos + 3, yPos + 3, this));
			screen.inventory.addMeat(-1);
			screen.inventory.addGold(-1);
			time = 0;
		}
		
		LinkedList<PlayerEntity> toBeRemoved = new LinkedList<PlayerEntity>();
		
		for(PlayerEntity p : players){
			if(!map.getEntities().contains(p) && !map.notOnMap.contains(p)){
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
	
	public HashMap<String, Integer> getCosts(){
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 5);
		return costs;
	}
}
