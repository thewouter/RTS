package walnoot.rtsgame.rest;

import java.awt.Graphics;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.map.structures.Structure;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.SPGameScreen;

public abstract class MousePointer {
	public Map map;
	public InputHandler input;
	public GameScreen screen;
	public int x = 0, y = 0;
	
	public MousePointer(Map map, InputHandler input, GameScreen screen){
		this.map = map;
		this.input = input;
		this.screen = screen;
	}
	
	public void update(){
		x = input.getMouseX();
		y = input.getMouseY();
		if(input.LMBTapped() && screen.isOnlyOnMap(x, y)){
			Entity e = toBuild();
			screen.map.addEntity(e);
			screen.inventory.gold -= e.getCosts();
			afterBuild();
		}
	}
	
	public abstract Entity toBuild();
	
	public void render(Graphics g){
		if(!(toBuild() instanceof Structure && !(((Structure) toBuild()) instanceof BasicStructure))) g.fillRect(x, y, 5, 5);
		else ((BasicStructure) toBuild()).render(g, x, y);
	}
	
	public void afterBuild(){} // can be overwritten in some cases
}
