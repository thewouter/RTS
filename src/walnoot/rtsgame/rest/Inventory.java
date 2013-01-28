package walnoot.rtsgame.rest;

import walnoot.rtsgame.multiplayer.host.MPHost;
import walnoot.rtsgame.popups.screenpopup.ScreenPopup;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupButton;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupTextField;
import walnoot.rtsgame.screen.GameScreen;


/**
 * this is a inventory for the GameScreen || MPHost object
 * @author wouter
 */
public class Inventory {
	private GameScreen owner;
	
	public int gold = 0;
	public int meat = 0;
	public int wood = 0;
	public int stone = 0;
	public int vegetables = 0;
	
	public Inventory(GameScreen owner){
		this.owner = owner;
	}
	
	public Inventory(MPHost mpHost) {
		
	}

	public void showInventory() {
		ScreenPopup popup = new ScreenPopup((owner.getWidth()-200)/2, (owner.getHeight() - 20)/2, 200, 20, owner, false);
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
