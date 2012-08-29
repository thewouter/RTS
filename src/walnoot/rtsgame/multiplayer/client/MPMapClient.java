package walnoot.rtsgame.multiplayer.client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.structures.Structure;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.Screen;

public class MPMapClient extends Map{
	boolean hasLoaded = false;
	private BufferedImage grass;
	private BufferedImage grass2;
	private boolean[][] tiles = new boolean[32][32];
	MapLoader loader;
	
	
	public MPMapClient(String map) {
		super(Integer.parseInt(Util.splitString(map).get(0)), Util.parseInt(Util.splitString(map).get(1)));
		
		loader = new MapLoader(map, this);
		loader.start();
		grass2 = Images.terrain[0][0];
		grass = Images.terrain[1][0];
		
		for(int x = 0; x < 20; x++){
			for(int y = 0; y < 20; y++){
				boolean b = Util.RANDOM.nextBoolean();
				tiles[x][y] = b;
			}
		}
		
	}
	
	public void update(int translationX, int translationY,int screenWidth,int screenHeight){
		if(!hasLoaded){
			return;
		}
		
		for(Entity e: entities){
			if(e.xPos + e.yPos + 2 > - ((translationY) / 8) && e.xPos + e.yPos - 1 < - ((translationY - screenHeight - 128)/ 8) && e.xPos - e.yPos - 3 < ((translationX) / 16) && e.xPos - e.yPos + 1 > ((translationX - screenWidth) / 16)) {
				e.update();
			}else if(e instanceof MovingEntity){
				e.update();
			}
		}
		
		entities.removeAll(toBeRemoved);
		toBeRemoved.clear();
		entities.addAll(toBeAdded);
		toBeAdded.clear();
	}
	
	public void render(Graphics g, Point translation, Dimension screenSize, int screenWidth, int screenHeight){
		if(!hasLoaded){
			for(int x = 0; x < tiles.length; x++){
				for(int y = 0; y < tiles[x].length; y++){
					if(tiles[x][y]){
						g.drawImage(grass, x * 32, y * 16, null);
						g.drawImage(grass, x * 32 - 16, y * 16 - 8, null);
					}else{
						g.drawImage(grass2, x * 32, y * 16, null);
						g.drawImage(grass2, x * 32 - 16, y * 16 - 8, null);
					}
				}
				
			}
			Screen.font.drawLine(g, loader.checkProgress() + " %", 10, 10);
			return;
		}
		
		super.render(g, translation, screenSize, screenWidth, screenHeight);
	}
	
	public void addEntityFromHost(Entity u){
		super.addEntity(u);
	}
	
	public void addEntityFromHost(LinkedList<Entity> u){
		super.addEntity(u);
	}
	
	public void removeEntityFromHost(Entity u){
		super.removeEntity(u);
	}
	
	public void addEntity(Entity u){
		// MPMapClient may add entities.
	}
	
	public void addEntity(LinkedList<Entity> entities){
		// MPMapClient may add entities.
	}
	
	public void removeEntity(Entity u){
		// MPMapClient may remove entities.
	}
}
