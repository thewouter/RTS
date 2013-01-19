package walnoot.rtsgame.multiplayer.host;

import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.rest.Inventory;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.Screen;


public class MPHost extends Screen{
	
	public int port;
	
	public int translationX, translationY;
	
	public MPMapHost map;

	public Inventory inventory =new Inventory(this);
	
	public LinkedList<Player> players = new LinkedList<Player>();
	private ClientHandler clientHandler;

	private LinkedList<Player> toRemove = new LinkedList<Player>();
	private LinkedList<Player> toAdd = new LinkedList<Player>();
	
	
			
	public MPHost(RTSComponent component, InputHandler input, int port) {
		super(component , input);
		
		this.port = port;
		
		map =  new MPMapHost(256, this);
		
		clientHandler = new ClientHandler(this);
		clientHandler.start();
	}
	
	public void addPlayer(Player p){
		toAdd.add(p);
	}
	
	public void removePlayer(Player p){
		toRemove.add(p);
	}
	
	public void update(){
		map.update(translationX, translationY, component.getWidth(), component.getHeight());
		
		if(input.up.isPressed()) translationY += 5;
		if(input.down.isPressed()) translationY -= 5;
		if(input.left.isPressed()) translationX += 5;
		if(input.right.isPressed()) translationX -= 5;
		
		super.update();
		
		players.removeAll(toRemove);
		players.addAll(toAdd);
		toRemove.clear();
		toAdd.clear();
	}
	
	public void entityMoved(Entity e, int newX, int newY){
		String toSend = "2 " + e.uniqueNumber + " " + newX + " " + newY; 
		for(Player p: players){
			p.update(toSend);
		}
	}
	
	public void entityRemoved(int uniqueNumber){
		for(Player p:players){
			p.update(5 + " " + uniqueNumber);
		}
	}

	public void render(Graphics g) {
		map.render(g, new Point(translationX, translationY), component.getSize(), component.getWidth(), component.getHeight());
	}

	public void messageReceived(String message, Player owner) {
		//System.out.println(message);
		switch(Util.parseInt(Util.splitString(message).get(0))){
		case 2:
			moveEntity(message);
			break;
		case 1:
			for(Player p: players){
				p.sendTextMessage(message);
			}
			break;
		case 3:
			addEntity(message, owner);
			System.out.println(message);
			break;
		case 5:
			removeEntity(message);
			break;
		}
	}

	private void addEntity(String message, Player owner) {
		int ID = Util.parseInt(Util.splitString(message).get(1));
		int xPos = Util.parseInt(Util.splitString(message).get(2));
		int yPos = Util.parseInt(Util.splitString(message).get(3));
		int extraInfoOne = Util.parseInt(Util.splitString(message).get(4));
		Entity e = Util.getEntity(map, ID, xPos, yPos, extraInfoOne);
		e.screen = owner;
		map.addEntity(e);
		
	}
	
	public void entityAdded(Entity e, Player owner){
		int ID = e.ID;
		int xPos = e.xPos;
		int yPos = e.yPos;
		int uniqueNumber = e.uniqueNumber;
		int extraInfoOne = e.getExtraOne();
		String update = 4 + " " + 0 + " " + ID + " " + uniqueNumber + " " + xPos + " " + yPos + " " + extraInfoOne;
		for(Player p: players){
			if(owner == p){
				p.update(4 + " " + 1 + " " + ID + " " + uniqueNumber + " " + xPos + " " + yPos + " " + extraInfoOne);
				continue;
			}
			p.update(update);
		}
	}

	private void moveEntity(String message){
		Entity e = map.getEntity(Util.parseInt(Util.splitString(message).get(1)));
		if(e instanceof MovingEntity){
			((MovingEntity)e).moveTo(new Point(Util.parseInt(Util.splitString(message).get(2)),Util.parseInt(Util.splitString(message).get(3))));
		}
	}
	
	private void removeEntity(String message){
		map.removeEntity(map.getEntity(Util.parseInt(Util.splitString(message).get(1))));
	}
	
}

