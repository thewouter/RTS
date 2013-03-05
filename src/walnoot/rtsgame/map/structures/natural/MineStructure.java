package walnoot.rtsgame.map.structures.natural;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.structures.Structure;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.Screen;

public abstract class MineStructure extends Structure {
	int size;
	public BufferedImage image;
	public int textureX, textureY;
	
	
	public MineStructure(Map map, GameScreen screen, int xPos, int yPos, int ID, int size) {
		super(map, screen,xPos, yPos, ID);
		if(size < 1) size = 1;
		if(size > 3) size = 3;
		this.size = size;
		textureX = (ID - 300) * 3;
		if(size == 1){
			textureY = 0;
		}else if(size == 2){
			textureY = 1;
		}else{
			textureY = 4;
		}
		loadImage(textureX, textureY, Images.mines);
		health = getMaxHealth();
		
	}
	
	public MineStructure(Map map, GameScreen screen, int xPos, int yPos, int ID, int size,  int health){
		super(map, screen, xPos, yPos, ID);
		if(size < 1) size = 1;
		if(size > 3) size = 3;
		this.size = size;
		textureX = (ID - 300) * 3;
		if(size == 1){
			textureY = 0;
		}else if(size == 2){
			textureY = 1;
		}else{
			textureY = 4;
		}
		loadImage(textureX, textureY, Images.mines);
		health = getMaxHealth();
		
		this.health = health;
	}
	
	public void render(Graphics g){
		int x = getScreenX() - (Tile.WIDTH / 2) * (getSize() - 1);
		int y = getScreenY();
		if(size == 2) y -= Tile.HEIGHT / 2;
		g.drawImage(image, x, y, null);
		Screen.font.drawLine(g, "" + uniqueNumber, getScreenX(), getScreenY());
	}
	
	public void loadImage(int textureX, int textureY, BufferedImage i){
		int x = textureX * Tile.WIDTH;
		int y = textureY * Tile.HEIGHT;
		
		int width = getSize() * Tile.WIDTH;
		int height = getSize() * Tile.HEIGHT;
		
		try{
			image = Images.mines.getSubimage(x, y, width, height);
		}catch(Exception e){
			//System.out.println(e);
			//System.out.println(x + " " + y + " " + width + " " + height + " " + textureX);
		}
		
	}

	public int getSize() {
		return size;
	}
	
	public HashMap<String, Integer> getCosts(){
		return new HashMap<String, Integer>();
	}
	
	public void setSize(int size){
		this.size = size;
		if(size == 1){
			textureY = 0;
		}else if(size == 2){
			textureY = 1;
		}else{
			textureY = 4;
		}
		loadImage(textureX, textureY, Images.mines);
	}
	
	public String getExtraOne(){
		return 1 + " " + size;
	}
	
	public void damage(int damage){
		super.damage(damage);
	}
	
	public int getHeadSpace(){
		return 1;
	}

	public abstract void mine(int damage, Entity miner);
}
