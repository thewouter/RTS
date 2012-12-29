package walnoot.rtsgame.multiplayer.host;

import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.popups.screenpopup.ScreenPopup;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupButton;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.Screen;


public class MPHost extends Screen{
	
	public int port;
	
	public int translationX, translationY;
	
	public MPMapHost map;
	
	public LinkedList<Player> players = new LinkedList<Player>();
	private ClientHandler clientHandler;
	
	private String toSend = "" ;
	private String entitymoves = "";
	private String entityAdds = "";
	private String entityDeletes = "";
	private int adds = 0, deletes = 0, moves = 0;

	private LinkedList<Player> toRemove = new LinkedList<Player>();
	private LinkedList<Player> toAdd = new LinkedList<Player>();
	
	
			
	public MPHost(RTSComponent component, InputHandler input, int port) {
		super(component , input);
		
		this.port = port;
		
		map =  new MPMapHost(256, this);
		map.addEntity(new PlayerEntity(map, null, 10, 10, null));
		
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
	
	public void entityAdded(int ID, int xPos, int yPos){
		String entityAdds = " " + ID + " " + xPos + " " + yPos;
		this.entityAdds = this.entityAdds + entityAdds;
		adds++;
	}
	
	public void entityRemoved(int index){
		entityDeletes = entityDeletes + " " + index;
		deletes++;
	}

	public void render(Graphics g) {
		map.render(g, new Point(translationX, translationY), component.getSize(), component.getWidth(), component.getHeight());
	}

	public void messageReceived(String message) {
		System.out.println(message);
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
			addEntity(message);
			break;
		}
	}

	private void addEntity(String message) {
		int ID = Util.parseInt(Util.splitString(message).get(1));
		int xPos = Util.parseInt(Util.splitString(message).get(2));
		int yPos = Util.parseInt(Util.splitString(message).get(3));
		int extraInfoOne = Util.parseInt(Util.splitString(message).get(4));
		map.addEntity(Util.getEntity(map, ID, xPos, yPos, extraInfoOne));
	}
	
	public void entityAdded(Entity e){
		int ID = e.ID;
		int xPos = e.xPos;
		int yPos = e.yPos;
		int uniqueNumber = e.uniqueNumber;
		int extraInfoOne = e.getExtraOne();
		String update = 4 + " " + ID + " " + uniqueNumber + " " + xPos + " " + yPos + " " + extraInfoOne;
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
	
}

