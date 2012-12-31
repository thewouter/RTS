package walnoot.rtsgame.map.entities.players.professions;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.structures.nonnatural.BaseOfOperations;
import walnoot.rtsgame.popups.entitypopup.EntityOptionsPopup;
import walnoot.rtsgame.popups.entitypopup.Option;
import walnoot.rtsgame.rest.MousePointer;
import walnoot.rtsgame.rest.Sound;
import walnoot.rtsgame.screen.GameScreen;

public class Founder extends Profession {
	public static int ID = 403;

	public Founder(PlayerEntity owner) {
		super(owner, ID);
	}

	public void update() {
		
	}
	
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		EntityOptionsPopup popup = new EntityOptionsPopup(owner, screen);
		popup.addOption(new Option("Start a new settlement", popup) {
			public void onClick() {
				owner.screen.pointer = new MousePointer(owner.owner.map, owner.owner.screen.input, owner.screen) {
					public Entity toBuild() {
						return new BaseOfOperations(map, screen, screen.getMapX(), screen.getMapY());
					}
					public void afterBuild(){
						//map.removeEntity(owner.owner);
						new Sound("/res/Sounds/Buildbaseofoperations.mp3").play();
						if(screen.level < 1) screen.levelUp();
					}
				};
			}
		});
		screen.setEntityPopup(popup);
		
		
		return true;
	}

	public String getName() {
		return "the Founder";
	}

}
