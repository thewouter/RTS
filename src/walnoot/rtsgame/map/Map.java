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
import walnoot.rtsgame.map.entities.players.professions.Miner;
import walnoot.rtsgame.map.projectiles.Arrow;
import walnoot.rtsgame.map.projectiles.Projectile;
import walnoot.rtsgame.map.structures.Structure;
import walnoot.rtsgame.map.structures.natural.GoldMine;
import walnoot.rtsgame.map.structures.natural.IronMine;
import walnoot.rtsgame.map.structures.natural.MineStructure;
import walnoot.rtsgame.map.structures.natural.StoneMine;
import walnoot.rtsgame.map.structures.natural.TreeStructure;
import walnoot.rtsgame.map.structures.nonnatural.Farm;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.multiplayer.host.MPHost;
import walnoot.rtsgame.multiplayer.host.MPMapHost;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.MPGameScreen;

public class Map {
	public Tile[][] surface;
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<Entity> notOnMap = new ArrayList<Entity>();
	public LinkedList<Entity> toBeRemoved = new LinkedList<Entity>(), toBeAdded = new LinkedList<Entity>(), toBeRemovedFromMap = new LinkedList<Entity>();
	private ArrayList<Projectile> projectiles = new ArrayList<>(), projectilesToAdd = new ArrayList<>(), projectilesToRemove = new ArrayList<>();
	public PerlinNoise2D noiseObj;
	public int amountSheepGroups = 0;
	private GameScreen screen;
	int c1 = 0, c2 = 0;
	
	public static final int TREE_GROW_CHANGE = 15, SHEEP_SPAWN_CHANGE_IN_FOREST =2, 
			SHEEP_SPAWN_CHANGE_ON_PLAINS = 5, RADIUS_SHEEP_GROUPS = 5, SIZE_SHEEP_GROUPS = 10, 
					SPAWN_CHANGE_GOLD_MINE = 3, SPAWN_CHANGE_IRON_MINE = 3;  
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
	
	private static final Comparator<Entity> entitySorter = new Comparator<Entity>(){

		public int compare(Entity arg0, Entity arg1) {
			int number = (arg0.uniqueNumber - arg1.uniqueNumber);
			if(number > 0) return 1;
			else if(number < 0) return -1;
			return 0;
		}
		
	};
	
