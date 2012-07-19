package walnoot.rtsgame.map.structures;

import java.awt.Graphics;

import walnoot.rtsgame.map.Map;

public class GoldMine extends MineStructure {
	public static int ID = 300;
	
	public GoldMine(Map map, int xPos, int yPos, int sizeX, int sizeY) {
		super(map, xPos, yPos, ID, sizeX, sizeY);
	}

	protected int getHeadSpace() {
		return 1;
	}

	public int getSize() {
		return 1;
	}
	

	public void update() {
		
	}

	public int getMaxHealth() {
		return 0;
	}

	public String getName() {
		return "GoldMine";
	}

}
