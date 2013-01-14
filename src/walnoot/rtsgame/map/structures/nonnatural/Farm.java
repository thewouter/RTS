package walnoot.rtsgame.map.structures.nonnatural;

import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.screen.GameScreen;

public class Farm extends BasicStructure{
	public static int ID = 209;
	public Farm(Map map, GameScreen screen, int xPos, int yPos) {
		super(map, screen, xPos, yPos,2, 8, ID);
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

	public int getCosts() {
		return 0;
	}

	public int getExtraOne() {
		return 0;
	}

}
