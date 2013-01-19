package walnoot.rtsgame.map.structures.nonnatural.warrelated;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;

import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.entities.players.Bow;
import walnoot.rtsgame.map.entities.players.Soldier;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.rest.Sound;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.GameScreen;

public class DefenseTower extends BasicStructure {
	public static int ID = 212;
	private Soldier guard = null;
	private ArrayList<Arrow> arrows = new ArrayList<DefenseTower.Arrow>();
	private ArrayList<Arrow> toRemove = new ArrayList<DefenseTower.Arrow>();
	private int HIT_RANGE = 0;
	Entity target = null;
	private int counter = 0; // (:

	public DefenseTower(Map map, GameScreen screen, int xPos, int yPos) {
		super(map, screen, xPos, yPos, 4, 0, ID);
	}
	
	public DefenseTower(Map map, GameScreen screen, int xPos, int yPos, int health) {
		super(map, screen, xPos, yPos, 4, 0, ID);
		this.health = health;
	}

	public int getHeadSpace() {
		return 2;
	}

	public int getSize() {
		return 1;
	}
	
	public void update() {
		for(int x = -1; x <=1; x++){
			for(int y = -1; y <= 1; y++){
				Entity e = map.getEntity(x + xPos, y + yPos);
				if(guard == null && e != null && e instanceof Soldier && ((Soldier)e).getWeapon() instanceof Bow  && ((MovingEntity)e).entityGoal == this){
					guard = (Soldier)e;
					map.removeEntityFromMap(guard);
					HIT_RANGE = guard.getWeapon().MAX_HIT_RANGE;
					loadImage(5, 0);
				}
			}
		}
		if(target != null && target instanceof MovingEntity && ((MovingEntity)target).isMoving() && Util.getDistance(target, this) > HIT_RANGE){
			target = null;
		}

		if(guard == null) return;
		
		ArrayList<Entity> inRange = map.getEntities(xPos, yPos, HIT_RANGE);
		for(Entity e : inRange){
			if(e instanceof MovingEntity && !e.isOwnedByPlayer()){
				target = e;
			}
		}
		
		if(!map.containsEntity(target))target = null;
		
		Arrow toAdd = null;
		
		if(target != null){
			counter ++;
			if(counter >= guard.getWeapon().LOAD_TIME){
				toAdd = new Arrow(this,xPos - getHeadSpace(), yPos - getHeadSpace() , 500.0, HIT_RANGE, Util.getDirectionInDegrees(this, target, true));
				counter = 0;
			}
		}
		
		for(Arrow a:getArrows()){
			a.update();
		}
		
		if(toAdd != null){
			arrows.add(toAdd);
		}
		arrows.removeAll(toRemove);
		toRemove.clear();
	}
	
	public synchronized ArrayList<Arrow> getArrows(){
		return arrows;
	}
	
	public void render(Graphics g){
		super.render(g);
		for(Arrow a: getArrows()){
			a.render(g);
		}
	}
	
	public void renderSelected(Graphics g){
		int radius = HIT_RANGE - 1;
		g.setColor(new Color(0,0,0,250/2));
		g.drawOval((int) (getScreenX() - radius * Tile.WIDTH + 0.5 * Tile.WIDTH ), (int)(getScreenY() - radius * Tile.HEIGHT + 0.5 * Tile.HEIGHT) , radius * Tile.WIDTH * 2  , radius * Tile.HEIGHT * 2);
		g.setColor(new Color(168,11,0,32));
		g.fillOval((int) (getScreenX() - radius * Tile.WIDTH + 0.5 * Tile.WIDTH ), (int)(getScreenY() - radius * Tile.HEIGHT + 0.5 * Tile.HEIGHT) , radius * Tile.WIDTH * 2  , radius * Tile.HEIGHT * 2);
		super.renderSelected(g);
	}
	
	public void onDestroying() {
		map.removeEntity(guard);
	}

	public int getMaxHealth() {
		return 300;
	}

