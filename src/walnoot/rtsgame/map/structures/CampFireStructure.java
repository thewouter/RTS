package walnoot.rtsgame.map.structures;

import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.tribes.Tribe;

public class CampFireStructure extends BasicStructure{
	
	public CampFireStructure(Map map, int xPos, int yPos, Tribe tribe){
		super(map, xPos, yPos, tribe, 2, 0);
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
