package walnoot.rtsgame.map.entities.players;

import java.awt.Point;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.structures.Structure;
import walnoot.rtsgame.map.structures.natural.MineStructure;
import walnoot.rtsgame.popups.entitypopup.EntityOptionsPopup;
import walnoot.rtsgame.popups.entitypopup.Option;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.GameScreen;

public class MinerEntity extends PlayerEntity {
	private boolean isMining = false;
	private MineStructure closestMine = null;
	public static int ID = 105, TIME_TO_MINE_ONE_DAMAGE = 10;
	private int teller = 0;

	public MinerEntity(Map map, GameScreen screen, int xPos, int yPos, Structure owner) {
		super(map, screen, xPos, yPos, owner);
		name = Util.NAME_GEN.getRandomName() + " the miner";
		setID(ID);
	}
	
	public MinerEntity(Map map, GameScreen screen, int xPos, int yPos, Structure owner, int health) {
		super(map,screen, xPos, yPos, owner);
		name = Util.NAME_GEN.getRandomName() + " the Miner";
		this.health = health;
		setID(ID);
	}
	
	public int getMaxHealth(){
		return 7;
	}
	
	public void update(){
		super.update();
		if(isMining)teller++;
		if(teller >= TIME_TO_MINE_ONE_DAMAGE){
			teller = 0;
			if(isMining){ 
				if(map.getEntity(getxPos() - 1, getyPos() - 1) instanceof MineStructure){
					((MineStructure)map.getEntity(getxPos() -1,  getyPos() - 1)).mine(1);
				}
			}
		}
		if(isMining && !isMoving() && (map.getEntity(xPos - 1, yPos - 1) != closestMine || closestMine == null)){
			moveToNearestMine();
		}
	}
	
	public void moveToNearestMine(){
		isMining = true;
		closestMine = (MineStructure) map.getClosestMine(getxPos(), getyPos());
		moveTo(new Point(closestMine.xPos + closestMine.getSize(), closestMine.yPos + closestMine.getSize()));
		
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		if(entityClicked != this) return false;
		EntityOptionsPopup popup = new EntityOptionsPopup(this, screen);
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
		
		return false;
	}
	
	public int getID(){
		return ID;
	}

}
