package walnoot.rtsgame.map.structures.nonnatural;

import java.util.HashMap;

import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.screen.GameScreen;

public class Farm extends BasicStructure{
	public static int ID = 209;
	public Farm(Map map, GameScreen screen, int xPos, int yPos, Direction front) {
		super(map, screen, xPos, yPos,2, 8, ID, front);
	}

	public int getHeadSpace() {
		return 2;
	}

	public int getSize() {
		return 3;
	}

	public void update() {
	}

	public int getMaxHealth() {
		return 6;
	}

	public String getName() {
		return "Farm";
	}

	public HashMap<String, Integer> getCosts() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 5);
		costs.put("wood", 15);
		costs.put("stone", 3);
		return costs;
	}

	public int getExtraOne() {
		return 0;
	}

}
