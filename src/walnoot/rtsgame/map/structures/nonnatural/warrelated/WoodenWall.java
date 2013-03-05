package walnoot.rtsgame.map.structures.nonnatural.warrelated;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.screen.GameScreen;

public class WoodenWall extends BasicStructure {
	private static int ID = 213; 
	private final Image[] images = new Image[4];
	private boolean[] neightboursAreConnected = {false, false, false, false};
	
	public WoodenWall(Map map, GameScreen screen, int xPos, int yPos, Direction front) {
		super(map, screen, xPos, yPos, 6, 0, ID, front);
		for (int i = 0, k = 0; i < 2; i++){
			for(int j = 0; j < 2; j++, k++){
				int x = (6 + j)* Tile.WIDTH;
				int y = (i) * Tile.HEIGHT * 2;
				
				int width = getSize() * Tile.WIDTH;
				int height = (getSize() + getHeadSpace()) * Tile.HEIGHT;
				
				images[k] = Images.structures.getSubimage(x, y, width, height);
			}
		}
		
		for(int i = 0; i < 4; i++){
			neightboursAreConnected[i] = checkSide(i);
		}
	}
	
	public WoodenWall(Map map, GameScreen screen, int xPos, int yPos, int health, Direction front) {
		super(map, screen, xPos, yPos, 6, 0, ID, front);
		for (int i = 0, k = 0; i < 2; i++){
			for(int j = 0; j < 2; j++, k++){
				int x = (6 + j)* Tile.WIDTH;
				int y = (i) * Tile.HEIGHT * 2;
				
				int width = getSize() * Tile.WIDTH;
				int height = (getSize() + getHeadSpace()) * Tile.HEIGHT;
				
				images[k] = Images.structures.getSubimage(x, y, width, height);
			}
		}
		
		for(int i = 0; i < 4; i++){
			neightboursAreConnected[i] = checkSide(i);
		}
		this.health = health;
	}

	private boolean checkSide(int side){
		ArrayList<Entity> es;
		int x = xPos;
		int y = yPos;
		if(side % 2 == 0){	
			x += side - 1;
		}else{
			y += side - 2;
		}
		es = map.getEntities(x, y);
		for(Entity e:es){
			if (e instanceof WoodenWall){
				((WoodenWall)e).setConnection((side + 2) % 4, true);
				return true;
			}
		}
		return false;
		
	}
	
	public void render(Graphics g){
		
		for(int i = 0; i < images.length; i++){
			if(neightboursAreConnected[i]) g.drawImage(images[i], getScreenX() - (Tile.WIDTH / 2) * (getSize() - 1), getScreenY() - (getHeadSpace() * Tile.HEIGHT) + (Tile.HEIGHT / 2) * (getSize() - 1), null);
		}
		boolean flag = true;
		for(boolean b:neightboursAreConnected){
			if(b) flag = false;
		}
		if(flag){
			for(Image image:images){
				g.drawImage(image, getScreenX() - (Tile.WIDTH / 2) * (getSize() - 1), getScreenY() - (getHeadSpace() * Tile.HEIGHT) + (Tile.HEIGHT / 2) * (getSize() - 1), null);
			}
		}
	}
	
	public void setConnection(int side, boolean connected){
		neightboursAreConnected[side] = connected;
	}

	public int getSize() {
		return 1;
	}

	public void update() {
		
	}

	public int getMaxHealth() {
		return 200;
	}

	public String getName() {
		return "Wall";
	}

	public HashMap<String, Integer> getCosts() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 1);
		costs.put("wood", 1);
		
		return costs;
	}

	public String getExtraOne() {
		return "0";
	}

	public int getHeadSpace() {
		return 1;
	}

}
