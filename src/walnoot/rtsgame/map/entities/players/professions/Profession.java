package walnoot.rtsgame.map.entities.players.professions;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.screen.GameScreen;

public abstract class Profession {
	public PlayerEntity owner;

	public Profession(PlayerEntity owner) {
		this.owner = owner;
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
	
	public abstract void update();

	public abstract String getName();
}
