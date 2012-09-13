package walnoot.rtsgame.map.structures.nonnatural;

import java.util.LinkedList;

import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.players.LumberJackerPlayer;
import walnoot.rtsgame.map.entities.players.MinerEntity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.screen.GameScreen;

public class MinerISchool extends BasicStructure {
	private static final int TIME_TO_TEACH_ONE_PLAYER = 120;
	public static int ID = 207;
	private boolean isTeaching = false;
	private int teller = 0;
	private LinkedList<PlayerEntity> playersCollected = new LinkedList<PlayerEntity>();
	
	public MinerISchool(Map map, GameScreen screen, int xPos, int yPos) {
		super(map, screen, xPos, yPos, 0, 0, ID);
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
				PlayerEntity e = playersCollected.getFirst();
				Entity newPlayer = new MinerEntity(map, screen, e.getxPos(), e.getyPos(), e.owner);
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
				if(e != null && e.ID == 102){	//it's a player, no proffession.
					playersCollected.add((PlayerEntity) e);
					isTeaching = true;
					map.notOnMap.add(e);
					map.removeEntityFromMap(e);
				}
			}
		}
		if(playersCollected.isEmpty()){
			isTeaching = false;
		}
	}

	public int getMaxHealth() {
		return 26;
	}

	public String getName() {
		return "Elementary mining";
	}

	public int getCosts() {
		return 13;
	}

	public int getExtraOne() {
		return 0;
	}

}
