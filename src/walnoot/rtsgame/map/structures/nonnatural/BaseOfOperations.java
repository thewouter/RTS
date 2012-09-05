package walnoot.rtsgame.map.structures.nonnatural;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.screen.GameScreen;

public class BaseOfOperations extends BasicStructure {
	
	public static int ID = 203;
	
	public BaseOfOperations(Map map, GameScreen screen, int xPos, int yPos) {
		super(map,screen, xPos, yPos, 0, 4, ID);
		
	}
	public BaseOfOperations(Map map, GameScreen screen,int xPos, int yPos, int health) {
		super(map, screen, xPos, yPos, 0, 4, ID);
		this.health = health;
		
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		if(entityClicked != this) return true;
		screen.inventory.showInventory();
		
		return false;
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
