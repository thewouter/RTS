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
import walnoot.rtsgame.map.Save;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.structures.nonnatural.TentIStructure;
import walnoot.rtsgame.menubar.HomeBar;
import walnoot.rtsgame.menubar.MenuBarPopupButton;
import walnoot.rtsgame.menubar.StatusBar;
import walnoot.rtsgame.multiplayer.client.InputListener;
import walnoot.rtsgame.multiplayer.client.MPMapClient;
import walnoot.rtsgame.popups.entitypopup.EntityPopup;
import walnoot.rtsgame.popups.screenpopup.ScreenPopup;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupButton;
import walnoot.rtsgame.rest.Inventory;
import walnoot.rtsgame.rest.MousePointer;
import walnoot.rtsgame.rest.Util;

	public class MPGameScreen extends GameScreen {
	public MPMapClient map;
	private int translationX, translationY;
	private EntityPopup entityPopup = null;
	
	private LinkedList<Entity> selectedEntities = new LinkedList<Entity>();
	
	private Entity targetEntity; //the Entity the camera will go to
	
	private HomeBar bar;
	private StatusBar statusBar;	
	
	public MousePointer pointer;
	
	private ScreenPopup popup = null;
	
	boolean pause = false;
	
	public int level = 0, port;
	public String IP;
	
	public Inventory inventory = new Inventory(this);
	
	private InputListener listener;
	
	private Socket socket;


	public MPGameScreen(RTSComponent component, InputHandler input, int port, String IP){
		super(component, input);
		this.IP = IP;
		this.port = port;
		try {
			socket = new Socket(IP, this.port);
			listener = new InputListener(this,  new BufferedReader(new InputStreamReader(socket.getInputStream())), new PrintStream(socket.getOutputStream()));
		} catch (IOException e) {
			System.out.println(port);
			System.out.println(IP);
			System.out.println(e);
			this.component.setTitleScreen();
		}
		map = new MPMapClient(listener.read(), listener);
		bar = new HomeBar(input, this);
		statusBar = new StatusBar(input, this);
		
		listener.start();
	}
	
	public void render(Graphics g){
		Point translation = new Point((int) translationX, (int) translationY);
		
		map.render(g, translation, new Dimension(getWidth(), getHeight()), getWidth(), getHeight());
		bar.render(g, getWidth(), getHeight());
		statusBar.render(g, getWidth(), getHeight());
		
		g.translate(translation.x, translation.y);
		
		if(entityPopup != null) entityPopup.render(g);
		g.translate(-translation.x, -translation.y);
		
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
		
		if(input.escape.isTapped()){
			component.setTitleScreen();
		}
	}
	
	public Entity getSelectedEntity(){
		return selectedEntities.getFirst();
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
				}
				selectedEntities.clear();
				
				if(bar.isInBar(input.getMouseX(), input.getMouseY())){
				
				}else if(entityPopup != null && !entityPopup.isInPopup(input.getMouseX(), input.getMouseY()) ){
					selectedEntities.addAll(map.getEntities(getMapX(), getMapY()));
				}else if( entityPopup == null ){
					selectedEntities.addAll(map.getEntities(getMapX(), getMapY()));
				}
			}
			
			
			
			if(entityPopup != null){
				entityPopup.update(input.getMouseX(), input.getMouseY());
				if(!selectedEntities.contains(entityPopup.getOwner())) entityPopup = null;
			}	
			
			if(input.RMBTapped()){
			
				Entity rightClicked = map.getEntity(getMapX(), getMapY()); //the Entity that is right clicked, if any
			
				boolean canMove = true;
				if(rightClicked != null && !selectedEntities.isEmpty()) canMove = selectedEntities.getFirst().onRightClick(rightClicked, this, input);
				
				if(canMove){
					if(!selectedEntities.isEmpty() && selectedEntities.getFirst() instanceof MovingEntity){
						for(Entity m:selectedEntities){
							if(((MovingEntity)selectedEntities.getFirst()).isMovable()){
								((MovingEntity) selectedEntities.getFirst()).moveTo(new Point(getMapX(), getMapY()));
								entityPopup = null;
							}	
						}
					}
				}
			}
			LinkedList<Entity> remove = new LinkedList<Entity>();
			for(Entity e: selectedEntities){
				if(!map.entities.contains(e)) remove.add(e);
			}
			
			selectedEntities.removeAll(remove);
			if(pointer != null){
				pointer.update(); 
				if(bar.showPopup == false){
					pointer = null;
				}
			}
			
		
		}
		if (input.p.isTapped()&& popup == null){
			if(!pause)pause();
			else dePause();
		}
		if(popup != null){
			popup.update(input.getMouseX(), input.getMouseY());
		}
		if(input.escape.isTapped()) quit();
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
		}else if(Util.parseInt(Util.splitString(message).get(0)) == 0){
			update(message);
		}
	}
	
	private void update(String update){
		ArrayList<String> inStrings = Util.splitString(update);
		int adds;
		int removes;
		int moves;
		
		System.out.print(inStrings.size() + "     ");
		
		moves = Util.parseInt(inStrings.get(1));
		System.out.print(moves + " ");
		adds = Util.parseInt(inStrings.get((5 * moves) + 2));
		System.out.print(adds + " ");
		removes = Util.parseInt(inStrings.get((5 * moves) + (3 * adds) + 2));
		System.out.println(removes);
		
		for(int i  = 2; i < (moves * 5) + 2;){
			int index = Util.parseInt(inStrings.get(i));
			i += 3;
			int x = Util.parseInt(inStrings.get(i));
			i++;
			int y = Util.parseInt(inStrings.get(i));
			i++;
			try{
			if(map.entities.get(index) instanceof MovingEntity) {
				((MovingEntity)map.entities.get(index)).moveToFromHost(new Point(x , y));
				System.out.println("moved!!!");
			}else{
				System.out.println(map.entities.get(index));
			}
			}catch(Exception e){
				System.out.println(map.entities.size() + " requested: " + index);
			}
		}
	}

	public void levelUp(){
		level++;
		switch(level){
		case 1:
			bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[2][0], this.bar.screen) {
				public void onLeftClick() {
					screen.pointer = new MousePointer(map, input, screen) {
						public Entity toBuild() {
							if(screen.inventory.gold >= 10){
								return new TentIStructure(map,screen, Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY));
								}
							return null;
						}
					};
				}
			});
			bar.buildmenu.removeButton(bar.buildmenu.getButton(1, 1));
			pointer=null;
		}
	}
	
	public void quit(){
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

