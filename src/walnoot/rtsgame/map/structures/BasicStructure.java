package walnoot.rtsgame.map.structures;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.screen.GameScreen;

public abstract class BasicStructure extends Structure {
	private BufferedImage image;
	protected Direction front;
	
	public BasicStructure(Map map, GameScreen screen, int xPos, int yPos, int textureX, int textureY, int ID, Direction front){
		super(map, screen, xPos, yPos, ID);
		this.front = front;
		loadImage(textureX, textureY);
	}
	
	public void render(Graphics g){
		if(front != Direction.SOUTH_EAST) g.drawImage(image, getScreenX() - (Tile.WIDTH / 2) * (getSize() - 1), getScreenY() - (getHeadSpace() * Tile.HEIGHT) + (Tile.HEIGHT / 2) * (getSize() - 1), null);
		else{
			g.drawImage(image, image.getWidth() + getScreenX() - (Tile.WIDTH / 2) * (getSize() - 1) ,  getScreenY() - (getHeadSpace() * Tile.HEIGHT) + (Tile.HEIGHT / 2) * (getSize() - 1), 
					getScreenX() - (Tile.WIDTH / 2) * (getSize() - 1), getScreenY() - (getHeadSpace() * Tile.HEIGHT) + (Tile.HEIGHT / 2) * (getSize() - 1) + image.getHeight(), 
					0, 0, image.getWidth(), image.getHeight(), null); // there must be an easyer way  somewhere... nevermind
		}
	}
	
	public void render(Graphics g, int x, int y){
		g.drawImage(image, x - (Tile.WIDTH / 2) * (getSize() - 1), y - (getHeadSpace() * Tile.HEIGHT) + (Tile.HEIGHT / 2) * (getSize() - 1), null);
		
	}
	
	protected void loadImage(int textureX, int textureY){
		int x = textureX * Tile.WIDTH;
		int y = textureY * Tile.HEIGHT;
		
		int width = getSize() * Tile.WIDTH;
		int height = (getSize() + getHeadSpace()) * Tile.HEIGHT;
		
		image = Images.structures.getSubimage(x, y, width, height);
	}
	
	public Image getImage(){
		return image;
	}
	
	
}
