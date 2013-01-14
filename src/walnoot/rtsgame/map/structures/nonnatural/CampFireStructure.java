package walnoot.rtsgame.map.structures.nonnatural;

import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.screen.GameScreen;

public class CampFireStructure extends BasicStructure{
	
	public static final int ID = 200;
	
	public CampFireStructure(Map map,GameScreen screen, int xPos, int yPos){
		super(map, screen, xPos, yPos, 2, 0, ID);
	}
	
	public CampFireStructure(Map map, GameScreen screen, int xPos, int yPos,  int health){
		super(map,screen, xPos,yPos,2,0,ID);
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
	
	public int getCosts(){
		return 20;
	}
}
