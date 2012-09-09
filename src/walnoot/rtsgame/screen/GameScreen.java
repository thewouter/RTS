package walnoot.rtsgame.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.structures.natural.TreeStructure;
import walnoot.rtsgame.map.structures.nonnatural.HunterSchool;
import walnoot.rtsgame.map.structures.nonnatural.LumberJackerSchool;
import walnoot.rtsgame.map.structures.nonnatural.StoneMine;
import walnoot.rtsgame.map.structures.nonnatural.TentIIStructure;
import walnoot.rtsgame.map.structures.nonnatural.TentIStructure;
import walnoot.rtsgame.menubar.HomeBar;
import walnoot.rtsgame.menubar.MenuBarPopupButton;
import walnoot.rtsgame.menubar.StatusBar;
import walnoot.rtsgame.popups.entitypopup.EntityPopup;
import walnoot.rtsgame.popups.screenpopup.ScreenPopup;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupButton;
import walnoot.rtsgame.rest.Inventory;
import walnoot.rtsgame.rest.MousePointer;
import walnoot.rtsgame.rest.Util;

public abstract class GameScreen extends Screen {
	
	public Map map;

	public int translationX, translationY;
	public EntityPopup entityPopup = null;
	
	public LinkedList<Entity> selectedEntities = new LinkedList<Entity>();
	public Entity targetEntity; //the Entity the camera will go to
	
	public HomeBar bar;
	public StatusBar statusBar;
	
	public MousePointer pointer;
	
	public boolean pause = false;
	
	public int level = 0;
	
	public Inventory inventory = new Inventory(this);

	public GameScreen(RTSComponent component, InputHandler input) {
		super(component, input);

		bar = new HomeBar(input, this);
		statusBar = new StatusBar(input,this);
		
	}
	
	public void render(Graphics g){
		Point translation = new Point((int) translationX, (int) translationY);
		
		map.render(g, translation, new Dimension(getWidth(), getHeight()), getWidth(), getHeight());
		bar.render(g, getWidth(), getHeight());
		statusBar.render(g, getWidth(), getHeight());
		if(entityPopup != null) entityPopup.render(g);
		g.setColor(Color.WHITE);
		
		if(selectedEntities.size() == 1){
			font.drawBoldLine(g, selectedEntities.getFirst().getName(), 20, getHeight() - 40, Color.BLACK);
			font.drawBoldLine(g, "Health: " + selectedEntities.getFirst().getHealth(), 20, getHeight() - 30, Color.BLACK);
		}else if(!selectedEntities.isEmpty()){
			font.drawBoldLine(g, "Multiple Select: " + selectedEntities.size(), 20, getHeight() - 30, Color.BLACK);
		}
		
		if(popup!= null){
			popup.render(g);
		}
		
		if(pointer != null){
			pointer.render(g);
		}
	}

	public Entity getSelectedEntity(){
		return selectedEntities.getFirst();
	}
	
	public abstract void save();
	
	public abstract void save(String fileName);
	
	public abstract void load();
	
	public abstract void load(String nameFile);
	
	public abstract void update();
	
	public void setEntityPopup(EntityPopup popup){
		this.entityPopup = popup;
	}

	public void removePopup(){
		entityPopup = null;
	}
	
	public int getMapX(){
		return Util.getMapX(input.getMouseX() - translationX, input.getMouseY() - translationY);
	}
	
	public int getMapY(){
		return Util.getMapY(input.getMouseX() - translationX, input.getMouseY() - translationY);
	}
	
	public void deselectEntity(){
		selectedEntities.clear();
	}
	
	public void pause(){
		pause = true;
		popup = new ScreenPopup((getWidth()-84)/2, (getHeight() - 20)/2, 84, 20, this);
		popup.addPart(new ScreenPopupButton("play", popup, input) {
			public void onLeftClick() {
				dePause();			
			}
		});
	}
	
	public void dePause(){
		popup = null;
		pause = false;
	}
	
	public void setPopup(ScreenPopup popup){
		super.setPopup(popup);
		if(popup == null){
			pause = false;
		}else pause = true;
	}
	
	public boolean isOnlyOnMap(int x, int y){
		return (x >0 && x < getWidth() && y > 0 && y < getHeight() && !bar.isInBar(x, y) && !statusBar.isInBar(x, y));
	}
	
	public void levelUp(){
		level++;
		switch(level){
		case 1:
			bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[5][6], this.bar.screen) {
				public void onLeftClick() {
					screen.pointer = new MousePointer(map, input, screen) {
						public Entity toBuild() {
							return new TentIStructure(map,screen, Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY));
						}
					};
				}

				public String getName() {
					return "Tent I";
				}
			});
			bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[7][7], this.bar.screen) {
				public void onLeftClick() {
					screen.pointer = new MousePointer(map, input, screen) {
						public Entity toBuild() {
							return new HunterSchool(map,screen, Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY));
						}
					};
				}

				public String getName() {
					return "Hunter Hut";
				}
			});
			bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[5][7], this) {
				public void onLeftClick() {
					screen.pointer = new MousePointer(map, input, screen) {
						public Entity toBuild() {
							return new LumberJackerSchool(map,screen, Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY));
						}
					};
				}

				public String getName() {
					return "LumberJackers Hut";
				}
			});
			bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[6][7], this) {
				public void onLeftClick() {
					screen.pointer = new MousePointer(map, input, screen) {
						public Entity toBuild() {
							return new TreeStructure(map, screen, Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY));
						}
					};
					
				}

				public String getName() {
					return "Tree";
				}
			});
			
			bar.buildmenu.removeButton(bar.buildmenu.getButton(1, 1));
			pointer=null;
			return;
		case 2:
			bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[4][6], this) {
				public void onLeftClick() {
					screen.pointer = new MousePointer(screen.map, input, screen) {
						public Entity toBuild() {
							return new StoneMine(map, screen, Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY));
						}
					};
				}
				
				public String getName() {
					return "Quarry";
				}
			});
			bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[3][7], this) {
				public void onLeftClick() {
					screen.pointer = new MousePointer(screen.map, input, screen) {
						public Entity toBuild() {
							return new TentIIStructure(map, screen,Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY));
						}
					};
					
				}
				public String getName() {
					return "Tent II";
				}
			}, 1);
		}
	}
	
}
