package walnoot.rtsgame.map.structures.nonnatural.warrelated;

import java.awt.Graphics;

import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.structures.Structure;
import walnoot.rtsgame.screen.GameScreen;

public class Wall extends Structure {

	public Wall(Map map, GameScreen screen, int xPos, int yPos, int ID) {
		super(map, screen, xPos, yPos, ID);
	}

	public int getSize() {
		return 1;
	}

	public void update() {
	}

	public void render(Graphics g) {
	}

	public int getMaxHealth() {
		return 0;
	}

	public String getName() {
		return null;
	}

	public int getCosts() {
		return 0;
	}

	public int getExtraOne() {
		return 0;
	}

	public int getHeadSpace() {
		return 0;
	}

}
