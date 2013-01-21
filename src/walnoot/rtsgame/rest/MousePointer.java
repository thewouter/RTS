package walnoot.rtsgame.rest;

import java.awt.BasicStroke;
import java.awt.Graphics;

import walnoot.rtsgame.InputHandler;
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
	
	public MousePointer(Map map, InputHandler input, GameScreen screen){
		this.map = map;
		this.input = input;
		this.screen = screen;
		entity = toBuild();
	}
	
	public void update(){
		x = input.getMouseX();
		y = input.getMouseY();
		if(input.LMBTapped() && screen.isOnlyOnMap(x, y)){
			Entity e = toBuild();
			if(screen == null) System.out.println("screen");
			if(map == null) System.out.println("map");
			if(e == null) System.out.println("entity");
			
			map.addEntity(e);
			screen.inventory.gold -= e.getCosts();
			afterBuild();
		}
	}
	
	public abstract Entity toBuild();
	
	public void render(Graphics g){
		if(entity instanceof BasicStructure){
			g.drawImage(((BasicStructure)entity).getImage(), x - (Tile.WIDTH / 2) * (((BasicStructure)entity).getSize() - 1) - Tile.getWidth() / 2, y - ((entity.getHeadSpace() )* Tile.HEIGHT) + (Tile.HEIGHT / 2) * (((BasicStructure)entity).getSize() - 1) - Tile.getHeight() / 2, null);
		}
	}
	
	public void afterBuild(){} // can be overwritten in some cases
}
