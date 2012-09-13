package walnoot.rtsgame.map;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.entities.SheepEntity;
import walnoot.rtsgame.map.structures.Structure;
import walnoot.rtsgame.map.structures.natural.GoldMine;
import walnoot.rtsgame.map.structures.natural.MineStructure;
import walnoot.rtsgame.map.structures.natural.TreeStructure;
import walnoot.rtsgame.map.structures.nonnatural.StoneMine;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.rest.Util;

public class Map {
	public Tile[][] surface;
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<Entity> notOnMap = new ArrayList<Entity>();
	public LinkedList<Entity> toBeRemoved = new LinkedList<Entity>(), toBeAdded = new LinkedList<Entity>(), toBeRemovedFromMap = new LinkedList<Entity>();
	public PerlinNoise2D noiseObj;
	public int amountSheepGroups = 0;
	int c1 = 0, c2 = 0;
	
	public static final int TREE_GROW_CHANGE = 15, SHEEP_SPAWN_CHANGE_IN_FOREST =2, SHEEP_SPAWN_CHANGE_ON_PLAINS = 5, RADIUS_SHEEP_GROUPS = 5, SIZE_SHEEP_GROUPS = 10, SPAWN_CHANGE_GOLD_MINE = 3;  
	/*
	 *Sheep_spawn_change out of 10,000 change to spawn a sheep group existing of SIZE_SHEEP_GROUPS_IN_SHEEPS sheeps in a radius of RADIUS_SHEEP_GROUPS
	 *size_sheep_groups_in_sheeps has a chance of 20% deviation
	 *  
	 * change to spawn a tree is TREE_GROW_CHANGE^-1
	 */
	
	private static final Comparator<Entity> spriteSorter = new Comparator<Entity>() {
		public int compare(Entity e0, Entity e1){
			int y0 = e0.getxPos() + e0.getyPos(); //aprox. screen y coordinate of e0
			int y1 = e1.getxPos() + e1.getyPos(); //aprox. screen y coordinate of e1

			if(y1 < y0)
				return +1;
			if(y1 > y0)
				return -1;
			return 0;
		}
	};
	
	
	public Map(int mapSize){
		
		surface = new Tile[mapSize][mapSize];
		noiseObj = new PerlinNoise2D();
		//this.player = screen;
		generateMap();
	}
	
	public Map(int mapSize, int amountSheepGroups){
		surface = new Tile[mapSize][mapSize];
		noiseObj = new PerlinNoise2D();
		this.amountSheepGroups = amountSheepGroups;
		generateEmptyMap();
	
	}
	
