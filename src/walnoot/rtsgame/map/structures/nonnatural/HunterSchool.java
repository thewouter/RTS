package walnoot.rtsgame.map.structures.nonnatural;

import java.util.LinkedList;

import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.players.HunterEntity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.screen.GameScreen;

public class HunterSchool extends BasicStructure {
	public static int ID = 205, TIME_TO_TEACH_ONE_PLAYER = 120;
	private boolean isTeaching;
	private int teller = 0;
	private LinkedList<Entity> playersCollected = new LinkedList<Entity>();

	public HunterSchool(Map map, GameScreen screen, int xPos, int yPos) {
		super(map, screen, xPos, yPos, 0, 8, ID);
	}

	protected int getHeadSpace() {
		return 2;
	}

	public int getSize() {
		return 2;
	}

	public void update() {

		if(isTeaching){
			teller++;
			if(teller > TIME_TO_TEACH_ONE_PLAYER && !playersCollected.isEmpty()){
				Entity e = playersCollected.getFirst();
				Entity newPlayer = new HunterEntity(map, screen, e.getxPos(), e.getyPos());
				map.addEntity(newPlayer);
				map.removeEntity(e);
				playersCollected.remove(e);
			}
			if(teller > TIME_TO_TEACH_ONE_PLAYER){
				teller = 0;
			}
		}
		for(int x = -1; x < getSize() + 1; x++){
			for(int y = -1; y < getSize() + 1; y++){
				Entity e = map.getEntity(xPos + x, yPos + y);
				if(e != null && e.ID == 102){	//it's a player, no profession.
					playersCollected.add((PlayerEntity) e);
					isTeaching = true;
					map.removeEntity(e);
					map.notOnMap.add(e);
				}
			}
		}
		if(playersCollected.isEmpty()){
			isTeaching = false;
		}
		
		
	}

	public int getMaxHealth() {
		return 30;
	}

	public String getName() {
		return "Hunt School";
	}

	public int getCosts() {
		return 56;
	}

	public int getExtraOne() {
		return 0;
	}

}
