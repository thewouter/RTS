package walnoot.rtsgame.menubar;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.structures.nonnatural.BaseOfOperations;
import walnoot.rtsgame.popups.screenpopup.ScreenPopup;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupButton;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupTextField;
import walnoot.rtsgame.popups.screenpopup.TextInput;
import walnoot.rtsgame.rest.MousePointer;
import walnoot.rtsgame.rest.Sound;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.MPGameScreen;


public class HomeBar extends MenuBar{
		public static int X_POS_FROM_RIGHT = 5, Y_POS_FROM_BOTTOM = 5, WIDTH_BUTTON = 16, HEIGHT_BUTTON = 16;
	
		public MenuBarPopup buildmenu;
		
		public HomeBar(InputHandler input, GameScreen GameScreen) {
			super(input, GameScreen, X_POS_FROM_RIGHT, Y_POS_FROM_BOTTOM, WIDTH_BUTTON, HEIGHT_BUTTON, 3);
			
			buildmenu = new MenuBarPopup(xPosOnScreen + UITLOOP + (buttons.indexOf(this) + 1)*WIDTH_BUTTON, yPosOnScreen + UITLOOP, buttons.indexOf(this), false);
			
			
			
			
			addButton(new Button(Images.buttons[0][0], this) {
				public void onLeftClick() {
					setMenuBarPopup(buildmenu);
					showPopup = !showPopup;
				}
			});
			
			addButton(new Button(Images.buttons[1][1], this){
				public void onLeftClick() {
					MenuBarPopup popup = new MenuBarPopup(xPosOnScreen + UITLOOP + (buttons.indexOf(this) + 1)*WIDTH_BUTTON, yPosOnScreen + UITLOOP, buttons.indexOf(this), false);
					popup.addButton(new MenuBarPopupButton(Images.buttons[2][0],bar.screen) {

						public void onLeftClick() {
							ScreenPopup options = new ScreenPopup(bar.screen.getWidth() / 2 - 50, bar.screen.getHeight() / 2 - 50, 85, 1, bar.screen);
							options.addPart(new ScreenPopupTextField("set music"));
							options.addPart(new ScreenPopupButton("on", options, bar.input) {
								public void onLeftClick() {
									this.owner.screen.component.amplifybackground();
								}
							});
							options.addPart(new ScreenPopupButton("off", options, bar.input) {
								public void onLeftClick() {
									this.owner.screen.component.muteBackground();
								}
							});
							options.addPart(new ScreenPopupButton("ok", options, bar.input) {
								public void onLeftClick() {
									this.owner.screen.setPopup(null);
								}
							});
							bar.screen.setPopup(options);
						}

						public String getName() {
							return "Sound";
						}
					});
					setMenuBarPopup(popup);
					showPopup = !showPopup;
				}
			});
			
			addButton(new Button(Images.buttons[0][1], this){
				public void onLeftClick() {
					MenuBarPopup popup = new MenuBarPopup(xPosOnScreen + UITLOOP + (buttons.indexOf(this) + 1)*WIDTH_BUTTON, yPosOnScreen + UITLOOP, buttons.indexOf(this), false);
					popup.addButton(new MenuBarPopupButton(Images.buttons[1][0],bar.screen) {
						public void onLeftClick() {
							ScreenPopup popup = new ScreenPopup((bar.screen.getWidth() - 100)/2, (bar.screen.getHeight() - 100)/2, 100, 100, bar.screen);
							popup.addPart(new TextInput(popup, bar.input));
							popup.addPart(new ScreenPopupButton("load", popup, bar.input){
								public void onLeftClick() {
									bar.screen.load(owner.getTextInput(1).getOutput());
									bar.screen.setPopup(null);
								}
							});
							popup.addPart(new ScreenPopupButton("cancel", popup, bar.input) {
								public void onLeftClick() {
									bar.screen.setPopup(null);
								}
							});
							bar.screen.setPopup(popup);
							
						}

						public String getName() {
							return "Load";
						}
						
					});
					
					popup.addButton(new MenuBarPopupButton(Images.buttons[2][0],bar.screen) {
						public void onLeftClick() {
							ScreenPopup popup = new ScreenPopup((screen.getWidth() - 100)/2, (screen.getHeight() - 100)/2, 100, 100, screen);
							popup.addPart(new TextInput(popup, bar.input));
							popup.addPart(new ScreenPopupButton("Save", popup, bar.input){
								public void onLeftClick() {
									screen.save(owner.getTextInput(1).getOutput());
									screen.setPopup(null);
								}
							});
							popup.addPart(new ScreenPopupButton("cancel", popup, bar.input) {
								public void onLeftClick() {
									screen.setPopup(null);
								}
							});
							screen.setPopup(popup);
						}

						public String getName() {
							return "Save";
						}
					});
					
					setMenuBarPopup(popup);
					showPopup = !showPopup;
				}
			});
			
			addButton(new Button(Images.buttons[3][0], this) {
				public void onLeftClick() {
					this.bar.screen.component.setTitleScreen();
					if(this.bar.screen instanceof MPGameScreen){
						((MPGameScreen) this.bar.screen).quit();
					}
				}
			});
			
		}
}
