package walnoot.rtsgame.map.structures.nonnatural.warrelated;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.entities.players.Soldier;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.multiplayer.host.MPMapHost;
import walnoot.rtsgame.popups.screenpopup.BarracksPopup;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.MPGameScreen;
import walnoot.rtsgame.screen.Screen;

public class Barracks extends BasicStructure {
	private BufferedImage image = Images.school;
	public static int ID = 211, MAX_SOLDIER = 5;
	public boolean isSelected = false;
	public ArrayList<Soldier> soldiers = new ArrayList<Soldier>();
	private BarracksPopup popup;
	private int time = 0;
	
	/**
	 * barracks can teach and house soldiers.
	 */
	
	public Barracks(Map map, GameScreen screen, int xPos, int yPos, Direction front) {
		super(map, screen, xPos, yPos, 5, 9, ID, front);
		popup = new BarracksPopup(screen, image, this, screen.input);
	}

	public Barracks(Map map, GameScreen screen, int xPos, int yPos, int health, Direction front) {
		super(map, screen, xPos, yPos, 5, 9, ID, front);
		popup = new BarracksPopup(screen, image, this, screen.input);
		this.health = health;
	}
	
	public BarracksPopup getpopup(){
		return popup;
	}

	public int getHeadSpace() {
		return 2;
	}

	public int getSize() {
		return 3;
	}
	
	public void render(Graphics g){
		super.render(g);
		Screen.font.drawLine(g, popup.bow.amountToBuild + "", getScreenX(), getScreenY());
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		screen.setPopup(popup);
		return false;
	}

	public void update() {
		if(screen instanceof MPGameScreen) return;
		LinkedList<Soldier> toRemove = new LinkedList<Soldier>();
		for(Soldier s: soldiers){
			if(!map.containsEntity(s)){
				toRemove.add(s);
			}
		}
		soldiers.removeAll(toRemove);
		
		if(soldiers.size() < MAX_SOLDIER && popup.soldierNeeded()){
			time ++;
			if(time > 180){
				time = 0;
				Soldier s = popup.getSoldier();
				map.addEntity(s);
				soldiers.add(s);
			}
		}
		popup.updateBarracks();
	}

	public int getMaxHealth() {
		return 400;
	}

	public String getName() {
		return "Barracks";
	}

	public HashMap<String, Integer> getCosts() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 5);
		costs.put("wood", 20);
		return costs;
	}

	public String getExtraOne() {
		return "0";
	}
	
	public PlayerEntity getPupil(){
		return soldiers.get(0);
	}

}
