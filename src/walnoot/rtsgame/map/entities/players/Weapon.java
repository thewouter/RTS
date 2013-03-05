package walnoot.rtsgame.map.entities.players;

import java.awt.Graphics;

import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.rest.Util;

public abstract class Weapon extends SoldierComponent {
	boolean isFighting = false;
	public int MIN_HIT_RANGE = 0, MAX_HIT_RANGE = 1, LOAD_TIME = 0; //standard..  TODO place in constructor
	int ticksCounter = 0;

	public Weapon(Soldier owner, int ID) {
		super(owner, ID);
		for(SoldierComponent c: owner.getComponents()){
			if(c instanceof Weapon){
				owner.comp.remove(c);
			}
		}
	}

	public void render(Graphics g) {}
	public void renderSelected(Graphics g){}

	public void update(){
		if(isFighting){
			ticksCounter++;
			if(ticksCounter >= LOAD_TIME && owner.target != null && Util.getDistance(owner.target, owner) <= MIN_HIT_RANGE){
				owner.standStill();
				activate();
				ticksCounter = 0;
			}else if(owner.target == null){
				isFighting = false;
			}else if(Util.getDistance(owner.target, owner) > MIN_HIT_RANGE){
				owner.follow(owner.target);
			}
		}
	}

	public abstract void activate();
	
	public void activate(Entity target){
		isFighting = true;
		System.out.println("activate");
	}
	
	public void setTarget(Entity t){
		isFighting = true;
	}
}
