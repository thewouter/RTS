package walnoot.rtsgame.rest;

import walnoot.rtsgame.popups.screenpopup.ScreenPopup;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupButton;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupTextField;
import walnoot.rtsgame.screen.GameScreen;


/**
 * this is a inventory for the GameScreen object
 * 
 * @author wouter
 *
 */
public class Inventory {
	private GameScreen owner;
	
	public int gold = 10000;
	public int meat = 10000;
	public int wood;
	public int stone;
	public int vegetables;
	
	public Inventory(GameScreen owner){
		this.owner = owner;
	}
	
	public void showInventory() {
		ScreenPopup popup = new ScreenPopup((owner.getWidth()-84)/2, (owner.getHeight() - 20)/2, 130, 20, owner);
		popup.addPart(new ScreenPopupTextField(new String("Gold: " + gold + "  stone: " + stone)));
		popup.addPart(new ScreenPopupTextField(new String("Meat: " + meat + " vegetables: " + vegetables)));
		popup.addPart(new ScreenPopupTextField(new String("Wood: " + wood)));
		popup.addPart(new ScreenPopupButton("ok",popup,owner.input) {
			public void onLeftClick() {
				owner.screen.setPopup(null);
			}
		});
		owner.setPopup(popup);
	}

}
