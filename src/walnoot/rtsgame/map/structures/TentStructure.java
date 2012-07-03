package walnoot.rtsgame.map.structures;

import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.tribes.Tribe;

public class TentStructure extends BasicStructure {
	
	public final static int ID = 201;
	
	public TentStructure(Map map, int xPos, int yPos, Tribe tribe){
		super(map, xPos, yPos, tribe, 0, 0,ID);
	}
	
	public TentStructure(Map map, int xPos, int yPos, Tribe tribe, int health){
		super(map,xPos,yPos,tribe,2,0, ID);
		this.health = health;
	}
	
	protected int getHeadSpace(){
		return 2;
	}
	
	public int getSize(){
		return 2;
	}
	
	public void update(){
	}
	
	public int getMaxHealth(){
		return 20;
	}
	
	public String getName(){
		return "Tent";
	}
}
