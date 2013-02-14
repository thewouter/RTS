package walnoot.rtsgame.map.entities.players.professions;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.rest.Util;
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
		p.setProfession(Util.getProfession(ID, p));
	}
	
	public abstract void update();

	public abstract String getName();
	
	public void walkingCalculated(){}
	
	public void setOwner(PlayerEntity owner){
		this.owner = owner;
	}
}
