package walnoot.rtsgame.map.entities.players;

import java.awt.Graphics;

import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.rest.Util;

public abstract class Weapon extends SoldierComponent {
	boolean isFighting = true;
	public int MIN_HIT_RANGE = 0, MAX_HIT_RANGE = 1, LOAD_TIME = 0; //standard..  TODO place in constructor
	int ticksCounter = 0;

	public Weapon(Soldier owner) {
		super(owner);
		for(SoldierComponent c: owner.comp){
			if(c instanceof Weapon){
				owner.comp.remove(c);
			}
		}
	}

	public void render(Graphics g) {}

	public void update(){
		ticksCounter++;
		if(isFighting && ticksCounter >= LOAD_TIME && owner.target != null){
			ticksCounter = 0;
			if(Util.getDistance(owner.target, owner) <= MAX_HIT_RANGE){
				owner.moveTo(owner);
				activate();
			}else{
				owner.follow(owner.target);
			}
		}
	}

	public abstract void activate();
	
	public void setTarget(Entity t){
		isFighting = true;
	}
}
