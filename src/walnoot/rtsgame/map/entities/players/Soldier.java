package walnoot.rtsgame.map.entities.players;

import java.awt.Graphics;
import java.util.ArrayList;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.structures.Structure;
import walnoot.rtsgame.popups.entitypopup.EntityOptionsPopup;
import walnoot.rtsgame.popups.entitypopup.EntityPopup;
import walnoot.rtsgame.popups.entitypopup.Option;
import walnoot.rtsgame.screen.GameScreen;

public class Soldier extends PlayerEntity {
	ArrayList<SoldierComponent> comp = new ArrayList<SoldierComponent>();
	PlayerEntity target = null;
	Weapon weapon = null;

	public Soldier(Map map, GameScreen screen, int xPos, int yPos,
			Structure tent) {
		super(map, screen, xPos, yPos, tent);
	}

	public Soldier(Map map, GameScreen screen, int xPos, int yPos,
			Structure tent, int health) {
		super(map, screen, xPos, yPos, tent, health);
	}
	
	public void addSoldierComponent(SoldierComponent comp){
		this.comp.add(comp);
	}
	
	public void render(Graphics g){
		super.render(g);
		for(SoldierComponent c:comp){c.render(g);}
	}
	
	public void update(){
		super.update();
		for(SoldierComponent c:comp){c.update();}
		if(!map.entities.contains(target)) target = null;
	}
	
	public void activate(PlayerEntity target){
		for (SoldierComponent c:comp){
			c.activate();
		}
		this.target = target;
	}
	
	public String getName(){
		return name + " the Soldier";
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		if(entityClicked != this) return false;
		EntityOptionsPopup popup = new EntityOptionsPopup(this, screen);
		popup.addOption(new Option("set bow", popup) {
			public void onClick() {
				((Soldier)owner.owner).addSoldierComponent(new Bow(((Soldier)owner.owner)));
			}
		});
		
		popup.addOption(new Option("activate", popup) {
			
			public void onClick() {
				for(SoldierComponent e:((Soldier)owner.owner).comp){
					e.activate();
				}
			}
		});
		screen.setEntityPopup(popup);
		return false;
	}

}
