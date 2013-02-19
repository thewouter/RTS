package walnoot.rtsgame.multiplayer.host;

import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.entities.players.professions.Hunter;
import walnoot.rtsgame.map.entities.players.professions.LumberJacker;
import walnoot.rtsgame.map.entities.players.professions.Miner;
import walnoot.rtsgame.map.entities.players.professions.Profession;
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
		for(Player p: toAdd){
			PlayerEntity player = new PlayerEntity(map, p, 11, 11, null);
			player.owner = p;
			map.addEntity(player);
		}
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
			break;
		case 5:
			removeEntity(message);
			break;
		case 6:
			damageEntity(message);
			break;
		case 7:
			professionSet(message);
			break;
		case 8:
			professionMethod(message);
			break;
		}
	}
	
	private void professionMethod(String message){
		int uniqueNumber = Util.parseInt(Util.splitString(message).get(1));
		int method = Util.parseInt(Util.splitString(message).get(2));
		Entity e = map.getEntity(uniqueNumber);
		if(e instanceof PlayerEntity){
			PlayerEntity player = (PlayerEntity)e;
			Profession profession = player.getProfession();
			if(profession == null){
				System.out.println("profession = null!");
				return;
			}
			switch(method){
			case 1:
				if(profession instanceof Hunter){
					((Hunter)profession).setIsHunting(true);
				}else{
					System.out.println("profession isn't Hunter");
				}
				break;
			case 2:
				if(profession instanceof Hunter){
					((Hunter)profession).setIsHunting(false);
				}else{
					System.out.println("profession isn't Hunter");
				}
				break;
			case 3:
				if(profession instanceof Miner){
					((Miner)profession).setIsMining(true);
				}else{
					System.out.println("profession isn't Miner");
				}
				break;
			case 4:
				if(profession instanceof Miner){
					((Miner)profession).setIsMining(false);
				}else{
					System.out.println("profession isn't Miner");
				}
				break;
			case 5:
				if(profession instanceof LumberJacker){
					((LumberJacker)profession).setIsChopping(true);
				}else{
					System.out.println("profession isn't LumerJacker");
				}
				break;
			case 6:
				if(profession instanceof LumberJacker){
					((LumberJacker)profession).setIsChopping(false);
				}else{
					System.out.println("profession isn't LumberJacker");
				}
				break;
			}
		}
	}

	private void addEntity(String message, Player owner) {
		int ID = Util.parseInt(Util.splitString(message).get(1));
		int xPos = Util.parseInt(Util.splitString(message).get(2));
		int yPos = Util.parseInt(Util.splitString(message).get(3));
		int health = Util.parseInt(Util.splitString(message).get(4));
		int extraInfoOne = Util.parseInt(Util.splitString(message).get(5));
		int isOwnedByPlayer = Util.parseInt(Util.splitString(message).get(6));
		Entity e = Util.getEntity(map, ID, xPos, yPos, extraInfoOne);
		if(isOwnedByPlayer == 1) e.owner = owner;
		e.screen = owner;
		e.setHealth(health);
		map.addEntity(e);
		
	}
	
	private void damageEntity(String message){
		map.getEntity(Util.parseInt(Util.splitString(message).get(1))).damage(Util.parseInt(Util.splitString(message).get(2)));
	}
	
	public void entityAdded(Entity e, Player owner){
		int ID = e.ID;
		int xPos = e.xPos;
		int yPos = e.yPos;
		int uniqueNumber = e.uniqueNumber;
		int health = e.getHealth();
		int extraInfoOne = e.getExtraOne();
		String update = 4 + " " + 0 + " " + ID + " " + uniqueNumber + " " + xPos + " " + yPos + " " + health + " " + extraInfoOne;
		for(Player p: players){
			if(e.owner == p){
				p.update(4 + " " + 1 + " " + ID + " " + uniqueNumber + " " + xPos + " " + yPos + " " + health + " " + extraInfoOne);
				continue;
			}
			p.update(update);
		}
	}
	
	public void entityDamaged(Entity e, int damage){
		int uniqueNumber = e.uniqueNumber;
		String update = 6 + " " + uniqueNumber + " " + damage;
		for(Player p: players){
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

	public void addProfession(PlayerEntity p, Profession prof) {
		String update = 7 + " " + p.uniqueNumber + " " + Util.getProfessionID(prof);
		for(Player pl: players){
			pl.update(update);
		}
	}
	
	private void professionSet(String message){
		int uniqueNumber = Util.parseInt(Util.splitString(message).get(1));
		int ProfessionID = Util.parseInt(Util.splitString(message).get(2));
		Profession.setProfession(ProfessionID, (PlayerEntity)map.getEntity(uniqueNumber));
	}
	
}