	public synchronized void update(int translationX, int translationY, int screenWidth, int screenHeight){
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
		entities.removeAll(toBeRemovedFromMap);
		toBeRemovedFromMap.clear();
		notOnMap.removeAll(toBeRemoved);
		toBeRemoved.clear();
		entities.addAll(toBeAdded);
		toBeAdded.clear();
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
								addEntity((new SheepEntity(this,null, xPos, yPos)));
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
		if(getEntity(x, y) == null){
			for(int i = 0,control = 0; i < SIZE_SHEEP_GROUPS && control < RADIUS_SHEEP_GROUPS * RADIUS_SHEEP_GROUPS; control++){
				int xPos = x - 2 * RADIUS_SHEEP_GROUPS + (Util.RANDOM.nextInt(RADIUS_SHEEP_GROUPS * 2)) - 2;
				int yPos = y - 2 * RADIUS_SHEEP_GROUPS + (Util.RANDOM.nextInt(RADIUS_SHEEP_GROUPS * 2)) - 2;
				if(xPos > 0 && yPos > 0){
					if(!getTile(xPos, yPos).isSolid()){
						addEntity((new SheepEntity(this,null, xPos, yPos)));
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
					if(noise > 0.6) {
						if(Util.RANDOM.nextInt(TREE_GROW_CHANGE) == 0)addEntity(new TreeStructure(this,null, x, y));
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
					
					if(Util.RANDOM.nextInt(10000) < SPAWN_CHANGE_GOLD_MINE){
						int size = Util.RANDOM.nextInt(3) + 1;
						if(x > size && y > size) addEntity(new GoldMine(this,null, x - size, y - size ,size));
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
		
		LinkedList<Entity> toSort = new LinkedList<Entity>();
		for(Entity e: entities){
			int x = e.getScreenX() + translation.x;
			int y = e.getScreenY() + translation.y;
			if(x > - 30  && y > - 30 && x < screenWidth + 30 && y < screenHeight + 30 ){
				toSort.add(e);
			}
		}
		Collections.sort(toSort, spriteSorter);
		
		for(Entity e: toSort){
			e.render(g);
		}
		
		g.translate(-translation.x, -translation.y);
	}
	
	public boolean isSolid(Point pos){
		return isSolid(pos.x, pos.y);
	}
	
	public boolean isSolid(int x, int y){
		if(x < 0 || y < 0 || x >= surface.length || y >= surface.length) return true; //outside the map
		if(surface[x][y].isSolid()) return true;
		
		for(Entity e: entities){
			if(e.isSolid(x, y)) return true;
		}
		return false;
	}
	
	public synchronized Entity getEntity(int x, int y){
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
	
	public LinkedList<Entity> getEntities(int x, int y){
		LinkedList<Entity> result = new LinkedList<Entity>();
		for(Entity e: entities){
			if(e instanceof Structure){
				Structure structure = (Structure) e;
				
				int dx = x - structure.getxPos();
				int dy = y - structure.getyPos();
				
				if(dx >= 0 && dy >= 0){
					if(dx < structure.getSize() && dy < structure.getSize()) result.add(structure);
				}
			}else{
				if(e.getxPos() == x && e.getyPos() == y) result.add(e);
			}
		}
		return result;
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
	
	public Entity getClosestMine(int x, int y){
		int closestDistance = 999;
		int xe, ye;
		Entity closest = null;
		for(Entity e: entities){
			xe = e.getxPos();
			ye = e.getyPos();
			if(Util.getDistance(x, y, xe, ye) < closestDistance && xe !=x && ye != y && e instanceof MineStructure){
				closest = e;
				closestDistance = Util.getDistance(x, y, xe, ye);
			}
		}
		return closest;
	}
	
	public Entity getClosestMine(int x, int y, ArrayList<Entity> notIncluded){
		int closestDistance = 999;
		int xe, ye;
		Entity closest = null;
		for(Entity e: entities){
			xe = e.getxPos();
			ye = e.getyPos();
			if(Util.getDistance(x, y, xe, ye) < closestDistance && xe !=x && ye != y && (e instanceof MineStructure || e instanceof StoneMine)&& !notIncluded.contains(e)){
				//System.out.println(e);
				closest = e;
				closestDistance = Util.getDistance(x, y, xe, ye);
			}
		}
		return closest;
	}
	
	public Entity getClosestEntity(int x, int y){
		int closestDistance = 999;
		int xe, ye;
		Entity closest = null;
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
	
	public MovingEntity getClosestMovingEntity(int x, int y, LinkedList<MovingEntity> notIncluded){
		int closestDistance = 999;
		int xe, ye;
		Entity closest = null;
		for(Entity e: entities){
			xe = e.getxPos();
			ye = e.getyPos();
			if(Util.getDistance(x, y, xe, ye) < closestDistance && xe !=x && ye != y && e instanceof MovingEntity && !(notIncluded.contains(e))){
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
		if(u == null)return;
		if(!(u instanceof Structure) || ((Structure) u ).getSize() == 1){
			if(getEntity(u.xPos, u.yPos) instanceof MovingEntity || getEntity(u.xPos, u.yPos) == null){
				if(getTile(u.getxPos(), u.getyPos()) == null) return;
				if(!getTile(u.getxPos(), u.getyPos()).isSolid()){
					toBeAdded.add(u);
				}
			}
		}else{
			Structure structure = (Structure) u;
			for(int x = 0; x < structure.getSize(); x++){
				for(int y = 0; y < structure.getSize(); y++){
					if(getTile(u.xPos + x, u.yPos + y).isSolid()) return;
					if(getEntity(u.getxPos() + x, u.getyPos() + y) != null) return;
				}
			}
			toBeAdded.add(u);
			
		}
	}
	
	public void addEntity(LinkedList<Entity> entities){
		for(Entity e:entities){
			addEntity(e);
		}
	}
	
	public void changeTile(int xPos, int yPos, Tile t){
		if(getEntity(xPos,yPos)== null){
			surface[xPos][yPos] = t;
		}
	}
	
	public void removeEntity(Entity u){
		toBeRemoved.add(u);
	}
	
	public void removeEntityFromMap(Entity u){
		toBeRemovedFromMap.add(u);
	}
	
	public void setEntityBackOnMap(Entity u){
		removeEntity(u);
		addEntity(u);
	}
	
	public Tile[][] getSurface(){
		return surface;
	}

	public Entity getClosestTree(int x, int y) {
		int closestDistance = 999;
		int xe, ye;
		Entity closest = null;
		for(Entity e: entities){
			xe = e.getxPos();
			ye = e.getyPos();
			if(Util.getDistance(x, y, xe, ye) < closestDistance && xe !=x && ye != y && e instanceof TreeStructure){
				closest = e;
				closestDistance = Util.getDistance(x, y, xe, ye);
			}
		}
		return closest;
	}
}