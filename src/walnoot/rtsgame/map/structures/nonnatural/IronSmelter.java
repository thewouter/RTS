package walnoot.rtsgame.map.structures.nonnatural;

import java.util.HashMap;

import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.screen.GameScreen;

public class IronSmelter extends BasicStructure {
	private static int ID = 215, MELT_TIME = 60;
	private boolean hasIronOre = false;
	private int counter = 0;
	
	public IronSmelter(Map map, GameScreen screen, int xPos, int yPos, Direction front) {
		super(map, screen, xPos, yPos, 4, 3, ID, front);
	}

	public int getSize() {
		return 2;
	}

	public void update() {
		if(!hasIronOre){
			if(screen.inventory.getIronOre() > 0){
				screen.inventory.addIronOre(-1);
				hasIronOre = true;
				loadImage(4, 3);
			}
		}else{
			counter++;
			if(counter >= MELT_TIME){
				hasIronOre  = false;
				screen.inventory.addIron(1);	
				counter = 0;
				loadImage(6	, 4);
			}
		}
	}

	public int getMaxHealth() {
		return 300;
	}

	public String getName() {
		return "Ironsmelter";
	}

	public HashMap<String, Integer> getCosts() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 15);
		costs.put("stone", 10);
		costs.put("wood", 2);
		return costs;
	}

	public int getHeadSpace() {
		return 3;
	}

	public int getExtraOne() {
		return 0;
	}

}
