package walnoot.rtsgame.map.structures;

import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.tribes.Tribe;

public abstract class Structure extends Entity {
	
	public Structure(Map map, int xPos, int yPos, Tribe tribe, int ID){
		super(map, xPos, yPos, tribe, ID);
	}
	
	public abstract int getSize();
	
	public boolean isSolid(int x, int y){
		int dx = x - xPos;
		int dy = y - yPos;
		
		if(dx >= 0 && dy >= 0){
			if(dx < getSize() && dy < getSize()) return true;
		}
		
		return false;
	}
}
