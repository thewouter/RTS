package walnoot.rtsgame.map.entities.players;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.lang.invoke.MethodHandleProxies;
import java.util.ArrayList;
import java.util.LinkedList;

import walnoot.rtsgame.Animation;
import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.ItemEntity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.entities.players.professions.LumberJacker;
import walnoot.rtsgame.map.entities.players.professions.Profession;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.map.structures.Structure;
import walnoot.rtsgame.map.structures.nonnatural.CampFireStructure;
import walnoot.rtsgame.map.structures.nonnatural.TentIStructure;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.multiplayer.host.MPHost;
import walnoot.rtsgame.multiplayer.host.MPMapHost;
import walnoot.rtsgame.popups.entitypopup.EntityOptionsPopup;
import walnoot.rtsgame.popups.entitypopup.Option;
import walnoot.rtsgame.rest.Sound;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.MPGameScreen;

public class PlayerEntity extends MovingEntity {
	public String name;
	private ArrayList<ItemEntity> inventory = new ArrayList<ItemEntity>();
	private int lastSelectedOption = -1;
	InputHandler input;
	protected static int ID = 102;
	private Animation animation;
	private Animation backwardAnimation;
	public final Structure ownerTent;
	private Profession profession;
	
	public PlayerEntity(Map map, GameScreen screen, int xPos, int yPos, Structure tent){
		super(map,screen, xPos, yPos, ID);
		name = Util.NAME_GEN.getRandomName();
		loadAnimation(Images.player);
		ownerTent = tent;
		/*final Exception e = new Exception();
		new Thread(){
			public void run(){
				e.printStackTrace();
			}
		}.start();*/
	}
	
	public PlayerEntity(Map map, GameScreen screen, int xPos, int yPos, int ID, Structure tent){
		super(map,screen, xPos, yPos, ID);
		name = Util.NAME_GEN.getRandomName();
		loadAnimation(Images.player);
		ownerTent = tent;
		/*final Exception e = new Exception();
		new Thread(){
			public void run(){
				e.printStackTrace();
			}
		}.start();*/
	}
	
	public void update(){
		super.update();
		animation.update();
		backwardAnimation.update();
		if(profession != null){
			profession.update();
		}
	}
	
	public PlayerEntity(Map map, GameScreen screen, int xPos, int yPos, Structure tent, int health){
		super(map,screen,xPos,yPos, ID);
		this.health = health;
		name = Util.NAME_GEN.getRandomName();
		ownerTent = tent;
	}
	
	public PlayerEntity(Map map, GameScreen screen, int xPos, int yPos, int ID, Structure tent, int health){
		super(map,screen,xPos,yPos, ID);
		this.health = health;
		name = Util.NAME_GEN.getRandomName();
		ownerTent = tent;
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
	
	public void setNextDirections(LinkedList<Direction> toSet, boolean fromEndPoint){
		super.setNextDirections(toSet, fromEndPoint);
		if(profession != null){
			profession.walkingCalculated();
		}
	}
	
	public void render(Graphics g){
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
	
	public int getHeadSpace(){
		return 2;
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		if(profession != null && (entityClicked == this) && profession.onRightClick(entityClicked, screen, input)) return false;
		if(entityClicked == this){
			EntityOptionsPopup popup = new EntityOptionsPopup(this, screen);
			      
			Option option2 = new Option("lumber", popup){
				public void onClick(){
					((PlayerEntity)owner.owner).setProfession(new LumberJacker((PlayerEntity) owner.owner));
				}
			};
			Option option1 = new Option("add tent",popup) {
				public void onClick() {
					map.addEntity(new TentIStructure(map, this.owner.screen, xPos, yPos-2, Direction.SOUTH_WEST));
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
					new Sound("/res/Sounds/shot.mp3").play();
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
		}else if(entityClicked instanceof BasicStructure){
			moveTo(entityClicked);
		}else if(entityClicked instanceof MovingEntity){
			follow(entityClicked);
		}
		return false;
	}
	
	protected double getTravelTime(){
		return 500;
	}
	
	public int getMaxHealth(){
		return 10;
	}
	
	public String getName(){
		if(profession == null) return name;
		return name + " " + profession.getName();
	}
	
	public void setSelectedOption(int index){
		lastSelectedOption = index;
	}
	
	public int getSelectedOption(){
		return lastSelectedOption;
	}

	public String getExtraOne() {
		return 1 + " " + Util.getProfessionID(profession);
	}

	public boolean isMovable() {
		return true;
	}
	
	public void setProfession(Profession p){
		if(screen instanceof MPGameScreen){ // it's the client.
			((MPGameScreen)screen).setProfession(this, p);
			return;
		}else if(map instanceof MPMapHost && map.containsEntity(this)){ // it's the host
			((MPMapHost)map).addProfession(this, p);
		}
		profession = p;
	}
	
	public void setProfessionFromHost(Profession p){
		profession = p;
	}
	
	public Profession getProfession(){
		return profession;
	}
}
