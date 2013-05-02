package walnoot.rtsgame.screen;

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
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.entities.players.professions.Profession;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.map.structures.Structure;
import walnoot.rtsgame.map.structures.natural.StoneMine;
import walnoot.rtsgame.map.structures.nonnatural.SchoolI;
import walnoot.rtsgame.map.structures.nonnatural.SchoolII;
import walnoot.rtsgame.map.structures.nonnatural.Tent;
import walnoot.rtsgame.map.structures.nonnatural.warrelated.Barracks;
import walnoot.rtsgame.map.structures.nonnatural.warrelated.StoneDefenseTower;
import walnoot.rtsgame.menubar.MenuBarPopupButton;
import walnoot.rtsgame.multiplayer.client.Chat;
import walnoot.rtsgame.multiplayer.client.InputListener;
import walnoot.rtsgame.multiplayer.client.MPMapClient;
import walnoot.rtsgame.popups.entitypopup.EntityPopup;
import walnoot.rtsgame.popups.screenpopup.BarracksPopup.Button;
import walnoot.rtsgame.popups.screenpopup.ScreenPopup;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupButton;
import walnoot.rtsgame.rest.MousePointer;
import walnoot.rtsgame.rest.Sound;
import walnoot.rtsgame.rest.Util;

	public class MPGameScreen extends GameScreen {
	public MPMapClient map;
	public int port;
	public String IP;
	private InputListener listener;
	private Socket socket;
	private boolean isLoaded = false;
	private ArrayList<String> messagesToHandle = new ArrayList<>();
	private Chat chat = new Chat(this);


	public MPGameScreen(RTSComponent component, InputHandler input, int port, String IP){
		super(component, input);
		this.IP = IP;
		this.port = port;
		try {
			socket = new Socket(this.IP, this.port);
			listener = new InputListener(this,  new BufferedReader(new InputStreamReader(socket.getInputStream())), new PrintStream(socket.getOutputStream()));
			listener.send("0 " + component.getLoginName());
		} catch (IOException e) {
			System.out.println(port);
			System.out.println(IP);
			System.out.println(e);
			this.component.setTitleScreen();
		}
		//System.out.println("trying to read.... hold on....");
		map = new MPMapClient(listener.read(), listener, this);
		//System.out.println("done reading from stream!");
		
		listener.start();
	}
	
	public void setIsLoaded(boolean isLoaded){
		this.isLoaded = isLoaded;
	}
	
	public void save(){
		Save.save(map, "save1");
	}
	
	public void save(String fileName){
		Save.save(map, fileName);
	}
	
	public boolean isLoaded(){
		return isLoaded;
	}
	
	public void load(){
		load("save1");
	}

	public void load(String nameFile){}

	public synchronized void update(){
		if(!pause){
			if(input.up.isPressed()) translationY += 5;
			if(input.down.isPressed()) translationY -= 5;
			if(input.left.isPressed()) translationX += 5;
			if(input.right.isPressed()) translationX -= 5;
			
			if(pointer != null){
				pointer.update(); 
				if(bar.showPopup == false){
					pointer = null;
				}
			}
		}
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
		
		if(entityPopup != null){
			entityPopup.update(input.getMouseX(), input.getMouseY());
			if(!selectedEntities.contains(entityPopup.getOwner())) entityPopup = null;
		}
		if(input.wasDragging() && (popup == null || !popup.isInPopup(input.mouseX, input.mouseY))){
			int x1 = input.mouseXOnClick, y1 = input.mouseYOnClick, x2 = input.mouseX, y2 = input.mouseY;
			selectedEntities.clear();
			LinkedList<Entity> inRange = (map.getEntities(x1, y1 , x2, y2, new Dimension(translationX, translationY)));
			ArrayList<Entity> structures = new ArrayList<Entity>();
			for( Entity e:inRange){
				if(e instanceof MovingEntity){
					selectedEntities.add(e);
				}else if(e instanceof Structure){
					structures.add(e);
				}
			}
			if(selectedEntities.size() == 0){
				selectedEntities.addAll(structures);
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
		
		chat.update();
		
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
		if(isLoaded()){
			for(String message: messagesToHandle){
				messageReceived(message);
			}
			messagesToHandle.clear();
		}
		
	}
	
	public synchronized void render(Graphics g){
		Point translation = new Point((int) translationX, (int) translationY);
		map.render(g, translation, new Dimension(getWidth(), getHeight()), getWidth(), getHeight());
		super.render(g);
		chat.render(g);
	}
	
	public void levelUp(){
		level++;
		switch(level){
		case 1:
			bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[5][6], this.bar.screen) {
				public void onLeftClick() {
					screen.pointer = new MousePointer(map, input, screen) {
						public Entity toBuild(Direction face) {
							return new Tent(map,screen, Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY), face,2);
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
						public Entity toBuild(Direction face) {
							return new SchoolI(map, screen, Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY),face);
						}
					};
					
				}

				public String getName() {
					return "School";
				}
			});
			bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[6][5], this) {
				public void onLeftClick() {
					screen.pointer = new MousePointer(map, input, screen) {
						public Entity toBuild(Direction face) {
							return new StoneDefenseTower(map, screen, Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY),face);
						}
					};
					
				}

				public String getName() {
					return "DefenseTower";
				}
			});
			bar.buildmenu.addButton(new MenuBarPopupButton(Images.buttons[3][5], this) {
				public void onLeftClick() {
					screen.pointer = new MousePointer(map, input, screen) {
						public Entity toBuild(Direction face) {
							return new Barracks(map, screen, Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY),face);
						}
					};
					
				}
				public String getName() {
					return "Barracks";
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
						public Entity toBuild(Direction face) {
							return new StoneMine(map, screen, Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY), Direction.SOUTH_WEST);
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
							return new Tent(map, screen,Util.getMapX(input.mouseX - translationX, input.mouseY - translationY), Util.getMapY(input.mouseX - translationX	, input.mouseY - translationY), face,2);
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
		this.popup = popup;
		if(popup == null){
			pause = false;
		}else pause = true;
	}

	public boolean isOnlyOnMap(int x, int y){
		return (x >0 && x < getWidth() && y > 0 && y < getHeight() && !bar.isInBar(x, y) && !statusBar.isInBar(x, y));
	}
	
	public void sendMessage(String message){
		listener.update(1 + " " + message);
	}
	
	public synchronized void messageReceived(String message){
		if(!isLoaded() ){
			messagesToHandle.add(message);
			return;
		}
		int messageID = Util.parseInt(Util.splitString(message).get(0));
		switch(messageID){
		case 1:
			chat.messageReceived(message);
			break;
		case 2:
			moveEntity(message);
			break;
		case 4:
			entityAdded(message);
			break;
		case 5:
			entityRemoved(message);
			break;
		case 6:
			EntityDamaged(message);
			break;
		case 7:
			professionAdded(message);
			break;
		case 9:
			shootArrow(message);
			break;
		case 10:
			pressBarracksPopup(message);
			break;
		default:
			new Exception("invalid message: " + messageID).printStackTrace();
		}
	}
	
	private void pressBarracksPopup(String message){
		int select = Util.parseInt(Util.splitString(message).get(1));
		int uniqueNumber = Util.parseInt(Util.splitString(message).get(2));
		int buttonID = Util.parseInt(Util.splitString(message).get(3));
		Entity e = map.getEntity(uniqueNumber);
		if(e instanceof Barracks){
			if(select == 1){
				((Barracks)e).getpopup().getButton(buttonID).amountToBuild++;
			}else if(select == 2){
				((Barracks)e).getpopup().getButton(buttonID).amountToBuild--;
			}
		}
	}
	
	private void shootArrow(String message){
		Entity start = map.getEntity(Util.parseInt(Util.splitString(message).get(1)));
		boolean fromTop = (Util.parseInt(Util.splitString(message).get(2)) == 1)? true : false;
		Entity end = map.getEntity(Util.parseInt(Util.splitString(message).get(3)));
		int horSpeed = Util.parseInt(Util.splitString(message).get(4));
		int maxDistance = Util.parseInt(Util.splitString(message).get(5));
		
		map.shootArrowFromHost(start, end, fromTop, horSpeed, maxDistance);
		
	}
	
	private void professionAdded(String update){
		int uniqueNumber = Util.parseInt(Util.splitString(update).get(1));
		int professionID = Util.parseInt(Util.splitString(update).get(2));
		if(professionID != 0){
			((PlayerEntity)map.getEntity(uniqueNumber)).setProfessionFromHost(Util.getProfession(professionID, (PlayerEntity) map.getEntity(uniqueNumber)));
		}else{
			System.out.println("professionID not valid: " + update);
		}
	}
	
	private void entityAdded(String entity){
		//System.out.println(entity);
		int ID = Util.parseInt(Util.splitString(entity).get(2));
		int uniqueNumber = Util.parseInt(Util.splitString(entity).get(3));
		int xPos = Util.parseInt(Util.splitString(entity).get(4));
		int yPos = Util.parseInt(Util.splitString(entity).get(5));
		int health = Util.parseInt(Util.splitString(entity).get(6));
		int number = Util.parseInt(Util.splitString(entity).get(7));
		int[] extraInfoOne = new int[number + 1];
		extraInfoOne[0] = number;
		int n = 8;
		for(int i = 0; i < number; i++){
			extraInfoOne[i+1] = Util.parseInt(Util.splitString(entity).get(n));
			//System.out.println(extraInfoOne[i]);
			n++;
		}
		int front = Util.parseInt(Util.splitString(entity).get(n++));
		Entity e = Util.getEntity(map, this, ID, xPos, yPos,health, extraInfoOne, uniqueNumber, front);
		if(Util.parseInt(Util.splitString(entity).get(1)) == 0) e.setOwned(false);
		e.screen = this;
		map.addEntityFromHost(e);
	
	}
	
	private void moveEntity(String entity){
		int oneOrTwo = Util.parseInt(Util.splitString(entity).get(1));
		int uniqueNumber = Util.parseInt(Util.splitString(entity).get(2));
		Entity e = map.getEntity(uniqueNumber);
		if(e instanceof MovingEntity){
			switch(oneOrTwo){
			case 1:
				((MovingEntity)e).moveToFromHost(new Point(Util.parseInt(Util.splitString(entity).get(3)),Util.parseInt(Util.splitString(entity).get(4))));
				break;
			case 2:
				((MovingEntity)e).moveToFromHost(map.getEntity(Util.parseInt(Util.splitString(entity).get(3))));
				break;
			case 3:
				((MovingEntity)e).followFromHost(map.getEntity(Util.parseInt(Util.splitString(entity).get(3))), Util.parseInt(Util.splitString(entity).get(3)));
				break;
			}
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
		String update = 2 + " " + 1 + " "+ movingEntity.uniqueNumber + " " + x + " " + y; 
		listener.update(update);
	}
	
	public void moveEntity(MovingEntity entity, Entity goal){
		String update = 2 + " " + 2 + " "+ entity.uniqueNumber + " " + goal.uniqueNumber; 
		listener.update(update);
	}
	
	public void followEntity(MovingEntity entity, Entity goal, int distance){
		String update = 2 + " " + 3 + " " + entity.uniqueNumber + " " + goal.uniqueNumber + " " + distance;
		listener.update(update);
	}
	
	public void addEntity(Entity e, int x, int y){
		int frontData = 0;
		if(e instanceof BasicStructure){
			frontData = (((BasicStructure)e).getFront() == Direction.SOUTH_EAST)? 1 : 2;
		}
		String update = 3 + " " + e.ID + " " + x + " " + y + " " + e.getHealth() + " " + e.getExtraOne() + " " + ((e.isOwnedByPlayer())? 1: 0) + " " + frontData ;
		listener.update(update);
	}
	
	public void damageEntity(Entity e, int damage){
		listener.update(6 + " " + e.uniqueNumber + " " + damage);
	}
	
	private void EntityDamaged(String message){
		Entity e = map.getEntity(Util.parseInt(Util.splitString(message).get(1)));
		if(e != null) {	
			e.damageFromHost(Util.parseInt(Util.splitString(message).get(2)));
		}else{
			System.out.println(Util.parseInt(Util.splitString(message).get(1)));
		}
	}
	
	public void removeEntity(Entity e){
		listener.update(5 + " " + e.uniqueNumber);
	}
	
	private void entityRemoved(String message){
		map.removeEntityFromHost(map.getEntity(Util.parseInt(Util.splitString(message).get(1))));
	}
	
	public void setProfession(PlayerEntity p, Profession prof){
		listener.update( 7 + " " + p.uniqueNumber + " " + Util.getProfessionID(prof));
	}

	public void startChopping(Entity owner) {
		listener.update(8 + " " + owner.uniqueNumber + " " + 5);
	}

	public void stopChopping(Entity owner) {
		listener.update(8 + " " + owner.uniqueNumber + " " + 6);
	}

	public void startMining(Entity owner) {
		listener.update(8 + " " + owner.uniqueNumber + " " + 3);
	}

	public void stopMining(Entity owner) {
		listener.update(8 + " " + owner.uniqueNumber + " " + 4);
	}

	public void startHunting(Entity owner) {
		listener.update(8 + " " + owner.uniqueNumber + " " + 1);
	}

	public void stopHunting(Entity owner) {
		listener.update(8 + " " + owner.uniqueNumber + " " + 2);
	}
	
	public void shootArrow(Entity start, Entity end, boolean fromTop, int horSpeed, int maxDistance){
		listener.update(9 + " " + start.uniqueNumber + " " + ( fromTop ? "1" : " 0" ) + " " + end.uniqueNumber + " " + horSpeed + " " + maxDistance);
	}
	
	public void barracksPopupButtonSelected(Button b, Barracks barrack){
		listener.update(10 + " 1 " + barrack.uniqueNumber + " " + b.getButtonID());
	}
	
	public void barracksPopupButtonDeselected(Button b, Barracks barrack){
		listener.update(10 + " 2 " + barrack.uniqueNumber + " " + b.getButtonID());
	}
}

