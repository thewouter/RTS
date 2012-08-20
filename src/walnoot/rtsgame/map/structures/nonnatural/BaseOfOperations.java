package walnoot.rtsgame.map.structures.nonnatural;

import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.structures.BasicStructure;

public class BaseOfOperations extends BasicStructure {
	
	public static int ID = 203;
	
	public BaseOfOperations(Map map, int xPos, int yPos) {
		super(map, xPos, yPos, 0, 4, ID);
		
	}
	public BaseOfOperations(Map map, int xPos, int yPos, int health) {
		super(map, xPos, yPos, 0, 0, ID);
		this.health = health;
		
	}

	protected int getHeadSpace() {
		return 2;
	}

	public int getSize() {
		return 2;
	}

	public void update() {
		
	}

	public int getMaxHealth() {
		return 300;
	}

	public String getName() {
		return "Base of operations";
	}

	public int getExtraOne() {return 0;	}
	
	public int getCosts(){
		return 50;
	}

}
