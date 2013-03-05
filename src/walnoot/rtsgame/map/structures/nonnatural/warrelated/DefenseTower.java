package walnoot.rtsgame.map.structures.nonnatural.warrelated;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.corba.se.impl.oa.toa.TOA;
import com.sun.corba.se.impl.oa.toa.TOAFactory;

import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.Arrow;
import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.entities.players.Bow;
import walnoot.rtsgame.map.entities.players.Soldier;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.multiplayer.host.MPMapHost;
import walnoot.rtsgame.rest.Sound;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.MPGameScreen;

public class DefenseTower extends BasicStructure {
	public static int ID = 212;
	private Soldier guard = null;
	private int HIT_RANGE = 0;
	Entity target = null;
	private int counter = 0; // (:

	public DefenseTower(Map map, GameScreen screen, int xPos, int yPos, Direction front) {
		super(map, screen, xPos, yPos, 4, 0, ID, front);
	}
	
	public DefenseTower(Map map, GameScreen screen, int xPos, int yPos, int health, Direction front) {
		super(map, screen, xPos, yPos, 4, 0, ID, front);
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
		
		if(target != null){
			counter ++;
			if(counter >= guard.getWeapon().LOAD_TIME){
				map.shootArrow(this,xPos - getHeadSpace(), yPos - getHeadSpace() , 500.0, HIT_RANGE, Util.getDirectionInDegrees(this, target, true));
				counter = 0;
			}
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

	public HashMap<String, Integer> getCosts() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 20);
		costs.put("wood", 5);
		costs.put("stone", 25);
		return costs;
	}

	public String getExtraOne() {
		return "0";
	}
}
