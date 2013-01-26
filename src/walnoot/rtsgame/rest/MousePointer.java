package walnoot.rtsgame.rest;

import java.awt.BasicStroke;
import java.awt.Graphics;

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
			
			map.addEntity(e);
			screen.inventory.gold -= e.getCosts();
			afterBuild();
		}
		
		if(input.comma.isTapped()){
			face = Direction.SOUTH_EAST;
		}
		if(input.dot.isTapped()){
			face = Direction.SOUTH_WEST;
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
