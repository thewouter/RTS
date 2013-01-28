package walnoot.rtsgame.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.structures.natural.TreeStructure;
import walnoot.rtsgame.map.structures.nonnatural.SchoolI;
import walnoot.rtsgame.map.structures.nonnatural.SchoolII;
import walnoot.rtsgame.map.structures.nonnatural.StoneMine;
import walnoot.rtsgame.map.structures.nonnatural.TentIIStructure;
import walnoot.rtsgame.map.structures.nonnatural.TentIStructure;
import walnoot.rtsgame.map.structures.nonnatural.warrelated.WoodenGate;
import walnoot.rtsgame.map.structures.nonnatural.warrelated.WoodenWall;
import walnoot.rtsgame.menubar.Button;
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
	protected MousePointer pointer;
	public boolean pause = false;
	public int level = 0;
	private Color gameColor = Color.CYAN;
	public Inventory inventory = new Inventory(this);
	public Button levelUpButton;

	public GameScreen(RTSComponent component, InputHandler input) {
		super(component, input);

		bar = new HomeBar(input, this);
		statusBar = new StatusBar(input,this);
		
		levelUpButton = new Button(Images.buttons[2][1], statusBar) {
			public void onLeftClick() {
				levelUp();
			}
		};
		
	}
	
	public void render(Graphics g){
		Point translation = new Point((int) translationX, (int) translationY);
		if(map != null) map.render(g, translation, new Dimension(getWidth(), getHeight()), getWidth(), getHeight());
		bar.render(g, getWidth(), getHeight());
		statusBar.render(g, getWidth(), getHeight());
		if(entityPopup != null) entityPopup.render(g);
		g.setColor(Color.WHITE);
		
		if(selectedEntities.size() == 1){
			font.drawBoldLine(g, selectedEntities.getFirst().getName(), 20, getHeight() - 40, Color.BLACK);
			font.drawBoldLine(g, "Health: " + selectedEntities.getFirst().getHealthInString(), 20, getHeight() - 30, Color.BLACK);
		}else if(!selectedEntities.isEmpty()){
			font.drawBoldLine(g, "Multiple Select: " + selectedEntities.size(), 20, getHeight() - 30, Color.BLACK);
		}
		//font.drawBoldLine(g, getMapX() + ":" + getMapY(), 20, 20, Color.BLACK);
		font.drawBoldLine(g, (input.mouseX - translationX) + ":" + (input.mouseY - translationY), 20, 20, Color.BLACK);
		
		if(input.isDragging()){
			int x1 = input.mouseXOnClick, y1 = input.mouseYOnClick, x2 = input.mouseX, y2 = input.mouseY;
			int xMin = Math.min(x1, x2);
			int yMin = Math.min(y1, y2);
			int xMax = Math.max(x1, x2);
			int yMax = Math.max(y1, y2);
			g.setColor(new Color(0,0,0,28));
			g.fillRect(xMin, yMin, xMax - xMin, yMax - yMin);
			
		}
		
		for(Entity e:selectedEntities){
			g.translate(translationX, translationY);
			e.renderSelected(g);
			g.translate(-translationX, -translationY);
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
	
	public int getMapX(int x, int y){
		return Util.getMapX(x - translationX, y - translationY);
	}
	
	public int getMapY(int x, int y){
		return Util.getMapY(x - translationX, y - translationY);
	}
	
	public void deselectEntity(){
		selectedEntities.clear();
	}
	
	public void pause(){
		pause = true;
		popup = new ScreenPopup((getWidth()-84)/2, (getHeight() - 20)/2, 84, 20, this, false);
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
	
	public Color getColor(){
		return gameColor;
	}
	
	public boolean isOnlyOnMap(int x, int y){
		if(entityPopup != null)return (x >0 && x < getWidth() && y > 0 && y < getHeight() && !bar.isInBar(x, y) && !statusBar.isInBar(x, y) && entityPopup.isInPopup(x, y));
		return (x >0 && x < getWidth() && y > 0 && y < getHeight() && !bar.isInBar(x, y) && !statusBar.isInBar(x, y));
	}
	
	public boolean isSelected(Entity e){
		return selectedEntities.contains(e);
	}
	
	public void levelUp(){
		level++;
		switch(level){
		case 1:
			bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[5][6], this.bar.screen) {
				public void onLeftClick() {
					screen.pointer = new MousePointer(map, input, screen) {
						public Entity toBuild(Direction face) {
							return new TentIStructure(map,screen, Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY), face);
						}
					};
				}
				
				public String getName() {
					return "Tent I";
				}
			});
			
			bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[5][5], this.bar.screen) {
				public void onLeftClick() {
					screen.pointer = new MousePointer(map, input, screen) {
						
						public Entity toBuild(Direction face) {
							return new WoodenGate(map, screen, Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY), face);
						}
					};
				}
				public String getName() {
					return "Gate";
				}
			});
			bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[4][5], this.bar.screen) {
				
				public void onLeftClick() {
					screen.pointer = new MousePointer(map, input, screen) {
						
						public Entity toBuild(Direction face) {
							return new WoodenWall(map, screen, Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY), face);
						}
					};
				}
				
				public String getName() {
					return "Wall";
				}
			});
			
			bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[7][6], this) {
				public void onLeftClick() {
					screen.pointer = new MousePointer(map, input, screen) {
						public Entity toBuild(Direction face) {
							return new SchoolI(map, screen, Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY), face);
						}
					};
					
				}

				public String getName() {
					return "School";
				}
			});
			pointer=null;
			for(Entity e:map.getEntities()){
				if(e instanceof SchoolI){
					SchoolI s = ((SchoolI)e);
					s.popup.minerI.activate();
					s.popup.lumberJacker.activate();
					s.popup.hunter.activate();
				}else if(e instanceof SchoolII){
					SchoolII s = ((SchoolII)e);
					s.popup.minerI.activate();
					s.popup.lumberJacker.activate();
					s.popup.hunter.activate();
				}
			}
			return;
		case 2:
			inventory.meat -= 16;
			inventory.gold -= 25;
			bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[4][6], this) {
				public void onLeftClick() {
					screen.pointer = new MousePointer(screen.map, input, screen) {
						public Entity toBuild(Direction face) {
							return new StoneMine(map, screen, Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY));
						}
					};
				}
				
				public String getName() {
					return "Quarry";
				}
			});
			bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[4][7], this) {
				public void onLeftClick() {
					screen.pointer = new MousePointer(screen.map, input, screen) {
						public Entity toBuild(Direction face) {
							return new TentIIStructure(map, screen,Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY), face);
						}
					};
					
				}
				public String getName() {
					return "Tent II";
				}
			}, 1);
			
			bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[6][7], this) {
				
				public void onLeftClick() {
					screen.pointer = new MousePointer(screen.map, input, screen){
						public Entity toBuild(Direction face){
							return new TreeStructure(map, screen, Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY), face);
						}
					};
				}
				
				public String getName() {
					return "Tree";
				}
			});

			for(Entity e:map.getEntities()){
				if(e instanceof SchoolI){
					SchoolI s = ((SchoolI)e);
					s.popup.minerII.activate();
					s.popup.founder.activate();
				}else if(e instanceof SchoolII){
					SchoolII s = ((SchoolII)e);
					s.popup.minerII.activate();
					s.popup.founder.activate();
				}
			}
			break;
		case 3:
			inventory.gold -= 100;
			inventory.meat -= 60;
			inventory.wood -= 25;
		}
	}

	public void setMousePointer(MousePointer p){
		System.out.println(p);
		pointer = p;
	}
}
