package walnoot.rtsgame.map.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.Sound;
import walnoot.rtsgame.Util;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.structures.CampFireStructure;
import walnoot.rtsgame.map.structures.TentStructure;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.popups.entitypopup.EntityOptionsPopup;
import walnoot.rtsgame.popups.entitypopup.Option;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.Screen;

public class PlayerEntity extends MovingEntity {
	private String name;
	private ArrayList<ItemEntity> inventory = new ArrayList<ItemEntity>();
	private int lastSelectedOption = -1;
	InputHandler input;
	private final static int ID = 102;
	public PlayerEntity(Map map,int xPos, int yPos){
		super(map, xPos, yPos, ID);
		name = Util.NAME_GEN.getRandomName();
		
		//moveRandomLocation();
	}
	
	public PlayerEntity(Map map, int xPos, int yPos, int health){
		super(map,xPos,yPos, ID);
		this.health = health;
		name = Util.NAME_GEN.getRandomName();
	}
	
	public void render(Graphics g){
		g.setColor(Color.BLUE);
		g.fillRect(getScreenX() + 14, getScreenY() + 6, 4, 4);
		
		g.setColor(Color.WHITE);
		Screen.font.drawBoldLine(g, xPos + ":" + yPos, getScreenX(), getScreenY() - 8, Color.BLACK);
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
		if(screen == null){
			System.out.println("test");
		}
		if(entityClicked == this){
			EntityOptionsPopup popup = new EntityOptionsPopup(this, screen);
			
			Option option2 = new Option("Add campfire", popup){
				public void onClick(){
					map.addEntity(new CampFireStructure(map, xPos, yPos - 1));
				}
			};
			Option option1 = new Option("Add tent",popup) {
				public void onClick() {
					map.addEntity(new TentStructure(map, xPos, yPos-2));
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
			popup.addOption(option1);
			popup.addOption(option2);
			popup.addOption(dig);
			popup.addOption(raise);
			screen.setEntityPopup(popup);
		}else follow(entityClicked);
		return false;
	}
	
	protected double getTravelTime(){
		return 100;
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

}
