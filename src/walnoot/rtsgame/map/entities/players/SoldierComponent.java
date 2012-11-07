package walnoot.rtsgame.map.entities.players;

import java.awt.Graphics;

public abstract class SoldierComponent {
	
	public Soldier owner;
	
	public SoldierComponent(Soldier owner) {
		this.owner = owner;
	}
	
	public abstract void render(Graphics g);
	public abstract void update();
	public abstract void activate();
}
