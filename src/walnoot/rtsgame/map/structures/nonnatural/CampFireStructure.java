package walnoot.rtsgame.map.structures.nonnatural;

import java.util.HashMap;

import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.screen.GameScreen;

public class CampFireStructure extends BasicStructure{
	
	public static final int ID = 200;
	
	public CampFireStructure(Map map,GameScreen screen, int xPos, int yPos, Direction front){
		super(map, screen, xPos, yPos, 2, 0, ID, front);
	}
	
	public CampFireStructure(Map map, GameScreen screen, int xPos, int yPos,  int health, Direction front){
		super(map,screen, xPos,yPos,2,0,ID, front);
		this.health = health;
	}
	
	public void update(){
		
	}
	
	public int getSize(){
		return 1;
	}
	
	public int getMaxHealth(){
		return 10;
	}
	
	public String getName(){
		return "Campfire";
	}
	public int getHeadSpace(){
		return 1;
	}

	public int getExtraOne() {
		return 0;
	}
	
	public HashMap<String, Integer> getCosts(){
		return new HashMap<String, Integer>();
	}
}
