package walnoot.rtsgame.menubar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MenuBar;
import java.util.LinkedList;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.popups.screenpopup.ScreenPopup;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupButton;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupTextField;
import walnoot.rtsgame.popups.screenpopup.TextInput;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.Screen;

public class StatusBar extends walnoot.rtsgame.menubar.MenuBar{
	public InputHandler input;
	public static int X_POS_FROM_RIGHT = 5, Y_POS_FROM_BOTTOM = 5, WIDTH_BUTTON = 16, HEIGHT_BUTTON = Screen.font.HEIGHT, UITLOOP = 5, EXTRA_WIDTH = 30,EXTRA_HEIGHT = 30;
	public GameScreen screen;
	LinkedList <Button> buttons = new LinkedList<Button>();
	int xPosOnScreen = 0, yPosOnScreen = 0;
	private MenuBarPopup popup;
	public boolean showPopup = false;
	
	
	
	public StatusBar(InputHandler input, GameScreen screen) {
		super(input, screen, X_POS_FROM_RIGHT, Y_POS_FROM_BOTTOM, WIDTH_BUTTON, HEIGHT_BUTTON, 1);
		this.input = input;
		this.screen = screen;
		
		addButton(new MenubarTextField(this) {
			public String getText() {
				return "LvL";
			}
		});
		addButton(new MenubarTextField(this) {
			public String getText() {
				return new String(" " + bar.screen.level);
			}
		});
		
		addButton(new MenubarTextField(this) {
			public String getText() {
				return "gold :";
			}
		});
		
		addButton(new MenubarTextField(this) {public String getText() {return "";}}); // 4 empty characters
		
		addButton(new MenubarTextField(this) {
			public String getText() {
				return new String("" + StatusBar.this.screen.inventory.gold);
			}
		});
		
		addButton(new MenubarTextField(this) {public String getText() {return "";}}); // 4 empty characters
		
		
	}
}
