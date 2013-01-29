package walnoot.rtsgame.rest;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.popups.screenpopup.ScreenPopup;
import walnoot.rtsgame.screen.Screen;

public class InventoryPopup extends ScreenPopup {
	private static int BLANK_SPACE = 10, SIZE_WIDTH = 5, SIZE_HEIGHT = 5, TILE_WIDTH = 16, TILE_HEIGHT = 16, SPACE_BETWEEN_TILES = 1;
	private static int WIDTH = 2 * BLANK_SPACE + (SIZE_WIDTH - 1) * SPACE_BETWEEN_TILES + SIZE_WIDTH * TILE_WIDTH, 
			HEIGHT =  2 * BLANK_SPACE + (SIZE_HEIGHT - 1) * SPACE_BETWEEN_TILES + SIZE_HEIGHT * TILE_HEIGHT;
	public Button gold = new Button(0,0,this, Images.buttons[6][1]);
	public ArrayList<Button> buttons = new ArrayList<>();
	private Inventory inventory;
	
	
	
	public InventoryPopup(Screen title, Inventory inventory) {
		super((title.getWidth() - WIDTH) / 2, (title.getHeight() - HEIGHT)/2, WIDTH, HEIGHT, title, false);
		this.inventory = inventory;
		loadButtons();
	}
	
	public void loadButtons(){
		buttons.clear();
		buttons.add(gold);
		gold.updateAmount(inventory.getGold());
	}
	
	public void render(Graphics g){
		drawBox(g, width, height, xPos, yPos);
		for(Button b:buttons){
			b.render(g);
		}
		
	}
	
	public class Button{
		private int x, y;
		private InventoryPopup popup;
		private Image image;
		private int amount = 0;
		public Button(int x, int y, InventoryPopup popup, Image image){
			this.x = x;
			this.y = y;
			this.popup = popup;
			this.image = image;
		}
		
		public void render(Graphics g){
			int xPos = popup.xPos + InventoryPopup.BLANK_SPACE + x * (InventoryPopup.TILE_WIDTH + InventoryPopup.SPACE_BETWEEN_TILES);
			int yPos = popup.yPos + InventoryPopup.BLANK_SPACE + y * (InventoryPopup.TILE_HEIGHT + InventoryPopup.SPACE_BETWEEN_TILES);
			g.drawImage(image, xPos, yPos, InventoryPopup.TILE_WIDTH, InventoryPopup.TILE_HEIGHT, null);
			Screen.font.drawLine(g, amount + "", xPos, yPos);
		}
		
		public void updateAmount(int amount){
			this.amount = amount;
		}
	}
}
