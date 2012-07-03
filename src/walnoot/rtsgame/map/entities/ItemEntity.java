package walnoot.rtsgame.map.entities;

import java.awt.Graphics;

import walnoot.rtsgame.map.Map;

public abstract class ItemEntity extends Entity {
	
	public ItemEntity(Map map, int xPos, int yPos, int ID){
		super(map, xPos, yPos, ID);
	}
	
	public void update(){
		
	}
	
	public void render(Graphics g){
	}
	
	public int getMaxHealth(){
		return 0;
	}
}
