package walnoot.rtsgame.map.entities;

import java.awt.Color;
import java.awt.Graphics;

import walnoot.rtsgame.Animation;
import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.popups.entitypopup.EntityOptionsPopup;
import walnoot.rtsgame.popups.entitypopup.Option;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.SPGameScreen;

public class SheepEntity extends MovingEntity {
	
	private Animation sheepAnimation;
	private Animation currentAnimation;
	public static final int WALK_RANGE = 6, WALK_CHANGE = 5, TICKS_PER_SHEEP = 5, APROX_LIFETIME_IN_TICKS = 60000, ID = 101;

	public SheepEntity(Map map, GameScreen screen, int xPos, int yPos) {
		super(map, screen, xPos, yPos, ID);
		sheepAnimation = new Animation(TICKS_PER_SHEEP);
		currentAnimation = sheepAnimation;
		for(int i = 0; i < Images.sheep.length-1; i++){
			sheepAnimation.addScene(Images.sheep[i][0]);
		}
	}
	
	public SheepEntity(Map map,GameScreen screen, int xPos, int yPos, int health){
		super(map,screen ,xPos, yPos, ID);
		this.health = health;
		sheepAnimation = new Animation(TICKS_PER_SHEEP);
		currentAnimation = sheepAnimation;
		for(int i = 0; i < Images.sheep.length-1; i++){
			sheepAnimation.addScene(Images.sheep[i][0]);
		}
	}

	protected double getTravelTime() {
		return 1000;
	}
	
	
	public int getSelectedOption() {
		return -1;
	}
	
	public void update(){
		super.update();
		sheepAnimation.update();
		if(!isMoving() && Util.RANDOM.nextInt(1000) < WALK_CHANGE) moveRandomLocation(WALK_RANGE);
		if(Util.RANDOM.nextInt(APROX_LIFETIME_IN_TICKS) == 0)map.removeEntity(xPos, yPos);
	}
	
	
	
	public boolean onRightClick(Entity entityClicked, SPGameScreen screen, InputHandler input){
		if(entityClicked == this){
			EntityOptionsPopup popup = new EntityOptionsPopup(this, screen);
			Option option1 = new Option("getclosest",popup){
				public void onClick() {
					System.out.println(map.getClosestEntity(getxPos(), getyPos()).getName());
				}
			};
			popup.addOption(option1);
			popup.addOption(new Option("get closest moving entity",popup){
				public void onClick() {
					System.out.println(map.getClosestMovingEntity(getxPos(), getyPos()).getName());
				}
			});
			
			screen.setEntityPopup(popup);
			
		}else{
			follow(entityClicked);
		}
		
		return true;
	}
	
	public void setSelectedOption(int indexSelected){}
	
	public void render(Graphics g){
		g.setColor(Color.BLACK);
		if(isMoving()) g.drawImage(currentAnimation.getImage(), getScreenX(), getScreenY() - Tile.getHeight() / 2, null);
		else g.drawImage(Images.sheep[Images.sheep.length-1][0], getScreenX(), getScreenY() - Tile.getHeight() / 2, null);
	}
	
	public int getMaxHealth(){
		return 5;
	}

	public String getName(){
		return "Sheep";
	}
	
	public int getExtraOne() {
		return 0;
	}
	
	public boolean isMovable(){
		return true;
	}
}
