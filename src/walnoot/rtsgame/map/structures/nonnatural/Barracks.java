package walnoot.rtsgame.map.structures.nonnatural;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.entities.players.Soldier;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.popups.screenpopup.BarracksPopup;
import walnoot.rtsgame.popups.screenpopup.SchoolPopup;
import walnoot.rtsgame.screen.GameScreen;

public class Barracks extends BasicStructure {
	private BufferedImage image = Images.school;
	public static int ID = 211;
	public boolean isSelected = false;
	public ArrayList<Soldier> soldiers = new ArrayList<Soldier>();
	public BarracksPopup popup = new BarracksPopup(screen, image, this, screen.input);
	private int time = 0;
	
	/**
	 * barracks can teach and house soldiers max. 5.
	 */

	public Barracks(Map map, GameScreen screen, int xPos, int yPos) {
		super(map, screen, xPos, yPos, 5, 8, ID);
	}

	protected int getHeadSpace() {
		return 2;
	}

	public int getSize() {
		return 3;
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		screen.setPopup(popup);
		return false;
	}

	public void update() {
		LinkedList<Soldier> toRemove = new LinkedList<Soldier>();
		for(Soldier s: soldiers){
			if(!screen.map.containsEntity(s)){
				toRemove.add(s);
			}
		}
		soldiers.removeAll(toRemove);
		
		if(soldiers.size() < 5 && popup.soldierNeeded()){
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

	public int getCosts() {
		return 0;
	}

	public int getExtraOne() {
		return 0;
	}
	
	public PlayerEntity getPupil(){
		return soldiers.get(0);
	}

}
