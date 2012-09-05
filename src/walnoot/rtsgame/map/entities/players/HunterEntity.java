package walnoot.rtsgame.map.entities.players;

import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

import walnoot.rtsgame.Animation;
import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.popups.entitypopup.EntityOptionsPopup;
import walnoot.rtsgame.popups.entitypopup.Option;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.SPGameScreen;

public class HunterEntity extends PlayerEntity {
	public boolean isHunting = false;
	public static int ID = 104, TIME_TO_HUNT_ONE_DAMAGE = 5;
	private int teller = 0;
	private MovingEntity closestMovingEntity;

	public HunterEntity(Map map, GameScreen screen, int xPos, int yPos, int health) {
		super(map, screen, xPos, yPos, health);
		setID(ID);
		
	}
	public HunterEntity(Map map, SPGameScreen screen, int xPos, int yPos){
		super(map, screen, xPos, yPos);
		setID(ID);
	}

	public void update(){
		super.update();
		if(isHunting){
			teller++;
		}
		if(isHunting && !isMoving() && (!(map.entities.contains(closestMovingEntity)) || closestMovingEntity == null)){
			huntNearestAnimal();
		}
		
		if(isHunting){
			if(teller >= TIME_TO_HUNT_ONE_DAMAGE){						// hunting time !
				teller = 0;
				if(Util.getDistance(this.xPos, this.yPos, closestMovingEntity.xPos, closestMovingEntity.yPos) <= 1){
					closestMovingEntity.damage(1);
					screen.inventory.meat+=1;
					if(!map.entities.contains(closestMovingEntity)){	// it's dead.
						huntNearestAnimal();
					}
				}
			}
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
		//moveTo(new Point( closestMovingEntity.xPos, closestMovingEntity.yPos));
		follow(closestMovingEntity);
	}
	
	/*public void render(Graphics g){
		g.drawImage(Images.dudes[1][0], getScreenX(), getScreenY(), null);
	}
	*/
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		if(entityClicked != this) return false;
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
