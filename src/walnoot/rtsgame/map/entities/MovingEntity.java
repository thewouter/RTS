package walnoot.rtsgame.map.entities;

import java.awt.Point;
import java.util.LinkedList;

import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.Util;
import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.tribes.Tribe;

public abstract class MovingEntity extends Entity {
	protected double timeTraveled; //hoelang hij onderweg is
	private Entity goal = null;
	
	private LinkedList<Direction> nextDirections = new LinkedList<Direction>();
	
	public MovingEntity(Map map, int xPos, int yPos, Tribe tribe){
		super(map, xPos, yPos, tribe);
	}
	
	public void update(){
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
		
		nextDirection = nextDirections.get(0);
		
		timeTraveled += RTSComponent.MS_PER_TICK / (getTravelTime() * (nextDirection.isDiagonal() ? Math.sqrt(2) : 1.0));
		
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
	
	protected void onStopMoving(){
	}
	
	public void follow(Entity e){
		goal = e;
	}
	
	public Direction getNextDirection(){
		if(!nextDirections.isEmpty()) return nextDirections.get(0);
		else return null;
	}

	public void moveTo(Point goal){
		this.goal = null;
		
		LinkedList<Direction> path = Pathfinder.moveTo(new Point(xPos, yPos), goal, map);
		if(path != null) nextDirections = path;
	}
	
	public boolean isMoving(){
		return !nextDirections.isEmpty();
	}
	
	public int getScreenX(){
		int x = super.getScreenX();
		
		Direction direction = null;
		if(!nextDirections.isEmpty()) direction = nextDirections.get(0);
		
		if(direction != null) x += Math.round(direction.getPointOnScreen().x * timeTraveled);
		
		return x;
	}
	
	public int getScreenY(){
		int y = super.getScreenY();
		
		Direction direction = null;
		if(!nextDirections.isEmpty()) direction = nextDirections.get(0);
		
		if(direction != null) y += Math.round(direction.getPointOnScreen().y * timeTraveled);
		
		return y;
	}
	
	public void moveRandomLocation(int WALK_RANGE){
		int x, y;
		do{
			x = xPos + Util.RANDOM.nextInt(WALK_RANGE * 2) - WALK_RANGE;
			y = yPos + Util.RANDOM.nextInt(WALK_RANGE * 2) - WALK_RANGE;
		}while(map.isSolid(x, y));
	
		moveTo(new Point(x, y));
	}
	
	public Entity getGoal(){
		return goal;
	}
	
	/** @return tijd die het duurt om over 1 tile te bewegen */
	protected abstract double getTravelTime();

	public abstract int getSelectedOption() ;

	public abstract void setSelectedOption(int indexSelected) ;
}
