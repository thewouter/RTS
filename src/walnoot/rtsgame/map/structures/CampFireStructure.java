package walnoot.rtsgame.map.structures;

import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.tribes.Tribe;

public class CampFireStructure extends BasicStructure{
	
	public static final int ID = 200;
	
	public CampFireStructure(Map map, int xPos, int yPos, Tribe tribe){
		super(map, xPos, yPos, tribe, 2, 0, ID);
	}
	
	public CampFireStructure(Map map, int xPos, int yPos, Tribe tribe, int health){
		super(map,xPos,yPos,tribe,2,0,ID);
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
	protected int getHeadSpace(){
		return 1;
	}
}
