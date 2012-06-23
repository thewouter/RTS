package walnoot.rtsgame.map.entities;

import java.awt.Graphics;

import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.tribes.Tribe;

public abstract class ItemEntity extends Entity {
	
	public ItemEntity(Map map, int xPos, int yPos, Tribe tribe){
		super(map, xPos, yPos, tribe);
	}
	
	public void update(){
		
	}
	
	public void render(Graphics g){
	}
	
	public int getMaxHealth(){
		return 0;
	}
}
