package walnoot.rtsgame.map.structures;

import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.structures.nonnatural.warrelated.Wall;
import walnoot.rtsgame.screen.GameScreen;

public abstract class Structure extends Entity {
	
	public Structure(Map map, GameScreen screen ,int xPos, int yPos, int ID){
		super(map,xPos, yPos, ID, screen);
		//checkfor nearby walls to connect if needed.
		if(connectsToWall() && !(this instanceof Wall)){
			for(int x = -1; x <= getSize(); x++){
				for(int y = -1; y <= getSize(); y++){
					Entity e = map.getEntity(xPos + x, yPos + y);
					if(e instanceof Wall){
						((Wall)e).checkSides(this);
					}
				}
			}
		}
		
	}
	
	public abstract int getSize();
	
	public boolean connectsToWall(){
		return false;
	}
	
	public boolean isSolid(int x, int y){
		int dx = x - xPos;
		int dy = y - yPos;
		
		if(dx >= 0 && dy >= 0){
			if(dx < getSize() && dy < getSize()) return true;
		}
		return false;
	}
}
