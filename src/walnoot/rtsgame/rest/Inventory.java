package walnoot.rtsgame.rest;

import walnoot.rtsgame.popups.screenpopup.ScreenPopup;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupButton;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupTextField;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.SPGameScreen;


/**
 * this is a inventory for the GameScreen object
 * 
 * @author wouter
 *
 */
public class Inventory {
	private GameScreen owner;
	
	public int gold;
	public int meat;
	public int wood;
	
	public Inventory(GameScreen owner){
		this.owner = owner;
	}

	

	public void showInventory() {
		ScreenPopup popup = new ScreenPopup((owner.getWidth()-84)/2, (owner.getHeight() - 20)/2, 84, 20, owner);
		popup.addPart(new ScreenPopupTextField(new String("Gold: " + gold)));
		popup.addPart(new ScreenPopupTextField(new String("Meat: " + meat)));
		popup.addPart(new ScreenPopupTextField(new String("Wood: " + wood)));
		popup.addPart(new ScreenPopupButton("ok",popup,owner.input) {
			public void onLeftClick() {
				owner.screen.setPopup(null);
			}
		});
		owner.setPopup(popup);
	}

}
