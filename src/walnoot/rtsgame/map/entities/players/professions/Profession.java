package walnoot.rtsgame.map.entities.players.professions;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.screen.GameScreen;

public abstract class Profession {
	public PlayerEntity owner;
	public final int ID;

	public Profession(PlayerEntity owner, int ID) {
		this.owner = owner;
		this.ID = ID;
	}
	
	/**
	 * @param entityClicked
	 * @param screen
	 * @param input
	 * @return if it does something
	 */
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		return false;
	}
	
	public static void setProfession(int ID, PlayerEntity p){
		switch(ID){
		case 400:
			p.setProfession(new LumberJacker(p));
			return;
		case 401:
			p.setProfession(new Miner(p, 1));
			return;
		case 402:
			p.setProfession(new Hunter(p));
			return;
		case 403:
			p.setProfession(new Founder(p));
			return;
		case 404:
			p.setProfession(new Farmer(p));
			return;
		case 405:
			p.setProfession(new Miner(p, 2));
			return;
		}
	}
	
	public abstract void update();

	public abstract String getName();
	
	public void setOwner(PlayerEntity owner){
		this.owner = owner;
	}
}
