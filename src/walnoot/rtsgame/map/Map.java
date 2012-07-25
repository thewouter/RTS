package walnoot.rtsgame.map;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import walnoot.rtsgame.Util;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.entities.SheepEntity;
import walnoot.rtsgame.map.entities.SnakeEntity;
import walnoot.rtsgame.map.structures.Structure;
import walnoot.rtsgame.map.structures.TreeStructure;
import walnoot.rtsgame.map.tiles.Tile;

public class Map {
	private Tile[][] surface;
	public List<Entity> entities = new ArrayList<Entity>();
	private LinkedList<Entity> toBeRemoved = new LinkedList<Entity>();
	private PerlinNoise2D noiseObj;
	public int amountSheepGroups = 0;
	int c1 = 0, c2 = 0;
	
	public static final int TREE_GROW_CHANGE = 10, SHEEP_SPAWN_CHANGE_IN_FOREST =2, SHEEP_SPAWN_CHANGE_ON_PLAINS = 5, RADIUS_SHEEP_GROUPS = 5, SIZE_SHEEP_GROUPS = 10;  
	/**
	 *Sheep_spawn_change out of 10,000 change to spawn a sheep group existing of SIZE_SHEEP_GROUPS_IN_SHEEPS sheeps in a radius of RADIUS_SHEEP_GROUPS
	 *size_sheep_groups_in_sheeps has a chance of 20% deviation
	 *  
	 * change to spawn a tree is TREE_GROW_CHANGE^-1
	 */
	
	private static final Comparator<Entity> spriteSorter = new Comparator<Entity>() {
		public int compare(Entity e0, Entity e1){
			int x0 = e0.getxPos();
			int y0 = e0.getyPos();
			int x1 = e1.getxPos();
			int y1 = e1.getyPos();
			if(x0 == x1 && y0 == y1){
				return 0;
			}else if(x0 > x1){
				return 1;
			}else if(x0 < x1){
				return -1;
			}else if(y0 > y1){
				return 1;
			}
			return 0;
		}
	};
	
	
	public Map(int mapSize){
		
		surface = new Tile[mapSize][mapSize];
		noiseObj = new PerlinNoise2D();
		generateMap();
	}
	
	public Map(int mapSize, int amountSheepGroups){
		surface = new Tile[mapSize][mapSize];
		noiseObj = new PerlinNoise2D();
		this.amountSheepGroups = amountSheepGroups;
		generateEmptyMap();
	
	}
	public void update(int translationX, int translationY, int screenWidth, int screenHeight){
		for(Entity e: entities){
			if(e.xPos + e.yPos + 2 > - ((translationY) / 8) && e.xPos + e.yPos - 1 < - ((translationY - screenHeight - 128)/ 8) && e.xPos - e.yPos - 3 < ((translationX) / 16) && e.xPos - e.yPos + 1 > ((translationX - screenWidth) / 16)) {
				e.update();
			}else if(e instanceof MovingEntity){
				e.update();
			}
		}
			
		double fraction = ((double)amountSheepGroups/((double)SheepEntity.APROX_LIFETIME_IN_TICKS));
		if(Util.RANDOM.nextDouble() <= fraction){
			addSheepGroup();
		}
		entities.removeAll(toBeRemoved);
	}
	
	public void addSheepGroup(){
		while(true){
			int x = Util.RANDOM.nextInt(getLength());
			int y = Util.RANDOM.nextInt(getWidth());
			if(!getTile(x, y).isSolid()){
				if(getEntity(x, y) != null){
					for(int i = 0,control = 0; i < SIZE_SHEEP_GROUPS && control < RADIUS_SHEEP_GROUPS * RADIUS_SHEEP_GROUPS; control++){
						int xPos = x - 2 * RADIUS_SHEEP_GROUPS + (Util.RANDOM.nextInt(RADIUS_SHEEP_GROUPS * 2)) - 2;
						int yPos = y - 2 * RADIUS_SHEEP_GROUPS + (Util.RANDOM.nextInt(RADIUS_SHEEP_GROUPS * 2)) - 2;
						if(xPos > 0 && yPos > 0){
							if(!getTile(xPos, yPos).isSolid()){
								addEntity((new SheepEntity(this, xPos, yPos)));
								i++;
							}
						}
					}
					return;
				}
			}
		}
	}
	
	public void addSheepGroup(int x, int y){
		if(getEntity(x, y) != null){
			for(int i = 0,control = 0; i < SIZE_SHEEP_GROUPS && control < RADIUS_SHEEP_GROUPS * RADIUS_SHEEP_GROUPS; control++){
				int xPos = x - 2 * RADIUS_SHEEP_GROUPS + (Util.RANDOM.nextInt(RADIUS_SHEEP_GROUPS * 2)) - 2;
				int yPos = y - 2 * RADIUS_SHEEP_GROUPS + (Util.RANDOM.nextInt(RADIUS_SHEEP_GROUPS * 2)) - 2;
				if(xPos > 0 && yPos > 0){
					if(!getTile(xPos, yPos).isSolid()){
						addEntity((new SheepEntity(this, xPos, yPos)));
						i++;
					}
				}
			}
			return;
		}
	}
	
	public void generateEmptyMap(){
		for(int x = 0; x < getWidth(); x++){
			for(int y = 0; y < getWidth(); y++){
				float noise = noiseObj.perlinNoise(x, y, 0.3f, 32f, 4);
			
				if(noise > 0) {
					surface[x][y] = Tile.grass1;
				}
				else if(noise > -0.2f) surface[x][y] = Tile.sand1;
				else surface[x][y] = Tile.water1;
			}
		}
	}
	
