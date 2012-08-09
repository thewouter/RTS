package walnoot.rtsgame.map.structures;

import java.awt.image.BufferedImage;

import walnoot.rtsgame.map.Map;

public class TreeStructure extends BasicStructure{
	
	BufferedImage image;
	public final static int ID = 202;

	public TreeStructure(Map map, int xPos, int yPos) {
		super(map, xPos, yPos, 3, 0, ID);
	}
	
	
	public TreeStructure(Map map, int xPos, int yPos, int health){
		super(map, xPos, yPos, 3, 0, ID);
		this.health = health;
	}
	
	public void update() {
		
	}
	
	public int getMaxHealth() {
		return 5;
	}
	
	public String getName() {
		return "tree";
	}

	protected int getHeadSpace() {
		return 2;
	}

	public int getSize() {
		return 1;
	}

	public int getExtraOne() {
		return 0;
	}

}
