package walnoot.rtsgame.map.structures;

import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.screen.GameScreen;

public abstract class Structure extends Entity {
	
	public Structure(Map map, GameScreen screen ,int xPos, int yPos, int ID){
		super(map,xPos, yPos, ID, screen);
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
