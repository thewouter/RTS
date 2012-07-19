package walnoot.rtsgame.map.structures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.tiles.Tile;

public abstract class MineStructure extends BasicStructure {
	int sizeX, sizeY;
	BufferedImage image;
	BufferedImage[] images;
	
	
	public MineStructure(Map map, int xPos, int yPos, int ID, int sizeX, int sizeY) {
		super(map, xPos, yPos, 3, 0, ID);
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		image = Images.mines[ID - 300];
		
		images = new BufferedImage[image.getHeight()/Tile.HEIGHT];
		for(int i = 0; i < images.length; i++){
			images[i] = image.getSubimage(0, i * Tile.HEIGHT, image.getWidth(), Tile.HEIGHT);
		}
		System.out.println(sizeX + "  " + sizeY);
		
	}
	
	public void render(Graphics g){
		sizeX = 4;
		sizeY = 5;
		
		
		g.drawImage(images[0], getScreenX(),getScreenY(), null);
		if(sizeX > 1 || sizeY > 1){
			
			g.drawImage(images[1], getScreenX() + (Tile.WIDTH/2) * (sizeX -1), getScreenY() - (Tile.HEIGHT / 2) * (sizeY - 2) ,null);
			g.drawImage(images[2], getScreenX() + Tile.WIDTH * (sizeX), getScreenY() - ( sizeX - sizeY) * (Tile.HEIGHT / 2), null);
			g.drawImage(images[3], getScreenX() + (Tile.WIDTH/2) * (sizeX), getScreenY() + (Tile.HEIGHT / 2) * (sizeY - 1), null);
			
			int xPos = getScreenX(), yPos = getScreenY();
			xPos += Tile.WIDTH;
			/*for(int x = 1; x < sizeY - 1; x++){
				for(int y = 1; y < sizeX - 1; y++){
					g.drawImage(images[8],xPos , yPos,null);
					xPos +=(Tile.WIDTH / 2);
					yPos -=(Tile.HEIGHT / 2);
				}
				xPos = getScreenX() + Tile.WIDTH + (x) * (Tile.WIDTH / 2);
				yPos = getScreenY() + (x) * (Tile.HEIGHT / 2);
			}*/
			
			for(int i = 1; i < sizeX - 1; i++){
				g.drawImage(images[5], getScreenX() + (Tile.WIDTH / 2) * i ,getScreenY() - (Tile.HEIGHT / 2) * i , null);
				g.drawImage(images[7], getScreenX() + (sizeX * Tile.WIDTH) - (Tile.WIDTH / 2) * i - (Tile.WIDTH), getScreenY() + (i * (Tile.HEIGHT / 2)), null);
			}
			
			for(int i = 1; i < sizeY - 1; i++){
				g.drawImage(images[4],getScreenX() + (Tile.WIDTH / 2) * i ,getScreenY() + (Tile.HEIGHT / 2) * i,null);
			}
			
		}
		
		
		
	}
}