	public Map(int mapSize, GameScreen screen){
		
		surface = new Tile[mapSize][mapSize];
		noiseObj = new PerlinNoise2D();
		generateMap(screen);
		this.screen = screen;
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
			addSheepGroup(screen);
		}
		handleEntityMutations();
		for(Projectile a: projectiles){
			a.update();
		}
	}
	
	public synchronized Entity getEntity(int uniqueNumber){
		int high = entities.size() - 1;
		int low = 0;
		
		
		while(true){
			int mid = (high + low) / 2;
			Entity e = entities.get(mid);
			int num = e.uniqueNumber;
			
			if(low == high){
				if(num == uniqueNumber){
					return e;
				}
				return null;
			}
			if(entities.get(high).uniqueNumber == uniqueNumber) return entities.get(high);
			if(entities.get(low).uniqueNumber == uniqueNumber) return entities.get(low);
			if(Util.abs(high - low)  == 1){
				return null;
			}
			
			
			if(num > uniqueNumber){
				high = mid;
			}else if(num < uniqueNumber){
				low = mid;
			}else{
				return e;
			}
		}
	}
	
	public void addSheepGroup(GameScreen screen){
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
								Entity e = (new SheepEntity(this,screen, xPos, yPos));
								e.setOwned(false);
								addEntity(e);
								i++;
							}
						}
					}
					return;
				}
			}
		}
	}
	
	public void addSheepGroup(int x, int y, GameScreen screen){
		if(getEntity(x, y) == null){
			for(int i = 0,control = 0; i < SIZE_SHEEP_GROUPS && control < RADIUS_SHEEP_GROUPS * RADIUS_SHEEP_GROUPS; control++){
				int xPos = x - 2 * RADIUS_SHEEP_GROUPS + (Util.RANDOM.nextInt(RADIUS_SHEEP_GROUPS * 2)) - 2;
				int yPos = y - 2 * RADIUS_SHEEP_GROUPS + (Util.RANDOM.nextInt(RADIUS_SHEEP_GROUPS * 2)) - 2;
				if(xPos > 0 && yPos > 0){
					if(!getTile(xPos, yPos).isSolid()){
						Entity e = (new SheepEntity(this,screen, xPos, yPos));
						e.setOwned(false);
						addEntity(e);
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
	
	public boolean containsEntity(Entity e){
		return entities.contains(e);
	}
	
	public void generateMap(GameScreen screen){
		for(int x = 0; x < getWidth(); x++){
			for(int y = 0; y < getWidth(); y++){
				float noise = noiseObj.perlinNoise(x, y, 0.3f, 32f, 4);
				if(noise > 0) {
					surface[x][y] = Tile.grass1;
					if(noise > 0.6) {
						if(Util.RANDOM.nextInt(TREE_GROW_CHANGE) == 0){
							Entity e = (new TreeStructure(this,screen , x, y, Direction.SOUTH_WEST));
							e.setOwned(false);
							addEntity(e);
						}
						if(Util.RANDOM.nextInt(10000) < SHEEP_SPAWN_CHANGE_IN_FOREST){
							amountSheepGroups++;
							addSheepGroup(x,y, screen);
						}
					}else{
						if(Util.RANDOM.nextInt(10000) < SHEEP_SPAWN_CHANGE_ON_PLAINS && x > RADIUS_SHEEP_GROUPS + 1 && x < getLength() - RADIUS_SHEEP_GROUPS - 1 && y > RADIUS_SHEEP_GROUPS  + 1 && y < getWidth() - RADIUS_SHEEP_GROUPS - 1){
							amountSheepGroups++;
							addSheepGroup(x,y, screen);
						}
					}
					handleEntityMutations();

					if(Util.RANDOM.nextInt(10000) < SPAWN_CHANGE_GOLD_MINE){
						int size = Util.RANDOM.nextInt(3) + 1;
						if(x > size && y > size){
							Entity e = new GoldMine(this,screen, x - size, y - size ,size);
							e.setOwned(false);
							addEntity(e);
						}
					}
					handleEntityMutations();
					if(Util.RANDOM.nextInt(10000) < SPAWN_CHANGE_IRON_MINE){
						int size = Util.RANDOM.nextInt(3) + 1;
						if(x > size && y > size){
							Entity e = new IronMine(this,screen, x - size, y - size ,size);
							e.setOwned(false);
							addEntity(e);
						}
					}
				}
				else if(noise > -0.2f) surface[x][y] = Tile.sand1;
				else surface[x][y] = Tile.water1;
			}
		}
	}
	
	public synchronized void render(Graphics g, Point translation, Dimension screenSize, int screenWidth, int screenHeight){
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
		for(Projectile a:projectiles){
			a.render(g);
		}
		
		g.translate(-translation.x, -translation.y);
	}
	
	public void shootArrow(Entity start,Entity end, boolean fromTop, int horSpeed, int distance){
		if(screen instanceof MPGameScreen){
			return; // client is not allowed to shoot arrows
		}
		if(this instanceof MPMapHost){
			((MPMapHost)this).host.arrowShot(start, end, fromTop, horSpeed, distance);
		}
		shootArrow(start, start.xPos, start.yPos, horSpeed, Util.getDistance(start, end), Util.getDirectionInDegrees(start, end, false), (fromTop)?start.getHeadSpace():1, distance);
	}
	
	private void shootArrow(Entity owner, int xStart, int yStart, double horSpeed, int distance, int direction, int startHeight, int maxDistance){
		if(screen instanceof MPGameScreen){
			return; // client is not allowed to shoot
		}
		addProjectile(new Arrow(this, owner, xStart, yStart, horSpeed, distance, direction, startHeight, maxDistance));
	}
	
	private void shootArrowFromHost(Entity owner, int xStart, int yStart, double horSpeed, int distance, int direction, int startHeight, int maxDistance){
		addProjectile(new Arrow(this, owner, xStart, yStart, horSpeed, distance, direction, startHeight, maxDistance));
	}
	
	public void shootArrowFromHost(Entity start,Entity end, boolean fromTop, int horSpeed, int distance){
		shootArrowFromHost(start, start.xPos, start.yPos, horSpeed, Util.getDistance(start, end), Util.getDirectionInDegrees(start, end, false), (fromTop)?start.getHeadSpace():1, distance);
	}
	
	public boolean isSolid(Point pos){
		return isSolid(pos.x, pos.y);
	}
	
	public synchronized boolean isSolid(int x, int y){
		if(x < 0 || y < 0 || x >= surface.length || y >= surface.length) return true; //outside the map
		if(surface[x][y].isSolid()) return true;
		
		for(Entity e: entities){
			if(e.isSolid(x, y)) {
				return true;
			}
		}
		return false;
	}
	
	public void handleEntityMutations(){
		entities.removeAll(toBeRemoved);
		entities.removeAll(toBeRemovedFromMap);
		notOnMap.addAll(toBeRemovedFromMap);
		toBeRemovedFromMap.clear();
		notOnMap.removeAll(toBeRemoved);
		toBeRemoved.clear();
		if(toBeAdded.size() > 0){
			entities.addAll(toBeAdded);
			Collections.sort(entities, entitySorter);
		}
		toBeAdded.clear();
		projectiles.addAll(projectilesToAdd);
		projectilesToAdd.clear();
		projectiles.removeAll(projectilesToRemove);
		projectilesToRemove.clear();
	}
	
	public void addProjectile(Projectile projectile){
		projectilesToAdd.add(projectile);
	}
	
	public void removeProjectile(Projectile projectile){
		projectilesToRemove.add(projectile);
	}
	
	public ArrayList<Projectile> getProjectiles(){
		return projectiles;
	}
	
	public synchronized Entity getEntity(int x, int y){
		for(Entity e: getEntities()){
			if(e == null) continue;
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
	
	public Entity getEntity(int x, int y, ArrayList<Entity> copyOfEntities){
		for(Entity e: copyOfEntities){
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
	
	public synchronized ArrayList<Entity> getEntities(){
		return entities;
	}
	
	public synchronized ArrayList<Entity> getEntities(int x, int y){
		ArrayList<Entity> result = new ArrayList<Entity>();
		for(Entity e: getEntities()){
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
	
	public synchronized LinkedList<Entity> getEntities(int x1, int y1 , int x2, int y2, Dimension translation){
		/**
		 * IN SCREEN COORDINATS!!
		 */
		LinkedList<Entity> result = new LinkedList<Entity>();
		int xMin = Math.min(x1, x2);
		int yMin = Math.min(y1, y2);
		int xMax = Math.max(x1, x2);
		int yMax = Math.max(y1, y2);
		
		for (Entity e:getEntities() ){
			int x = e.getScreenX() + translation.width;
			int y = e.getScreenY() + translation.height;
			
			if(x < xMax && x > xMin && y < yMax && y > yMin){
				result.add(e);
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
	
	public ArrayList<Entity> getEntities(int x, int y , int radius, Entity ... exept){
		LinkedList<Entity> except = new LinkedList<Entity>();
		ArrayList<Entity> result = new ArrayList<Entity>();
		for (Entity e : exept){
			except.add(e);
		}
		
		for(Entity e: getEntities() ){
			if(Util.abs(Util.getDistance(x, y, e.xPos , e.yPos )) <= radius && !except.contains(e)){
				result.add(e);
			}
		}
		
		
		return result;
	}
	
	public Entity getClosestMine(int x, int y){
		int closestDistance = 999;
		int xe, ye;
		Entity closest = null;
		for(Entity e: getEntities()){
			xe = e.getxPos();
			ye = e.getyPos();
			if(Util.getDistance(x, y, xe, ye) < closestDistance && xe !=x && ye != y && e instanceof MineStructure){
				closest = e;
				closestDistance = Util.getDistance(x, y, xe, ye);
			}
		}
		return closest;
	}
	
	public Entity getClosestMine(int x, int y, ArrayList<Entity> notIncluded, Miner miner){
		int closestDistance = 999;
		int xe, ye;
		Entity closest = null;
		for(Entity e: getEntities()){
			xe = e.getxPos();
			ye = e.getyPos();
			if(Util.getDistance(x, y, xe, ye) < closestDistance && xe !=x && ye != y && (e instanceof MineStructure || e instanceof StoneMine)&& !notIncluded.contains(e) && miner.canIMineIt(e)){
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
		for(Entity e: getEntities()){
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
		for(Entity e: getEntities()){
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
		for(Entity e: getEntities()){
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
		if(!notOnMap.contains(u)) return;
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
		for(Entity e: getEntities()){
			xe = e.getxPos();
			ye = e.getyPos();
			if(Util.getDistance(x, y, xe, ye) < closestDistance && xe !=x && ye != y && e instanceof TreeStructure){
				closest = e;
				closestDistance = Util.getDistance(x, y, xe, ye);
			}
		}
		return closest;
	}

	public Entity getClosestFarm(int x, int y) {
		int closestDistance = 999;
		int xe, ye;
		Entity closest = null;
		for(Entity e: getEntities()){
			xe = e.getxPos();
			ye = e.getyPos();
			if(Util.getDistance(x, y, xe, ye) < closestDistance && xe !=x && ye != y && e instanceof Farm){
				closest = e;
				closestDistance = Util.getDistance(x, y, xe, ye);
			}
		}
		return closest;
	}
	
	public synchronized String getData(){
		String data = getLength() + " " + amountSheepGroups;
	
		for(int x = 0 ;x < getLength(); x++){
			for(int y = 0; y < getWidth(); y++){
				data = data + " " + surface[x][y].getID();
			}
		}
		String entityData = "";
		for(Entity e: getEntities()){
			entityData = entityData + " " + e.getData() + " ";
		}
		
		data = data + " " + getEntities().size() + entityData;
		
		String movements = "";
		int numberOfMovements = 0;
		for(Entity e:entities){
			if(e instanceof MovingEntity && ((MovingEntity)e).isMoving()){
				numberOfMovements++;
				movements = movements + " " + e.uniqueNumber + " " + ((MovingEntity)e).getEndPoint().x + " " + ((MovingEntity)e).getEndPoint().y;
			}
		}
		data = data + " " + numberOfMovements + movements;
		
		return data;
	}
}
