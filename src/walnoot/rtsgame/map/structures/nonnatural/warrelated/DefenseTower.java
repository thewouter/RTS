package walnoot.rtsgame.map.structures.nonnatural.warrelated;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.entities.players.Bow;
import walnoot.rtsgame.map.entities.players.Soldier;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.SPGameScreen;

public abstract class DefenseTower extends BasicStructure {
	public static int ID = 212, TICKS_BETWEEN_CHECKS = 30;
	protected Soldier guard = null;
	protected int HIT_RANGE = 0;
	protected Entity target = null;
	protected int counter = 0, checkCounter = 0; // (:

	public DefenseTower(Map map, GameScreen screen, int xPos, int yPos, int textureX, int textureY, int ID, Direction front) {
		super(map, screen, xPos, yPos, textureX, textureY, ID, front);
	}

	public String getExtraOne(){
		if(guard == null) return "0";
		String entityData = guard.getData();
		return (Util.splitString(entityData).size()) + " " + entityData;
	}
	
	public boolean connectsToWall(){
		return true;
	}

	public int getSize() {
		return 1;
	}
	
	public void setGuard(Entity entity){
		if(entity != null && entity instanceof Soldier && ((Soldier)entity).getWeapon() instanceof Bow){
			guard = (Soldier)entity;
			map.removeEntityFromMap(guard);
			HIT_RANGE = guard.getWeapon().MIN_HIT_RANGE;
			loadImage(5, 0);
		}else{
			System.out.println("this isn't a Soldier with a bow!");
		}
	}
	
	public void onDestroying() {
		map.removeEntity(guard);
	}
	
	public void renderSelected(Graphics g){
		int radius = HIT_RANGE - 1;
		g.setColor(new Color(0,0,0,250/2));
		g.drawOval((int) (getScreenX() - radius * Tile.WIDTH + 0.5 * Tile.WIDTH ), (int)(getScreenY() - radius * Tile.HEIGHT + 0.5 * Tile.HEIGHT) , radius * Tile.WIDTH * 2  , radius * Tile.HEIGHT * 2);
		g.setColor(new Color(168,11,0,32));
		g.fillOval((int) (getScreenX() - radius * Tile.WIDTH + 0.5 * Tile.WIDTH ), (int)(getScreenY() - radius * Tile.HEIGHT + 0.5 * Tile.HEIGHT) , radius * Tile.WIDTH * 2  , radius * Tile.HEIGHT * 2);
		super.renderSelected(g);
	}
	
	public void update() {
		if(guard == null){
			for(int x = -1; x <=1; x++){
				for(int y = -1; y <= 1; y++){
					Entity e = map.getEntity(x + xPos, y + yPos);
					if(e != null && e instanceof Soldier && ((Soldier)e).getWeapon() instanceof Bow  && ((MovingEntity)e).entityGoal == this){
						guard = (Soldier)e;
						map.removeEntityFromMap(guard);
						HIT_RANGE = guard.getWeapon().MIN_HIT_RANGE;
						loadImage(5, 0);
					}
				}
			}
		}

		if(guard == null) return;
		
		if(target != null && target instanceof MovingEntity && ((MovingEntity)target).isMoving() && Util.getDistance(target, this) > HIT_RANGE){
			target = null;
		}
		
		checkCounter++;
		if(checkCounter >= TICKS_BETWEEN_CHECKS){
			checkCounter = 0;
			ArrayList<Entity> inRange = map.getEntities(xPos, yPos, HIT_RANGE);
			for(Entity e : inRange){
				if(e instanceof MovingEntity){
					if (screen instanceof SPGameScreen && !e.isOwnedByPlayer()){
						target = e;
					}else if(e.owner != owner){
						target = e;
					}
				}
			}
		}
		
		if(!map.containsEntity(target))target = null;
		
		if(target != null){
			counter ++;
			if(counter >= guard.getWeapon().LOAD_TIME){
				map.shootArrow(this,target, true, 15, HIT_RANGE);
				counter = 0;
			}
		}
	}

}
