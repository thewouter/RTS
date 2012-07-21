package walnoot.rtsgame.map.structures;

import java.awt.Graphics;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.screen.GameScreen;

public class GoldMine extends MineStructure {
	public static int ID = 300, HEALTH_SMALL = 100, HEALTH_MEDIUM = 200, HEALTH_LARGE = 300;
	
	public GoldMine(Map map, int xPos, int yPos, int size) {
		super(map, xPos, yPos, ID, size);
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		System.out.println("test");
		
		return false;
	}
	
	public void update() {
		if(getHealth()  <=  100){
			size = 1;
		}else if(getHealth() <= 200){
			size = 2;
		}else{
			size = 3;
		}
	}

	public int getMaxHealth() {
		switch(size){
		case 1:
			return HEALTH_SMALL;
		case 2:
			return HEALTH_MEDIUM;
		case 3:
			return HEALTH_LARGE;
		default:
			return 0;
		}
	}

	public String getName() {
		switch(size){
		case 1:
			return "Goldmine small";
		case 2:
			return "Goldmine medium";
		case 3:
			return "Goldmine large";
		default:
			return "unknown??";
		}
		
	}

}
