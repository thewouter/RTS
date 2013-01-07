package walnoot.rtsgame.popups.screenpopup;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSFont;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.entities.players.Soldier;
import walnoot.rtsgame.map.entities.players.Weapon;
import walnoot.rtsgame.map.entities.players.professions.Profession;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.map.structures.nonnatural.SchoolI;
import walnoot.rtsgame.map.structures.nonnatural.SchoolII;
import walnoot.rtsgame.popups.screenpopup.SchoolPopup.Button;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.Screen;

public class BarracksPopup extends ScreenPopup {
	static BufferedImage image, faceImage = Images.buttons[4][2];
	int xPos, yPos;
	BasicStructure owner;
	InputHandler input;

	public Button swordsMan;
	public Button bow;
	
	LinkedList<Button> buttons = new LinkedList<Button>();

	public BarracksPopup(Screen title, BufferedImage image, BasicStructure owner, InputHandler input) {
		super(title.getWidth() - (image.getWidth() / 2), title.getHeight() - (image.getHeight() / 2), image.getWidth(), image.getHeight(), title);
		this.image = image;
		this.input = input;
		xPos = title.getWidth() / 2 - (image.getWidth() / 2);
		yPos = title.getHeight() / 2 - (image.getHeight() / 2);
		this.owner = owner;
		swordsMan = new Button(owner, xPos + 40, yPos + 20 * 0 + 10, 120, 501, Images.buttons[2][5]);
		bow = new Button(owner, xPos + 40, yPos + 50, 120, 500, Images.buttons[3][5]);
		
		setButtons();
	}
	
	private void setButtons(){
		buttons.clear();
		buttons.add(swordsMan);
		buttons.add(bow);
	}
	
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawImage(image, xPos, yPos, null); // render the box.
		
		for(Button b: buttons){
			b.render(g);
		}
		int size = 0;
		if(owner instanceof SchoolI)size = ((SchoolI)owner).playersCollected.size();
		else if (owner instanceof SchoolII){
			size = ((SchoolII)owner).playersCollected.size();
			g.setColor(Color.WHITE);
			Screen.font.drawBoldLine(g, ""+((SchoolII)owner).Knowledge, xPos + width - 15, yPos + 10, Color.BLACK);
		}
		if(size > 6) size = 6;
		for(int i = 0; i < size; i++){
			g.drawImage(faceImage, xPos + 5, yPos + 5 + i * 16, null);
		}
		
	}
	
	public void update(int mouseX, int mouseY){
		super.update(mouseX, mouseY);
		boolean LmouseDown = input.LMBTapped(), RMouseDown = input.RMBTapped();
		for(Button b: buttons){
			if(LmouseDown){
				if(b.isInButton(mouseX, mouseY)){
					b.onSelect();
				}
			}else if (RMouseDown){
				if(b.isInButton(mouseX, mouseY)){
					b.deSelect();
				}
			}
			
		}
	}
	
	public void updateBarracks() {
		for(Button b: buttons){
			b.update();
		}
	}
	
	public boolean soldierNeeded(){
		for(Button b: buttons){
			if(b.amountToBuild > 0){
				return true;
			}
		}
		return false;
	}
	
	public Soldier getSoldier(){
		for(Button b : buttons){
			if(b.amountToBuild > 0){
				Soldier soldier = new Soldier(((GameScreen)screen).map, (GameScreen)screen, owner.xPos - 1, owner.yPos - 1, null);
				soldier.addSoldierComponent(b.getWeapon(soldier));
				return soldier;
			}
		}
		return null;
	}

	public void onLeftClick(int mouseX, int mouseY) {
		for(Button b: buttons){
			if(b.isInButton(mouseX, mouseY))b.onSelect();
		}
	}
	
	
	public class Button{
		int x, y, width,height;
		BufferedImage button;
		int amountToBuild;
		SchoolI ownerA;
		SchoolII ownerB;
		PlayerEntity pupil = null;
		int teller = 0;
		private final int professionID;
		boolean isActive = true;
		
		public final int TICKS_TO_TEACH;
		
		private Button(BasicStructure owner, int x, int y,int teachTime, int professionID, BufferedImage image){
			this.x = x;
			this.y = y;
			this.professionID = professionID;
			button = image;
			width = button.getWidth();
			height = button.getHeight();
			amountToBuild = 0;
			TICKS_TO_TEACH = teachTime;
		}
		
		public Weapon getWeapon(Soldier owner){
			amountToBuild -= 1;
			return Util.getWeapon(professionID, owner);
		}
		
		public void update(){
			if(pupil != null){
				teller++;
				if(teller > TICKS_TO_TEACH){ // Time to teach!
					Profession.setProfession(professionID, pupil);
					if(ownerA != null)ownerA.releasePupil(pupil);
					else if (ownerB != null) {
						ownerB.releasePupil(pupil);
					}
					pupil = null;
					
				}
			}else if(ownerA != null && amountToBuild >= 1 && ownerA.playersCollected.size() >=1){
				pupil = ownerA.getPupil();
				ownerA.playersCollected.remove(pupil);
				amountToBuild --;
				
			}else if(ownerB != null && amountToBuild >= 1 && ownerB.playersCollected.size() >=1){
				pupil = ownerB.getPupil();
				ownerB.playersCollected.remove(pupil);
				amountToBuild --;
				
			}
		}
		
		public void render(Graphics g){
			g.drawImage(button, x, y, width, height, null);
			g.setColor(new Color(128, 128, 255, 64));
			if(!isActive)g.fillRect(x, y, width, height);
			
			g.setColor(Color.WHITE);
			Screen.font.drawLine(g, "" + amountToBuild, x + width - Screen.font.getLineWidth("" + amountToBuild), y + height - RTSFont.HEIGHT);
		}
		
		public void onSelect(){
			if(isActive)amountToBuild++;
		}
		
		public void deSelect(){
			if(isActive && amountToBuild > 0) amountToBuild--;
		}
		
		public boolean isInButton(int x, int y){
			return(x >= this.x && y >= this.y && x < width  + this.x && y < height + this.y);
		}
	}

}
