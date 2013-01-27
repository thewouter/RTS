package walnoot.rtsgame.map.entities.players;

import java.awt.Graphics;
import java.util.ArrayList;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.structures.Structure;
import walnoot.rtsgame.popups.entitypopup.EntityOptionsPopup;
import walnoot.rtsgame.popups.entitypopup.Option;
import walnoot.rtsgame.screen.GameScreen;

public class Soldier extends PlayerEntity {
	ArrayList<SoldierComponent> comp = new ArrayList<SoldierComponent>();
	Entity target = null;
	Weapon weapon = null;
	public int shieldProtection;
	ArrayList<SoldierComponent> toRemove = new ArrayList<SoldierComponent>();
	private double progressToNextDamage = 0.0;
	private static int ID = 107;

	public Soldier(Map map, GameScreen screen, int xPos, int yPos, Structure tent) {
		super(map, screen, xPos, yPos, tent);
		super.ID = ID;
	}

	public Soldier(Map map, GameScreen screen, int xPos, int yPos, Structure tent, int health) {
		super(map, screen, xPos, yPos, tent, health);
		super.ID = ID;
	}
	
	public void addSoldierComponent(SoldierComponent comp){
		SoldierComponent remove = null;
		for( SoldierComponent c : this.comp){
			if(c.getClass() == comp.getClass()){
				remove = c;
			}
		}
		
		if(comp instanceof Weapon){
			weapon = (Weapon) comp;
			for(SoldierComponent c : this.comp){
				if(c instanceof Weapon){
					remove = c;
				}
			}
		}
		if(comp instanceof Shield){
			for(SoldierComponent c : this.comp){
				if(c instanceof Shield){
					remove = c;
				}
			}
			shieldProtection = ((Shield)comp).getProtection();
		}
		this.comp.remove(remove);
		this.comp.add(comp);
	}
	
	public void removeSoldierComponent(SoldierComponent comp){
		toRemove.add(comp);
		if(comp instanceof Shield){
			shieldProtection = 0;
		}
	}
	
	public void render(Graphics g){
		super.render(g);
		for(SoldierComponent c:comp){
			c.render(g);
		}
	}
	
	public void renderSelected(Graphics g){
		for(SoldierComponent c:comp){
			c.render(g);
		}
		super.renderSelected(g);
	}
	
	public synchronized void update(){
		super.update();
		for(SoldierComponent c:comp){c.update();}
		if(!map.getEntities().contains(target)) target = null;
		comp.removeAll(toRemove);
	}
	
	public void activate(Entity target){
		this.target = target;
		for(SoldierComponent c: comp){
			c.activate(target);
		}
		follow(target);
	}
	
	public String getName(){
		return name + " the Soldier";
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		if(entityClicked == null) activate(null);
		if(entityClicked != this && entityClicked instanceof MovingEntity){ // attack it!!!
			activate(entityClicked);
			return false;
		}else if(entityClicked != this){
			moveTo(entityClicked);
			return false;
		}
		return false;
	}
	public String getHealthInString(){
		if(shieldProtection != 0) return health + ",  " + shieldProtection + " protection";
		return health + ""; 
	}
	
	public void damage(int damage){
		double damages = (double) damage;
		progressToNextDamage += damages * ((100.0 - shieldProtection * 1.0) / 100.0) * 1.0;
		if(progressToNextDamage >= 1){
			super.damage((int) Math.floor(progressToNextDamage));
			progressToNextDamage -= (int) Math.floor(progressToNextDamage);
		}
	}

	public synchronized ArrayList<SoldierComponent> getComponents() {
		return comp;
	}

	public Weapon getWeapon() {
		return weapon;
	}

}
