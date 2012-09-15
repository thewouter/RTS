package walnoot.rtsgame.map.entities.players.professions;

import java.awt.Point;
import java.util.ArrayList;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.structures.Structure;
import walnoot.rtsgame.map.structures.natural.GoldMine;
import walnoot.rtsgame.map.structures.natural.MineStructure;
import walnoot.rtsgame.map.structures.nonnatural.StoneMine;
import walnoot.rtsgame.popups.entitypopup.EntityOptionsPopup;
import walnoot.rtsgame.popups.entitypopup.Option;
import walnoot.rtsgame.screen.GameScreen;

public class Miner extends Profession {

	private static final int TIME_TO_MINE_ONE_DAMAGE = 120;
	private boolean isMining;
	private int teller;
	private Structure closestMine;
	public final int level;

	public Miner(PlayerEntity owner, int i) {
		super(owner);
		level = i;
	}

	public void update() {
		if(isMining)teller++;
		if(teller >= TIME_TO_MINE_ONE_DAMAGE){
			teller = 0;
			if(isMining){ 
				if(owner.map.getEntity(owner.getxPos() - 1, owner.getyPos() - 1) instanceof MineStructure){
					((MineStructure)owner.map.getEntity(owner.getxPos() -1,  owner.getyPos() - 1)).mine(1, owner);
				}
			}
		}
		if(isMining && !owner.isMoving() && (owner.map.getEntity(owner.xPos - 1, owner.yPos - 1) != closestMine || closestMine == null)){
			moveToNearestMine();
		}
	}
	
	public void moveToNearestMine(){
		isMining = true;
		ArrayList<Entity> notValid = new ArrayList<Entity>();
		do{
			//System.out.println(notValid.get(notValid.size()-1));
			closestMine = (Structure) owner.map.getClosestMine(owner.getxPos(), owner.getyPos(), notValid);
			if(canIMineIt(closestMine))break;
			notValid.add(closestMine);
		}while(true);
		owner.moveTo(new Point(closestMine.xPos + closestMine.getSize(), closestMine.yPos + closestMine.getSize()));
		
	}
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		if(entityClicked != owner) return false;
		EntityOptionsPopup popup = new EntityOptionsPopup(owner, screen);
		if(isMining){
			popup.addOption(new Option("stop mining", popup) {
				public void onClick() {
					isMining = false;
					owner.screen.setEntityPopup(null);
					closestMine = null;
				}
			});
		}else{
			popup.addOption(new Option("start mining", popup) {
				public void onClick() {
					isMining =  true;
					owner.screen.setEntityPopup(null);
				}
			});
		}
		popup.addOption(new Option("" + level,popup) {
			
			public void onClick() {
			}
		});
		screen.setEntityPopup(popup);	
		
		return false;
	}
	
	private boolean canIMineIt(Object o){
		if(o instanceof MineStructure){
			if(o instanceof StoneMine)return true;
			if(level <= 1) return false;
			if(o instanceof GoldMine) return true;
			
		}
		return false;
		
	}

	public String getName() {
		return null;
	}

}
