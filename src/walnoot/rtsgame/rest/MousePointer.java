package walnoot.rtsgame.rest;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.screen.GameScreen;

public abstract class MousePointer {
	public Map map;
	public InputHandler input;
	public GameScreen screen;
	public int x = 0, y = 0;
	private Entity entity;
	private Direction face = Direction.SOUTH_WEST;
	
	public MousePointer(Map map, InputHandler input, GameScreen screen){
		this.map = map;
		this.input = input;
		this.screen = screen;
		entity = toBuild(face);
	}
	
	public void update(){
		x = input.getMouseX();
		y = input.getMouseY();
		if(input.LMBTapped() && screen.isOnlyOnMap(x, y)){
			Entity e = toBuild(face);
			
			HashMap<String, Integer> costs;
			costs= e.getCosts();
			Set s = costs.entrySet();
			Iterator it = s.iterator();
			ArrayList<String> keysDone = new ArrayList<>();
			while(it.hasNext()){
				Entry en = (Entry)it.next();
				String material = (String)en.getKey();
				int cost = (int) en.getValue();
				if(!handleCosts(material, cost)){ // not enough materials detected!
					for(String key :keysDone){
						handleCosts(key, -cost);
					}
					return;
				}else{
					keysDone.add(material);
				}
			}
			map.addEntity(e);
			
			afterBuild();
		}
		
		if(input.comma.isTapped()){
			face = Direction.SOUTH_EAST;
		}
		if(input.dot.isTapped()){
			face = Direction.SOUTH_WEST;
		}
	}
	
	private boolean handleCosts(String material, int amount){
		switch(material){
		case "gold":
			if(screen.inventory.gold >= amount){
				screen.inventory.gold -= amount;
				return true;
			}
			return false;
		case "wood":
			if(screen.inventory.wood >= amount){
				screen.inventory.wood -= amount;
				return true;
			}
			return false;
		case "meat":
			if(screen.inventory.meat >= amount){
				screen.inventory.meat -= amount;
				return true;
			}
			return false;
		case "vegatables":
			if(screen.inventory.vegetables >= amount){
				screen.inventory.vegetables -= amount;
				return true;
			}
			return false;
		case "stone":
			if(screen.inventory.stone >= amount){
				screen.inventory.stone -= amount;
				return true;
			}
			return false;
		default:
			System.out.println("unknown material: " + material);
			return false;
			
			
		}
	}
	
	public abstract Entity toBuild(Direction face);
	
	public void render(Graphics g){
		if(entity instanceof BasicStructure){
			int x = this.x - (Tile.WIDTH / 2) * (((BasicStructure)entity).getSize() - 1) - Tile.getWidth() / 2;
			int y = this.y - ((entity.getHeadSpace() )* Tile.HEIGHT) + (Tile.HEIGHT / 2) * (((BasicStructure)entity).getSize() - 1) - Tile.getHeight() / 2;
			int width = ((BasicStructure)entity).getImage().getWidth(null);
			int height = ((BasicStructure)entity).getImage().getHeight(null);
			
			if(face == Direction.SOUTH_WEST) g.drawImage(((BasicStructure)entity).getImage(), x, y, null);
			else g.drawImage(((BasicStructure)entity).getImage(), x + width, y, x, y + height, 0, 0, width, height, null);
		}
	}
	
	public void afterBuild(){} // can be overwritten in some cases
}
