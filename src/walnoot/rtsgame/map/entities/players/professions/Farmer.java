package walnoot.rtsgame.map.entities.players.professions;

import java.awt.Point;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.structures.nonnatural.Farm;
import walnoot.rtsgame.popups.entitypopup.EntityOptionsPopup;
import walnoot.rtsgame.popups.entitypopup.Option;
import walnoot.rtsgame.screen.GameScreen;

public class Farmer extends Profession {
	
	private static final int TIME_TO_MINE_ONE_DAMAGE = 62;
	Farm closestFarm;
	private boolean isMining;
	private int teller = 0;
	private static int ID = 404;
	
	public Farmer(PlayerEntity owner) {
		super(owner, ID);
	}

	public void update() {
		if(isMining) teller ++;
		if(teller >= TIME_TO_MINE_ONE_DAMAGE){
			teller = 0;
			if(isMining){ 
				if(owner.map.getEntity(owner.getxPos() - 1, owner.getyPos() - 1) == closestFarm){
					owner.screen.inventory.addVegetables(1);
				}
			}
		}
		if(isMining && !owner.isMoving() && (owner.map.getEntity(owner.xPos - 1, owner.yPos - 1) != closestFarm || closestFarm == null)){
			moveToNearestFarm();
		}
	}
	
	public void moveToNearestFarm(){
		isMining = true;
		closestFarm = (Farm) owner.map.getClosestFarm(owner.getxPos(), owner.getyPos());
		owner.moveTo(new Point(closestFarm.xPos + closestFarm.getSize(), closestFarm.yPos + closestFarm.getSize()));
		
	}
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		if(entityClicked != owner) return false;
		EntityOptionsPopup popup = new EntityOptionsPopup(owner, screen);
		if(isMining){
			popup.addOption(new Option("stop farming", popup) {
				public void onClick() {
					isMining = false;
					owner.screen.setEntityPopup(null);
					closestFarm = null;
				}
			});
		}else{
			popup.addOption(new Option("start farming", popup) {
				public void onClick() {
					isMining =  true;
					owner.screen.setEntityPopup(null);
				}
			});
		}
		screen.setEntityPopup(popup);	
		
		return true;
	}

	public String getName() {
		return "the Farmer";
	}

}
