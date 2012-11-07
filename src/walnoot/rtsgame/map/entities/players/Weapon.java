package walnoot.rtsgame.map.entities.players;

import java.awt.Graphics;

public abstract class Weapon extends SoldierComponent {

	public Weapon(Soldier owner) {
		super(owner);
		for(SoldierComponent c: owner.comp){
			if(c instanceof Weapon){
				owner.comp.remove(c);
			}
		}
	}

	public void render(Graphics g) {}

	public abstract void update();

	public abstract void activate();
}
