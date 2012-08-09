package walnoot.rtsgame.map.entities.players;

import java.awt.Point;
import java.util.LinkedList;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.Util;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.map.structures.MineStructure;
import walnoot.rtsgame.popups.entitypopup.EntityOptionsPopup;
import walnoot.rtsgame.popups.entitypopup.Option;
import walnoot.rtsgame.screen.GameScreen;

public class HunterEntity extends PlayerEntity {
	public boolean isHunting = false;
	public static int ID = 104, TIME_TO_HUNT_ONE_DAMAGE = 5;
	private int teller = 0;
	private MovingEntity closestMovingEntity;

	public HunterEntity(Map map, int xPos, int yPos, int health) {
		super(map, xPos, yPos, health);
		
	}
	public HunterEntity(Map map, int xPos, int yPos){
		super(map, xPos, yPos);
		
	}

	public void update(){
		super.update();
		if(isHunting)teller++;
		if(teller >= TIME_TO_HUNT_ONE_DAMAGE){		// hunting time !
			teller = 0;
			if(Util.getDistance(this.xPos, this.yPos, closestMovingEntity.xPos, closestMovingEntity.yPos) < 3){
				System.out.println(closestMovingEntity);
				closestMovingEntity.damage(1);
			}
		}
		if(isHunting && !isMoving() && !(map.entities.contains(closestMovingEntity))){
			huntNearestAnimal();
			if(nextDirections.isEmpty()){
				isHunting = false;
			}
		}else if(isHunting && !isMoving() && (closestMovingEntity == null)){
			moveTo(new Point(closestMovingEntity.xPos, closestMovingEntity.yPos));
		}
		
		
	}
	
	public void huntNearestAnimal(){
		isHunting = true;
		LinkedList<MovingEntity> notIncluded = new LinkedList<MovingEntity>();
		while(true){
			closestMovingEntity = (MovingEntity) map.getClosestMovingEntity(getxPos(), getyPos(), notIncluded);
			if(!(closestMovingEntity instanceof PlayerEntity)){
				break;
			}
			notIncluded.add(closestMovingEntity);
		}
		moveTo(new Point(closestMovingEntity.getxPos(), closestMovingEntity.getyPos()));
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		EntityOptionsPopup popup = new EntityOptionsPopup(this, screen);
		if(isHunting){
			popup.addOption(new Option("stop hunting", popup) {
				public void onClick() {
					isHunting = false;
					owner.screen.setEntityPopup(null);
					closestMovingEntity = null;
				}
			});
		}else{
			popup.addOption(new Option("start hunting", popup) {
				public void onClick() {
					isHunting =  true;
					owner.screen.setEntityPopup(null);
				}
			});
		}
		screen.setEntityPopup(popup);
		
		return false;
	}
	
	public int getMaxHealth(){
		return 20;
	}
	
	public String getName(){
		return name + " the hunter";
	}

}
