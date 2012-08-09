package walnoot.rtsgame.map.structures;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.tiles.Tile;

public abstract class MineStructure extends Structure {
	int size;
	BufferedImage image;
	public int textureX, textureY;
	
	
	public MineStructure(Map map, int xPos, int yPos, int ID, int size) {
		super(map, xPos, yPos, ID);
		if(size < 1) size = 1;
		if(size > 3) size = 3;
		this.size = size;
		textureX = (ID - 300) * 4;
		if(size == 1){
			textureY = 0;
		}else if(size == 2){
			textureY = 1;
		}else{
			textureY = 4;
		}
		loadImage(textureX, textureY);
		health = getMaxHealth();
		
	}
	
	public void render(Graphics g){
		int x = getScreenX() - (Tile.WIDTH / 2) * (getSize() - 1);
		int y = getScreenY();
		if(size == 2) y -= Tile.HEIGHT / 2;
		g.drawImage(image, x, y, null);
	}
	
	private void loadImage(int textureX, int textureY){
		int x = textureX * Tile.WIDTH;
		int y = textureY * Tile.HEIGHT;
		
		int width = getSize() * Tile.WIDTH;
		int height = getSize() * Tile.HEIGHT;
		
		image = Images.mines.getSubimage(x, y, width, height);
		
	}

	public int getSize() {
		return size;
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
		loadImage(textureX, textureY);
	}
	
	public int getExtraOne(){
		return size;
	}
	
	
}
