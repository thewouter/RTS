package walnoot.rtsgame.map.structures.nonnatural;

import java.util.LinkedList;

import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.entities.players.professions.Miner;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.screen.GameScreen;

public class MinerIISchool extends BasicStructure {
	private static final int TIME_TO_TEACH_ONE_PLAYER = 120;
	public static int ID = 208;
	private Boolean isTeaching = false;
	private int teller;
	private LinkedList<PlayerEntity> playersCollected = new LinkedList<PlayerEntity>();;
	
	public MinerIISchool(Map map, GameScreen screen, int xPos, int yPos) {
		super(map, screen, xPos, yPos,0 ,0 , ID);
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
			if(teller > TIME_TO_TEACH_ONE_PLAYER && !playersCollected .isEmpty()){
				PlayerEntity e = playersCollected.getFirst();
				e.setProfession(new Miner(e, 2));
				map.setEntityBackOnMap(e);
				playersCollected.remove(e);
			}
			if(teller > TIME_TO_TEACH_ONE_PLAYER){
				teller = 0;
			}
		}
		for(int x = -1; x < getSize() + 1; x++){
			for(int y = -1; y < getSize() + 1; y++){
				Entity e = map.getEntity(xPos + x, yPos + y);
				if(e != null && e instanceof PlayerEntity && ((PlayerEntity)e).profession instanceof Miner && ((Miner)((PlayerEntity)e).profession).level == 1){	//it's a player, minerI.
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
		return 0;
	}

	public String getName() {
		return "Intermediate mining";
	}

	public int getCosts() {
		return 0;
	}

	public int getExtraOne() {
		return 0;
	}

}
