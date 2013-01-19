package walnoot.rtsgame.map.structures.nonnatural.warrelated;

import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.screen.GameScreen;

public class WoodenGate extends BasicStructure {
	private static int ID = 214;
	public WoodenGate(Map map, GameScreen screen, int xPos, int yPos) {
		super(map, screen, xPos, yPos, 8, 0, ID);
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

	public int getCosts() {
		return 25;
	}

	public int getHeadSpace() {
		return 2;
	}

	public int getExtraOne() {
		return 0;
	}

}