	public void generateMap(){
		
		for(int x = 0; x < getWidth(); x++){
			for(int y = 0; y < getWidth(); y++){
				float noise = noiseObj.perlinNoise(x, y, 0.3f, 32f, 4);
				
				if(noise > 0) {
					surface[x][y] = Tile.grass1;
					if(noise > 0.8) {
						if(Util.RANDOM.nextInt(TREE_GROW_CHANGE) == 0)addEntity(new TreeStructure(this, x, y));
						if(Util.RANDOM.nextInt(10000) < SHEEP_SPAWN_CHANGE_IN_FOREST){
							amountSheepGroups++;
							addSheepGroup(x,y);
						}
					}else{
						if(Util.RANDOM.nextInt(10000) < SHEEP_SPAWN_CHANGE_ON_PLAINS && x > RADIUS_SHEEP_GROUPS + 1 && x < getLength() - RADIUS_SHEEP_GROUPS - 1 && y > RADIUS_SHEEP_GROUPS  + 1 && y < getWidth() - RADIUS_SHEEP_GROUPS - 1){
							amountSheepGroups++;
							addSheepGroup(x,y);
						}
					}
				}
				else if(noise > -0.2f) surface[x][y] = Tile.sand1;
				else surface[x][y] = Tile.water1;
			}
		}
	}
	
	public void render(Graphics g, Point translation, Dimension screenSize, int screenWidth, int screenHeight){
		g.translate(translation.x, translation.y);
		
		for(int x = 0; x < getWidth(); x++){
			for(int y = 0; y < getLength(); y++){
				if(x + y + 2 > - ((translation.y) / 8) && x + y - 1 < - ((translation.y - screenHeight)/ 8) && x - y - 3 < ((translation.x) / 16) && x - y + 1 > ((translation.x - screenWidth) / 16)){
					getTile(x, y).render(g, screenSize, translation, new Point(x, y));
				}
			}
		}
		
		Collections.sort(entities, spriteSorter);
		
		for(Entity e: entities){
			int x = e.getxPos();
			int y = e.getyPos();
			if(x + y + 2 > - ((translation.y) / 8) && x + y - 1 < - ((translation.y - screenHeight - 128)/ 8) && x - y - 3 < ((translation.x) / 16) && x - y + 1 > ((translation.x - screenWidth) / 16)){
				e.render(g);
			}
		}
		
		g.translate(-translation.x, -translation.y);
	}
	
	public boolean isSolid(Point pos){
		return isSolid(pos.x, pos.y);
	}
	
	public boolean isSolid(int x, int y){
		if(x < 0 || y < 0 || x >= surface.length || y >= surface.length) return true; //buiten de map
		if(surface[x][y].isSolid()) return true;
		
		for(Entity e: entities){
			if(e.isSolid(x, y)) return true;
		}
		
		return false;
	}
	
	public Entity getEntity(int x, int y){
		for(Entity e: entities){
			if(e instanceof Structure){
				Structure structure = (Structure) e;
				
				int dx = x - structure.getxPos();
				int dy = y - structure.getyPos();
				
				if(dx >= 0 && dy >= 0){
					if(dx < structure.getSize() && dy < structure.getSize()) return structure;
				}
			}else{
				if(e.getxPos() == x && e.getyPos() == y) return e;
			}
		}
		return null;
	}
	
	public boolean isOnMap(int x, int y){
		if(x <= getLength() && x >= 0 && y >= 0 && y < getWidth()){
			return true;
		}
		return false;
	}
	
	public Tile getTile(int x, int y){
		if(x < 0 || y < 0){
			return null;
		}
		
		if(x >= getWidth() || y >= getLength()){ 
			return null;
		}
		 
		return surface[x][y];
	}
	
	public int getWidth(){
		return surface.length;
	}
	
	public void removeEntity(int x, int y){
		toBeRemoved.add(getEntity(x, y));
	}
	
	public Entity getClosestEntity(int x, int y){
		int closestDistance = 999;
		int xe, ye;
		Entity closest = new SnakeEntity(null, 0, 0);
		for(Entity e: entities){
			xe = e.getxPos();
			ye = e.getyPos();
			if(Util.getDistance(x, y, xe, ye) < closestDistance && xe !=x && ye != y){
				closest = e;
				closestDistance = Util.getDistance(x, y, xe, ye);
			}
		}
		return closest;
	}
	
	public MovingEntity getClosestMovingEntity(int x, int y){
		int closestDistance = 999;
		int xe, ye;
		Entity closest = null;
		for(Entity e: entities){
			xe = e.getxPos();
			ye = e.getyPos();
			if(Util.getDistance(x, y, xe, ye) < closestDistance && xe !=x && ye != y && e instanceof MovingEntity){
				closest = e;
				closestDistance = Util.getDistance(x, y, xe, ye);
			}
		}
		return (MovingEntity) closest;
	}
	
	public int getLength(){
		return surface[0].length;
	}
	
	public void addEntity(Entity u){
		if(!(u instanceof Structure) || ((Structure) u ).getSize() == 1){
			if(getEntity(u.xPos, u.yPos)== null){
				if(getTile(u.getxPos(), u.getyPos()) == null) return;
				if(!getTile(u.getxPos(), u.getyPos()).isSolid()){
					entities.add(u);
				}
			}
		}else{
			Structure structure = (Structure) u;
			for(int x = 0; x < structure.getSize(); x++){
				for(int y = 0; y < structure.getSize(); y++){
					if(getTile(u.xPos + x, u.yPos + y).isSolid()) return; System.out.println("gets");
				}
			}
			entities.add(u);
			
		}
	}
	
	public void changeTile(int xPos, int yPos, Tile t){
		if(getEntity(xPos,yPos)== null){
			surface[xPos][yPos] = t;
		}
	}
	
	public void removeEntity(Entity u){
		entities.remove(u);
	}
}
