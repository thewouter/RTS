package walnoot.rtsgame.map.entities.players;

import java.awt.Color;
import java.awt.Graphics;

import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.tiles.Tile;

public class AlertComponent extends SoldierComponent {
	private int radius;

	public AlertComponent(Soldier owner, int radius) {
		super(owner);
		this.radius = radius;
	}
	
	public void render(Graphics g){
		if(!owner.screen.isSelected(owner)) return;
		g.setColor(new Color(0,0,0,250/2));
		g.drawOval((int) (owner.getScreenX() - radius * Tile.WIDTH + 0.5 * Tile.WIDTH ), (int)(owner.getScreenY() - radius * Tile.HEIGHT + 0.5 * Tile.HEIGHT) , radius * Tile.WIDTH * 2  , radius * Tile.HEIGHT * 2);
		g.setColor(new Color(168,11,0,32));
		g.fillOval((int) (owner.getScreenX() - radius * Tile.WIDTH + 0.5 * Tile.WIDTH ), (int)(owner.getScreenY() - radius * Tile.HEIGHT + 0.5 * Tile.HEIGHT) , radius * Tile.WIDTH * 2  , radius * Tile.HEIGHT * 2);
	}
	
	public void update(){
		
	}
	
	public void activate(){}
	
	public void setRadius(int radius){
		this.radius = radius;
	}

	public void activate(Entity target) {}

}
