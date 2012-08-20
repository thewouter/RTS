package walnoot.rtsgame.map.entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.tiles.Tile;

/** tekst en uitleg op http://www.policyalmanac.org/games/aStarTutorial.htm */

public class Pathfinder extends Thread {
	private static Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
	private final Point goal;
	private final Point start;
	private final MovingEntity requester;
	private final Tile[][] currentMap;
	
	private Node finalNode;
	
	public static void moveTo(MovingEntity requester, Point start, Point goal, Map map){
		if(map.isSolid(goal)){
			System.out.println("Goal is unreachable!");
			requester.nextDirections = null;
		}
		
		new Pathfinder(requester, start, goal, map.getSurface()).start();
	}
	
	private Pathfinder(MovingEntity requester ,Point start, Point goal, Tile[][] currentMap){
		this.start = start;
		this.goal = goal;
		this.currentMap = currentMap;
		this.requester = requester;
	}
	
	public void run(){
		requester.setNextDirections(getPath());
	}
	
	private LinkedList<Direction> getPath(){
		LinkedList<Direction> result = new LinkedList<Direction>();
		
		ArrayList<Node> openlist = new ArrayList<Node>();
		ArrayList<Node> closedlist = new ArrayList<Node>();
		
		Node firstNode = new Node(null, start.x, start.y);
		openlist.add(firstNode);
		long startTimeNano = System.nanoTime();
		while(!openlist.isEmpty() && finalNode == null){
			getNodeLowestF(openlist).checkNeighbours(currentMap, openlist, closedlist);
			if(System.nanoTime() - startTimeNano > 1000000000){
				System.out.println("took too long to calculate");
				return result;
			}
		}
		
		try{
		Node node = finalNode;
		while(true){
			Direction dir = node.getDirection();
			if(dir != null)
				result.addFirst(dir);
			
			if(node.parent != null){
				node = node.parent;
			}else break;
		}
		}catch(Exception e){
			return null;
		}
		
		return result;
	}
	
	/** @return Node with lowest F value */
	private Node getNodeLowestF(ArrayList<Node> openlist){
		Node lowestF = openlist.get(0);
		
		for(Node n: openlist){
			if(n.getF() < lowestF.getF()) lowestF = n;
		}
		
		return lowestF;
	}
	
	private Node getEqualNode(ArrayList<Node> list, Node node){
		for(Node n: list){
			if(n.getXPos() == node.getXPos() && n.getYPos() == node.getYPos()) return n;
		}
		
		return null;
	}
	
	private class Node {
		int g, h;
		Node parent;
		//private Point position;
		private int posX, posY;
		
		public Node(Node parent, int x, int y){
			this.parent = parent;
			posX = x;
			posY = y;
			
			calculateG();
			calculateH();
		}
		
		public int getF(){
			calculateG();
			calculateH();
			
			return g + h;
		}
		
		public void calculateG(){
			int parentG = 0;
			if(parent != null){
				parentG += parent.g;
				parentG += (isDiagonal() ? 14 : 10);
			}
			g = parentG;
		}
		
		public boolean isDiagonal(){
			int dx = posX - parent.posX;
			int dy = posY - parent.posY;
			
			if(dx == 0 || dy == 0) return false;
			return true;
		}
		
		public Direction getDirection(){
			if(parent == null) return null;
			return Direction.getDirection(posX - parent.posX, posY - parent.posY);
		}
		
		public void calculateH(){
			int dx = Math.abs(getXPos() - goal.x);
			int dy = Math.abs(getYPos() - goal.y);
			
			h = 10 * (dx + dy);
		}
		
		public int getXPos(){
			return posX;
		}
		
		public int getYPos(){
			return posY;
		}
		
		public void checkNeighbours(Tile[][] surface, ArrayList<Node> openlist, ArrayList<Node> closedlist){
			openlist.remove(this);
			closedlist.add(this);
			
			if(posX == goal.x && posY == goal.y){
				finalNode = this;
				return;
			}
			
			for(Direction dir: directions){
				Point newPosition = dir.nextPoint(posX, posY);
				if(newPosition.x < 0 || newPosition.y < 0 || newPosition.x >= currentMap.length || newPosition.y >= currentMap[0].length) break;
				if(!currentMap[newPosition.x][newPosition.y].isSolid()){
					/*
					 * if(newPosition.equals(goal)) { finalNode = new Node(this,
					 * newPosition); break; }
					 */

					Node newNode = new Node(this, newPosition.x, newPosition.y);
					
					//if(getEqualNode(closedlist, newNode) != null) continue;
					
					Node openNode = getEqualNode(openlist, newNode);
					if(openNode != null){
						if(openNode.g > newNode.g){
							openNode.parent = this;
							openNode.calculateG();
							openNode.calculateH();
						}
					}else{
						openlist.add(new Node(this, newPosition.x, newPosition.y));
					}
				}
			}
		}
	}
}
