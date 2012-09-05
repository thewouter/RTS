package walnoot.rtsgame.map.entities;

import java.awt.Point;
import java.util.LinkedList;

import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.structures.nonnatural.LumberJacker;
import walnoot.rtsgame.multiplayer.client.MPMapClient;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.SPGameScreen;

public abstract class MovingEntity extends Entity {
	protected double timeTraveled; //hoelang hij onderweg is
	private Entity goal = null;
	private int oldX = 0, oldY = 0;
	
	public LinkedList<Direction> nextDirections = new LinkedList<Direction>();
	private LinkedList<Direction> nextNextDirections = null;
	
	public MovingEntity(Map map, GameScreen screen, int xPos, int yPos,  int ID){
		super(map, xPos, yPos, ID, screen);
	}
	
	public void update(){
		if(nextNextDirections != null){
			nextDirections = nextNextDirections;
		}else if(nextDirections == null){
			nextDirections = new LinkedList<Direction>();
		}
		
		Direction nextDirection = null;
		if(nextDirections.isEmpty()){
			if(goal != null){
				int dx = goal.xPos - this.xPos;
				int dy = goal.yPos - this.yPos;

				if(Util.abs(dx) <= 1 && Util.abs(dy) <= 1) return;

				if(dx > 1) dx = 1;
				if(dx < -1) dx = -1;

				if(dy > 1) dy = 1;
				if(dy < -1) dy = -1;

				nextDirections.add(Direction.getDirection(dx, dy));
			}else return;
		}
		
		if(!nextDirections.isEmpty()) {
			nextDirection = nextDirections.get(0);
			timeTraveled += RTSComponent.MS_PER_TICK / (getTravelTime() * (nextDirection.isDiagonal() ? Math.sqrt(2) : 1.0));
		}
		
		
		while(timeTraveled > 1){
			timeTraveled -= 1;
			
			if(nextDirections.size() == 1){
				timeTraveled = 0;
				onStopMoving();
			}
			
			xPos += nextDirection.getxOffset();
			yPos += nextDirection.getyOffset();
			
			if(!nextDirections.isEmpty()) nextDirections.remove(0);
		}
	}
	
	public void setNextDirections(LinkedList<Direction> toSet){
		nextNextDirections = toSet;
	}
	
	protected void onStopMoving(){
	
	}
	
	public void follow(Entity e){
		goal = e;
	}
	
	public void buildMenu(){}
	
	public Direction getNextDirection(){
		if(!nextDirections.isEmpty()) return nextDirections.get(0);
		else return null;
	}
	
	public int getCosts(){
		return 0;
	}

	public void moveTo(Point goal){
		if(map instanceof MPMapClient){
			return;
		}
		this.goal = null;
		if(this instanceof PlayerEntity && map.getEntity(goal.x, goal.y) instanceof LumberJacker){
			goal.x--;
			goal.y--;
		}
		Pathfinder.moveTo(this,new Point(xPos, yPos), goal, map);
	}

	public void moveToFromHost(Point goal){
		this.goal = null;
		Pathfinder.moveTo(this,new Point(xPos, yPos), goal, map);
	}
	
	public boolean isMoving(){
		if(nextDirections == null)return false;
		return !nextDirections.isEmpty();
	}
	
	public int getScreenX(){
		int x = super.getScreenX();
		
		Direction direction = null;
		if(nextDirections != null && !nextDirections.isEmpty()) direction = nextDirections.get(0);
		
		if(direction != null) x += Math.round(direction.getPointOnScreen().x * timeTraveled);
		
		return x;
	}
	
	public int getScreenY(){
		int y = super.getScreenY();
		
		Direction direction = null;
		if(nextDirections != null && !nextDirections.isEmpty()) direction = nextDirections.get(0);
		
		if(direction != null) y += Math.round(direction.getPointOnScreen().y * timeTraveled);
		
		return y;
	}
	
	public void moveRandomLocation(int WALK_RANGE){
		int x, y;
		do{
			x = xPos + Util.RANDOM.nextInt(WALK_RANGE * 2) - WALK_RANGE;
			y = yPos + Util.RANDOM.nextInt(WALK_RANGE * 2) - WALK_RANGE;
		}while(map.isSolid(x, y) && x != xPos && y != yPos);
	
		moveTo(new Point(x, y));
	}
	
	public Entity getGoal(){
		return goal;
	}
	
	/** @return tijd die het duurt om over 1 tile te bewegen */
	protected abstract double getTravelTime();

	public abstract int getSelectedOption() ;

	public abstract void setSelectedOption(int indexSelected) ;
	
	public abstract boolean isMovable();
}
