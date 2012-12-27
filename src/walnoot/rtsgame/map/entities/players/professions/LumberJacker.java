package walnoot.rtsgame.map.entities.players.professions;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.structures.natural.TreeStructure;
import walnoot.rtsgame.popups.entitypopup.EntityOptionsPopup;
import walnoot.rtsgame.popups.entitypopup.Option;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.GameScreen;

public class LumberJacker extends Profession {

	private static final int TIME_TO_CHOP_ONE_DAMAGE = 120, ID = 400;
	private boolean isChopping;
	private int teller;
	private TreeStructure closestTree;

	public LumberJacker(PlayerEntity owner) {
		super(owner, ID);
	}

	public void update() {
		if(isChopping)teller++;
		if(teller >= TIME_TO_CHOP_ONE_DAMAGE){
			teller = 0;
			if(isChopping){ 
				if(Util.getDistance(owner.xPos, owner.yPos, closestTree.xPos, closestTree.yPos)  <= 1){
					closestTree.damage(1);
					owner.screen.inventory.wood++;
				}
			}
		}
		if(isChopping && !owner.isMoving() && (closestTree  == null || !owner.map.getEntities().contains(closestTree))){
			moveToNearestTree();
			
		}
	}
	
	public void moveToNearestTree(){
		isChopping = true;
		closestTree = (TreeStructure) owner.map.getClosestTree(owner.getxPos(), owner.getyPos());
		owner.follow(closestTree);
	}

	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input) {
		if(entityClicked != owner) return false;
		EntityOptionsPopup popup = new EntityOptionsPopup(owner, screen);
		if(!isChopping){
			popup.addOption(new Option("start chopping", popup) {
				public void onClick() {
					isChopping = true;
					this.owner.screen.setEntityPopup(null);
				}
			});
		}else{
			popup.addOption(new Option("stop chopping", popup) {
				public void onClick() {
					isChopping = false;
					this.owner.screen.setEntityPopup(null);
				}
			});
		}
		screen.setEntityPopup(popup);
		return true;
	}

	public String getName() {
		return "the LumberJacker";
	}

}
