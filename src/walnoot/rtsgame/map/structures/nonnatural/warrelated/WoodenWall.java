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

public class WoodenWall extends Wall {

	private static int ID = 213; 


	public WoodenWall(Map map, GameScreen screen, int xPos, int yPos, Direction front) {
		super(map, screen, ID, xPos, yPos, front);
	}
	
	public WoodenWall(Map map, GameScreen screen, int xPos, int yPos, int health, Direction front) {
		super(map, screen, ID, xPos, yPos, health, front);
	}

	protected void loadImages() {
		for (int i = 0, k = 0; i < 2; i++){
			for(int j = 0; j < 2; j++, k++){
				int x = (6 + j)* Tile.WIDTH;
				int y = (i) * Tile.HEIGHT * 2;
				int width = getSize() * Tile.WIDTH;
				int height = (getSize() + getHeadSpace()) * Tile.HEIGHT;
				try{
					images[k] = Images.structures.getSubimage(x, y, width, height);
				}catch(NullPointerException e){
					e.printStackTrace();
				}
			}
		}
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

	public int getMaxHealth() {
		return 200;
	}

	public String getName() {
		return "Wooden Wall: " + this.toString();
	}

	public HashMap<String, Integer> getCosts() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 1);
		costs.put("wood", 1);
		
		return costs;
	}

	public int getHeadSpace() {
		return 1;
	}

}
