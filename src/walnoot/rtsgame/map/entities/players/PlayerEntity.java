package walnoot.rtsgame.map.entities.players;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import sun.awt.geom.AreaOp.AddOp;

import walnoot.rtsgame.Animation;
import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.ItemEntity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.structures.nonnatural.CampFireStructure;
import walnoot.rtsgame.map.structures.nonnatural.LumberJacker;
import walnoot.rtsgame.map.structures.nonnatural.TentIStructure;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.popups.entitypopup.EntityOptionsPopup;
import walnoot.rtsgame.popups.entitypopup.Option;
import walnoot.rtsgame.rest.Sound;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.SPGameScreen;

public class PlayerEntity extends MovingEntity {
	public String name;
	private ArrayList<ItemEntity> inventory = new ArrayList<ItemEntity>();
	private int lastSelectedOption = -1;
	InputHandler input;
	private final static int ID = 102;
	private Animation animation;
	private Animation backwardAnimation;
	
	public PlayerEntity(Map map, GameScreen screen,int xPos, int yPos){
		super(map,screen, xPos, yPos, ID);
		name = Util.NAME_GEN.getRandomName();
		loadAnimation(Images.player);
	}
	
	public void update(){
		super.update();
		animation.update();
		backwardAnimation.update();
	}
	
	public PlayerEntity(Map map, GameScreen screen, int xPos, int yPos, int health){
		super(map,screen,xPos,yPos, ID);
		this.health = health;
		name = Util.NAME_GEN.getRandomName();
		loadAnimation(Images.player);
	}
	
	private void loadAnimation(BufferedImage[][] image){
		animation = new Animation(3); 
		for(int i = 0; i < image.length; i++){
			animation.addScene(image[i][0]);
		}
	
		backwardAnimation = new Animation(3);
		for(int i = image.length - 1; i >= 0; i--){
			backwardAnimation.addScene(image[i][0]);
		}
		
	}
	
	public void render(Graphics g){/*
		g.setColor(Color.BLUE);
		g.fillRect(getScreenX() + 14, getScreenY() + 6, 4, 4);
		
		g.setColor(Color.WHITE);
		Screen.font.drawBoldLine(g, xPos + ":" + yPos, getScreenX(), getScreenY() - 8, Color.BLACK);
		*/

		if(isMoving() && nextDirections.getFirst().getyOffset() - nextDirections.getFirst().getxOffset() <= 0)g.drawImage(animation.getImage(), getScreenX() + (Tile.WIDTH - animation.getImage().getWidth(null)) / 2, getScreenY() - (animation.getImage().getHeight(null) - Tile.HEIGHT / 2), null);
		else if(isMoving())g.drawImage(backwardAnimation.getImage(), getScreenX() + (Tile.WIDTH - animation.getImage().getWidth(null)) / 2, getScreenY() - (animation.getImage().getHeight(null) - Tile.HEIGHT / 2), null);
		else g.drawImage(animation.getImage(0), getScreenX() + (Tile.WIDTH - animation.getImage().getWidth(null)) / 2, getScreenY() - (animation.getImage().getHeight(null) - Tile.HEIGHT / 2), null);
	}
	
	protected void onStopMoving(){
		Entity e = map.getEntity(xPos, yPos);
		
		if(e instanceof ItemEntity){
			ItemEntity item = (ItemEntity) e;
			inventory.add(item);
			
			map.removeEntity(e);
		}
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		if(entityClicked == this){
			EntityOptionsPopup popup = new EntityOptionsPopup(this, screen);
			
			Option option2 = new Option("Add campfire", popup){
				public void onClick(){
					map.addEntity(new CampFireStructure(map,this.owner.screen, xPos, yPos - 1));
				}
			};
			Option option1 = new Option("Add tent",popup) {
				public void onClick() {
					map.addEntity(new TentIStructure(map, this.owner.screen, xPos, yPos-2));
				}
			};
			Option dig = new Option("dig", popup){
				public void onClick() {
					int ID = map.getTile(xPos, yPos - 1).getID();
					if(ID == 0 || ID == 1) map.changeTile(xPos, yPos - 1, Tile.sand1);
					else if(ID == 17) map.changeTile(xPos, yPos - 1, Tile.water1);
				}
			};
			Option raise = new Option("raise",popup){
				public void onClick() {
					int ID = map.getTile(xPos, yPos - 1).getID();
					if(ID == 2) map.changeTile(xPos, yPos - 1, Tile.sand1);
					else if(ID == 17) map.changeTile(xPos, yPos - 1, Tile.grass1);
				}
			};
			popup.addOption(new Option("shoot", popup){
				public void onClick() {
					new Sound("/res/Sounds/shot.wav").play();
				}
			});
			
			popup.addOption(new Option("kill", popup) {
				public void onClick() {
					this.owner.owner.damage(this.owner.owner.getMaxHealth());
				}
			});
			popup.addOption(option1);
			popup.addOption(option2);
			popup.addOption(dig);
			popup.addOption(raise);
			
			screen.setEntityPopup(popup);
		}else if(entityClicked instanceof LumberJacker) moveTo(new Point(entityClicked.xPos, entityClicked.yPos));
		else System.out.println(entityClicked);
		return false;
	}
	
	protected double getTravelTime(){
		return 500;
	}
	
	public int getMaxHealth(){
		return 10;
	}
	
	public String getName(){
		return name;
	}
	
	public void setSelectedOption(int index){
		lastSelectedOption = index;
	}
	
	public int getSelectedOption(){
		return lastSelectedOption;
	}

	public int getExtraOne() {
		return 0;
	}

	public boolean isMovable() {
		return true;
	}

}
