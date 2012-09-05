package walnoot.rtsgame.map.entities.players;

import java.awt.Point;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.structures.natural.TreeStructure;
import walnoot.rtsgame.popups.entitypopup.EntityOptionsPopup;
import walnoot.rtsgame.popups.entitypopup.Option;
import walnoot.rtsgame.screen.GameScreen;

public class LumberJackerPlayer extends PlayerEntity {
	
	private static int TIME_TO_CHOP_ONE_DAMAGE = 120;
	public static int ID = 106;
	public boolean isChopping = false;
	private TreeStructure closestTree;
	private int teller = 0;

	public LumberJackerPlayer(Map map, GameScreen screen, int xPos, int yPos) {
		super(map, screen, xPos, yPos);
		setID(ID);
	}
	
	public void update(){
		super.update();
		if(isChopping)teller++;
		if(teller >= TIME_TO_CHOP_ONE_DAMAGE){
			teller = 0;
			if(isChopping){ 
				if(map.getEntity(getxPos() - 1, getyPos() - 1) instanceof TreeStructure){
					map.getEntity(getxPos() -1,  getyPos() - 1).damage(1);
					screen.inventory.wood++;
				}
			}
		}
		if(isChopping && !isMoving() && (map.getEntity(xPos - 1, yPos - 1) != closestTree || closestTree  == null)){
			moveToNearestTree();
			
		}
	}
	
	public void moveToNearestTree(){
		isChopping = true;
		closestTree = (TreeStructure) map.getClosestTree(getxPos(), getyPos());
		moveTo(new Point(closestTree.xPos + closestTree.getSize(), closestTree.yPos + closestTree.getSize()));
		
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		if(entityClicked != this) return false;
		EntityOptionsPopup popup = new EntityOptionsPopup(this, screen);
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
		return false;
	}
	
	public String getName(){
		return name  + " the LumberJacker";
	}

}
