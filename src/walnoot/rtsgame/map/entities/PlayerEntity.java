package walnoot.rtsgame.map.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.Util;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.structures.CampFireStructure;
import walnoot.rtsgame.map.structures.TentStructure;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.map.tribes.Tribe;
import walnoot.rtsgame.popups.Option;
import walnoot.rtsgame.popups.OptionsPopup;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.Screen;

public class PlayerEntity extends MovingEntity {
	private String name;
	private ArrayList<ItemEntity> inventory = new ArrayList<ItemEntity>();
	private int lastSelectedOption = -1;
	InputHandler input;
	public PlayerEntity(Map map,int xPos, int yPos, Tribe tribe){
		super(map, xPos, yPos, tribe);
		name = Util.NAME_GEN.getRandomName();
		
		//moveRandomLocation();
	}
	
	public void render(Graphics g){
		if(getTribe() != null) g.setColor(getTribe().getColor());
		else g.setColor(Color.BLACK);
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
		if(entityClicked == this){
			OptionsPopup popup = new OptionsPopup(input, this);
			Option option2 = new Option("Add campfire"){
				public void onClick(){
					map.addEntity(new CampFireStructure(map, xPos, yPos - 1, tribe));
				}
			};
			Option option1 = new Option("Add tent") {
				public void onClick() {
					map.addEntity(new TentStructure(map, xPos, yPos-2, tribe));
				}
			};
			Option dig = new Option("dig"){
				public void onClick() {
					int ID = map.getTile(xPos, yPos - 1).getID();
					if(ID == 0 || ID == 1) map.changeTile(xPos, yPos - 1, Tile.sand1);
					else if(ID == 17) map.changeTile(xPos, yPos - 1, Tile.water1);
				}
			};
			Option raise = new Option("raise"){
				public void onClick() {
					int ID = map.getTile(xPos, yPos - 1).getID();
					if(ID == 2) map.changeTile(xPos, yPos - 1, Tile.sand1);
					else if(ID == 17) map.changeTile(xPos, yPos - 1, Tile.grass1);
				}
			};
			Option subPopupTest = new Option ("test"){
				public void onClick(){
					
				}
			};
			popup.addOption(option1);
			popup.addOption(option2);
			popup.addOption(dig);
			popup.addOption(raise);
			popup.addOption(subPopupTest);
			screen.setPopup(popup);
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