	public String getName() {
		return "Primary defense Tower";
	}

	public int getCosts() {
		return 300;
	}

	public int getExtraOne() {
		return 0;
	}
	
	public void removeArrow(Arrow arrow) {
		toRemove.add(arrow);
	}
	
	private class Arrow{
		double xScreen = 0;
		double yScreen = 0;
		double horSpeedX;
		double horSpeedY;
		final int TICKS_PER_SECOND = (int)(1000 / RTSComponent.MS_PER_TICK);
		private DefenseTower ownerr;
		int distance = 0;
		int startX, startY;
		
		
		/**
		 * @param xStart 		x position at spawn in map coordinats
		 * @param yStart		y position at spawn in map coordinats
		 * @param horSpeed		horizontal speed in blocks*s^-1
		 * @param vertSpeed		vertical Speed in blocks*s^-1
		 * @param gravity  		gravity in m/s^2
		 * @param direction		direction in degrees
		 */
		public Arrow(DefenseTower ownerr, int xStart, int yStart, double horSpeed, int distance, int direction){
			xScreen = Util.getScreenX(xStart, yStart) + Tile.getWidth() / 2;// 
			yScreen = Util.getScreenY(xStart, yStart) + Tile.getHeight() / 2;// + owner.screen.translationY;

			startX = xStart;
			startY = yStart;
			
			direction %= 360;
			if(direction == 180){
				horSpeedX = 0;
				horSpeedY = horSpeed;
			}else if(direction <=90 ){														// set correct hor. directions
				horSpeedX = (int) (horSpeed * Math.cos((direction * Math.PI) / 180));
				horSpeedY = (int) (horSpeed * Math.sin((direction * Math.PI) / 180));
				horSpeedY *=-1;
			}else if(direction <= 180){
				direction = 180 - direction;
				horSpeedX = (int) (horSpeed * Math.cos((direction * Math.PI) / 180));
				horSpeedY = (int) (horSpeed * Math.sin((direction * Math.PI) / 180));
			}else if(direction <= 270){
				direction = 270 - direction;
				horSpeedX = (int) (horSpeed * Math.cos((direction * Math.PI) / 180));
				horSpeedY = (int) (horSpeed * Math.sin((direction * Math.PI) / 180));
				horSpeedX *=-1;
			}else if(direction <= 360){
				direction = 360 - direction;
				horSpeedX = (int) (horSpeed * Math.cos((direction * Math.PI) / 180));
				horSpeedY = (int) (horSpeed * Math.sin((direction * Math.PI) / 180));
				horSpeedY *=-1;
				horSpeedX *=-1;
			}
			this.distance = distance;
			this.ownerr = ownerr;
			
			new Sound("/res/Sounds/bowshot.mp3").play();
			
		}
		
		public void update(){
			xScreen += horSpeedX / TICKS_PER_SECOND;
			yScreen += horSpeedY / TICKS_PER_SECOND;
			
			if(Util.getDistance(startX, startY, Util.getMapX((int)xScreen, (int)yScreen), Util.getMapY((int)xScreen, (int)yScreen)) >= distance){
				stop();
			}
			
			if(guard.map.getEntities(Util.getMapX((int)xScreen, (int)yScreen), Util.getMapY((int)xScreen, (int)yScreen))!= null){
				for (Entity e:guard.map.getEntities(Util.getMapX((int)xScreen, (int)yScreen), Util.getMapY((int)xScreen, (int)yScreen))){
					if(e == guard || e == ownerr) continue;
					
					e.damage(1);
					stop();
				}
			}
			
		}
		
		public void render(Graphics g){
			g.setColor(Color.BLACK);
			g.drawLine((int)xScreen, (int)yScreen, (int)xScreen + (int)horSpeedX / 64, (int)yScreen + (int)horSpeedY / 64 );
		}
		
		public void stop(){
			ownerr.removeArrow(this);
		}
	}


}
