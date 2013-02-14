package walnoot.rtsgame.map.entities.players.professions;

import java.awt.Point;
import java.util.ArrayList;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.Pathfinder;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.structures.natural.GoldMine;
import walnoot.rtsgame.map.structures.natural.IronMine;
import walnoot.rtsgame.map.structures.natural.MineStructure;
import walnoot.rtsgame.map.structures.natural.StoneMine;
import walnoot.rtsgame.popups.entitypopup.EntityOptionsPopup;
import walnoot.rtsgame.popups.entitypopup.Option;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.GameScreen;

public class Miner extends Profession {

	private static final int TIME_TO_MINE_ONE_DAMAGE = 120, ID = 401;
	private boolean isMining;
	private int teller;
	private MineStructure closestMine;
	public final int level;
	private boolean minesLeft = true, isCalculating = false;

	public Miner(PlayerEntity owner, int i) {
		super(owner, ID);
		level = i;
	}

	public void update() {
		if(isMining) teller++;
		if(teller >= TIME_TO_MINE_ONE_DAMAGE){
			teller = 0;
			if(isMining){ 
				if(owner.map.getEntity(owner.getxPos() - 1, owner.getyPos() - 1) == closestMine){
					closestMine.mine(1, owner);
				}
			}
		}
		if(!isCalculating && minesLeft && isMining && !owner.isMoving() && (owner.map.getEntity(owner.xPos - 1, owner.yPos - 1) != closestMine || closestMine == null)){
			moveToNearestMine();
		}
	}
	
	public void moveToNearestMine(){
		isCalculating = true;
		isMining = true;
		ArrayList<Entity> notValid = new ArrayList<Entity>();
		closestMine = (MineStructure) owner.map.getClosestMine(owner.getxPos(), owner.getyPos(), notValid, this);
		if(closestMine != null){
			owner.moveTo(new Point(closestMine.xPos + closestMine.getSize(), closestMine.yPos + closestMine.getSize()));
		}else{
			isMining = false;
		}
	}
	
	public void walkingCalculated(){
		isCalculating = false;
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
		screen.setEntityPopup(popup);	
		
		return true;
	}
	
	public boolean canIMineIt(Object o){
		if(o instanceof MineStructure){
			if(o instanceof StoneMine)return true;
			if(level <= 1) return false;
			if(o instanceof GoldMine) return true;
			if(level <= 2) return false;
			if(o instanceof IronMine) return true;
			
		}
		return false;
		
	}

	public String getName() {
		switch (level) {
		case 1:
			return "the MinerI";
		case 2:
			return "the MinerII";
		default:
			return "the Miner";
		}
	}

}
