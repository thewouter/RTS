package walnoot.rtsgame.map.structures.nonnatural.warrelated;

import java.util.HashMap;

import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.screen.GameScreen;

public class WoodenGate extends BasicStructure {
	private static int ID = 214;
	
	public WoodenGate(Map map, GameScreen screen, int xPos, int yPos, Direction front) {
		super(map, screen, xPos, yPos, 8, 0, ID, front);
	}

	public int getSize() {
		return 3;
	}

	public void update() {
		
	}

	public int getMaxHealth() {
		return 350;
	}

	public String getName() {
		return "Wooden gate";
	}

	public HashMap<String, Integer> getCosts() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 5);
		costs.put("wood", 20);
		return costs;
	}

	public int getHeadSpace() {
		return 2;
	}

	public int getExtraOne() {
		return 0;
	}
	
	public boolean isWalkable(int x, int y, Entity toPass){
		x -= xPos;
		y -= yPos;
		
		if(!toPass.isOwnedByPlayer || x > getSize() || y > getSize() || x < 0 || y < 0) return false;
		if(front == Direction.SOUTH_EAST)return y == 1;
		return x == 1;
	}
}
