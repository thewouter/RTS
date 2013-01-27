package walnoot.rtsgame.map.structures.nonnatural;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.popups.screenpopup.SchoolPopup;
import walnoot.rtsgame.screen.GameScreen;

public class SchoolI extends BasicStructure {
	
	private BufferedImage image = Images.school;
	public static int ID = 210;
	public boolean isSelected = false;
	public ArrayList<PlayerEntity> playersCollected = new ArrayList<PlayerEntity>();
	public SchoolPopup popup = new SchoolPopup(screen, image, this, screen.input);
	
	
	public SchoolI(Map map, GameScreen screen, int xPos, int yPos, Direction front) {
		super(map, screen, xPos, yPos, 0, 12, ID, front);
		int level = screen.level;
		
		if(level <= 0) return;
		popup.hunter.activate();
		popup.lumberJacker.activate();
		popup.minerI.activate();
		if(level <= 1) return;
		popup.minerII.activate();
		popup.founder.activate();
	}

	public int getHeadSpace() {
		return 2;
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		screen.setPopup(popup);
		return false;
	}

	public int getSize() {
		return 2;
	}

	public void update() {
		for(int x = -1; x < getSize() + 1; x++){
			for(int y = -1; y < getSize() + 1; y++){
				Entity e = map.getEntity(xPos + x, yPos + y);
				if(e != null && e instanceof PlayerEntity && ((PlayerEntity)e).profession == null){	//it's a player, no profession.
					playersCollected.add((PlayerEntity) e);
					map.removeEntityFromMap(e);
				}
			}
		}
		popup.updateSchool();
	}

	public int getMaxHealth() {
		return 567;
	}

	public String getName() {
		return "Elementary school";
	}

	public HashMap<String, Integer> getCosts() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold",10);
		costs.put("wood", 10);
		return costs;
	}

	public int getExtraOne() {
		return 0;
	}
	
	public void releasePupil(PlayerEntity pupil){
		pupil.entityGoal = null;
		map.setEntityBackOnMap(pupil);
	}
	
	public PlayerEntity getPupil(){
		return playersCollected.get(0);
	}
}
