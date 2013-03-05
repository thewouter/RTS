 package walnoot.rtsgame.map.structures.natural;

import java.awt.Graphics;
import java.util.HashMap;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.screen.GameScreen;

public class StoneMine extends MineStructure {
	private static int ID = 206, MINES_PER_DAMAGE = 10;
	private int counter = 0;
	
	public StoneMine(Map map, GameScreen screen, int xPos, int yPos, Direction front) {
		super(map, screen, xPos, yPos,1, 1, ID);

		int x = 2 * Tile.WIDTH;
		int y = 4 * Tile.HEIGHT;
		
		int width = getSize() * Tile.WIDTH;
		int height = (getSize() + getHeadSpace()) * Tile.HEIGHT;
		
		image = Images.structures.getSubimage(x, y, width, height);
	}
	
	public StoneMine(Map map, GameScreen screen, int xPos, int yPos, int health, Direction front) {
		super(map, screen, xPos, yPos,1, 1, ID);
		this.health = health;
		int x = 2 * Tile.WIDTH;
		int y = 4 * Tile.HEIGHT;
		
		int width = getSize() * Tile.WIDTH;
		int height = (getSize() + getHeadSpace()) * Tile.HEIGHT;
		
		image = Images.structures.getSubimage(x, y, width, height);
	}


	public int getHeadSpace() {
		return 2;
	}

	public int getSize() {
		return 2;
	}
	
	public void render(Graphics g){
		g.drawImage(image, getScreenX() - (Tile.WIDTH / 2) * (getSize() - 1), getScreenY() - (getHeadSpace() * Tile.HEIGHT) + (Tile.HEIGHT / 2) * (getSize() - 1), null);
	}

	public int getMaxHealth() {
		return 25;
	}

	public String getName() {
		return "StoneMine";
	}

	public HashMap<String, Integer> getCosts() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 5);
		costs.put("wood", 12);
		return costs;
	}

	public void update() {
		
	}
	
	public void mine(int i, Entity miner){
		counter ++;
		if(counter > MINES_PER_DAMAGE){
			counter = 0;
			miner.screen.inventory.addStone(1);
		}
	}
	

}
