package walnoot.rtsgame.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Save;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.structures.nonnatural.SchoolI;
import walnoot.rtsgame.map.structures.nonnatural.SchoolII;
import walnoot.rtsgame.map.structures.nonnatural.StoneMine;
import walnoot.rtsgame.map.structures.nonnatural.TentIIStructure;
import walnoot.rtsgame.map.structures.nonnatural.TentIStructure;
import walnoot.rtsgame.menubar.HomeBar;
import walnoot.rtsgame.menubar.MenuBarPopupButton;
import walnoot.rtsgame.menubar.StatusBar;
import walnoot.rtsgame.multiplayer.client.InputListener;
import walnoot.rtsgame.multiplayer.client.MPMapClient;
import walnoot.rtsgame.multiplayer.host.Player;
import walnoot.rtsgame.popups.entitypopup.EntityPopup;
import walnoot.rtsgame.popups.screenpopup.ScreenPopup;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupButton;
import walnoot.rtsgame.rest.Inventory;
import walnoot.rtsgame.rest.MousePointer;
import walnoot.rtsgame.rest.Sound;
import walnoot.rtsgame.rest.Util;

	public class MPGameScreen extends GameScreen {
	public MPMapClient map;
	
	public int port;
	
	public String IP;
	
	private InputListener listener;
	
	private Socket socket;


	public MPGameScreen(RTSComponent component, InputHandler input, int port, String IP){
		super(component, input);
		this.IP = IP;
		this.port = port;
		try {
			socket = new Socket(this.IP, this.port);
			listener = new InputListener(this,  new BufferedReader(new InputStreamReader(socket.getInputStream())), new PrintStream(socket.getOutputStream()));
		} catch (IOException e) {
			System.out.println(port);
			System.out.println(IP);
			System.out.println(e);
			this.component.setTitleScreen();
		}
		System.out.println("trying to read.... hold on....");
		map = new MPMapClient(listener.read(), listener, this);
		System.out.println("done reading from stream!");
		
		listener.start();
	}
	
	public void save(){
		Save.save(map, "save1");
	}
	
	public void save(String fileName){
		Save.save(map, fileName);
	}
	
	public void load(){
		load("save1");
	}

	public void load(String nameFile){}

	public void update(){
		if(!pause){
			if(input.up.isPressed()) translationY += 5;
			if(input.down.isPressed()) translationY -= 5;
			if(input.left.isPressed()) translationX += 5;
			if(input.right.isPressed()) translationX -= 5;
	
			map.update((int) Math.floor(translationX), (int) Math.floor(translationY), getWidth(), getHeight());

			bar.update(getWidth(), getHeight());
			
			statusBar.update(getWidth(), getHeight());
		
			if(input.space.isPressed() && selectedEntities != null && !selectedEntities.isEmpty()) targetEntity = selectedEntities.getFirst();
		
			if(targetEntity != null){
				int dx = targetEntity.getScreenX() + (translationX - getWidth() / 2);
				int dy = targetEntity.getScreenY() + (translationY - getHeight() / 2);
				
				translationX -= dx / 10;
				translationY -= dy / 10;
			
				if(Util.abs(dx) < 10 && Util.abs(dy) < 10) targetEntity = null;
			}
		
			if(input.LMBTapped()){
				if(entityPopup != null){
					entityPopup.onLeftClick(input.getMouseX(), input.getMouseY());
				}else selectedEntities.clear();
				
				if(bar.isInBar(input.getMouseX(), input.getMouseY())){
					
				}else if(entityPopup != null && !entityPopup.isInPopup(input.getMouseX(), input.getMouseY()) ){
					selectedEntities.clear();
					selectedEntities.addAll(map.getEntities(getMapX(), getMapY()));
				}else if( entityPopup == null ){
					selectedEntities.addAll(map.getEntities(getMapX(), getMapY()));
				}
				
				
				
				
			}
			if(input.isDragging()){
				int x1 = input.mouseXOnClick, y1 = input.mouseYOnClick, x2 = input.mouseX, y2 = input.mouseY;
				selectedEntities.clear();
				selectedEntities.addAll(map.getEntities(x1, y1, x2, y2, new Dimension(translationX, translationY)));
			}
			
			if(entityPopup != null){
				entityPopup.update(input.getMouseX(), input.getMouseY());
				if(!selectedEntities.contains(entityPopup.getOwner())) entityPopup = null;
			}
			
			if(pointer != null){
				pointer.update(); 
				if(bar.showPopup == false){
					pointer = null;
				}
			}
			
			if(input.RMBTapped()){
				
				Entity rightClicked = map.getEntity(getMapX(), getMapY()); //the Entity that is right clicked, if any
				boolean canMove = true;
				
				if(rightClicked != null && !selectedEntities.isEmpty()) {
					canMove = selectedEntities.getFirst().onRightClick(rightClicked, this, input);
				}
				
				if(canMove){
					if(!selectedEntities.isEmpty() && selectedEntities.getFirst() instanceof MovingEntity){
						if(((MovingEntity)selectedEntities.getFirst()).isMovable()){
							((MovingEntity) selectedEntities.getFirst()).moveTo(new Point(getMapX(), getMapY()));
							entityPopup = null;
						}
					}
				}
			}
			LinkedList<Entity> remove = new LinkedList<Entity>();
			for(Entity e: selectedEntities){
				if(!map.getEntities().contains(e)) remove.add(e);
			}
			
			selectedEntities.removeAll(remove);
			
		}
		if (input.p.isTapped()&& popup == null){
			if(!pause)pause();
			else dePause();
		}
		if(input.escape.isTapped()){
			if(popup == null)component.setTitleScreen();
			else setPopup(null);
		}
		if(popup != null){
			popup.update(input.getMouseX(), input.getMouseY());
		}
		if(input.LMBTapped() || input.RMBTapped()) {
			new Sound("/res/Sounds/klick.mp3").play();
		}
	}
	
	public void render(Graphics g){
		Point translation = new Point((int) translationX, (int) translationY);
		map.render(g, translation, new Dimension(getWidth(), getHeight()), getWidth(), getHeight());
		super.render(g);
	}
	
	public void levelUp(){
		level++;
		switch(level){
		case 1:
			bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[5][6], this.bar.screen) {
				public void onLeftClick() {
					screen.pointer = new MousePointer(map, input, screen) {
						public Entity toBuild() {
							return new TentIStructure(map,screen, Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY), Direction.SOUTH_WEST);
						}
					};
				}
				
				public String getName() {
					return "Tent I";
				}
			});
			
			bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[7][6], this) {
				public void onLeftClick() {
					screen.pointer = new MousePointer(map, input, screen) {
						public Entity toBuild() {
							return new SchoolI(map, screen, Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY), Direction.SOUTH_WEST);
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
			bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[4][7], this) {
				public void onLeftClick() {
					screen.pointer = new MousePointer(screen.map, input, screen) {
						public Entity toBuild() {
							return new TentIIStructure(map, screen,Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY), Direction.SOUTH_WEST);
						}
					};
					
				}
				public String getName() {
					return "Tent II";
				}
			}, 1);

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
		}
	}
	
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
		this.popup = popup;
		if(popup == null){
			pause = false;
		}else pause = true;
	}

	public boolean isOnlyOnMap(int x, int y){
		return (x >0 && x < getWidth() && y > 0 && y < getHeight() && !bar.isInBar(x, y) && !statusBar.isInBar(x, y));
	}
	
	public void messageReceived(String message){
		if(Util.parseInt(Util.splitString(message).get(0)) == 1){
			System.out.println(message);
		}else if(Util.parseInt(Util.splitString(message).get(0)) == 2){
			moveEntity(message);
		}else if(Util.parseInt(Util.splitString(message).get(0)) == 4){
			entityAdded(message);
		}else if(Util.parseInt(Util.splitString(message).get(0)) == 5){
			entityRemoved(message);
		}
	}
	
	private void entityAdded(String entity){
		int ID = Util.parseInt(Util.splitString(entity).get(2));
		int uniqueNumber = Util.parseInt(Util.splitString(entity).get(3));
		int xPos = Util.parseInt(Util.splitString(entity).get(4));
		int yPos = Util.parseInt(Util.splitString(entity).get(5));
		int extraInfoOne = Util.parseInt(Util.splitString(entity).get(6));
		Entity e = Util.getEntity(map, ID, xPos, yPos, extraInfoOne);
		e.uniqueNumber = uniqueNumber;
		if(Util.parseInt(Util.splitString(entity).get(1)) == 0) e.setOwned(false);
		e.screen = this;
		map.addEntityFromHost(e);
	
	}
	
	private void moveEntity(String entity){
		int uniqueNumber = Util.parseInt(Util.splitString(entity).get(1));
		Entity e = map.getEntity(uniqueNumber);
		if(e instanceof MovingEntity){
			((MovingEntity)e).moveToFromHost(new Point(Util.parseInt(Util.splitString(entity).get(2)),Util.parseInt(Util.splitString(entity).get(3))));
		}
	}
	
	public void quit(){
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void moveEntity(MovingEntity movingEntity, int x, int y) {
		String update = 2 + " " + movingEntity.uniqueNumber + " " + x + " " + y; 
		listener.update(update);
	}
	
	public void addEntity(Entity e, int x, int y){
		String update = 3 + " " + e.ID + " " + x + " " + y + " " + e.getExtraOne();
		listener.update(update);
	}
	
	public void damageEntity(Entity e, int damage){
		listener.update(6 + " " + e.uniqueNumber + " " + damage);
	}
	
	private void EntityDamaged(String message){
		map.getEntity(Util.parseInt(Util.splitString(message).get(1))).damageFromHost(Util.parseInt(Util.splitString(message).get(2)));
	}
	
	public void removeEntity(Entity e){
		listener.update(5 + " " + e.uniqueNumber);
	}
	
	private void entityRemoved(String message){
		map.removeEntityFromHost(map.getEntity(Util.parseInt(Util.splitString(message).get(1))));
	}
}

