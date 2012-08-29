package walnoot.rtsgame.multiplayer.host;

import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.screen.GameScreen;
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
	
	
			
	public MPHost(RTSComponent component, InputHandler input, int port) {
		super(component , input);
		
		this.port = port;
		
		map =  new MPMapHost(256, this);
		
		
		clientHandler = new ClientHandler(this);
		clientHandler.start();
	}
	
	public void addPlayer(Player p){
		players.add(p);
	}
	
	public void removePlayer(Player p){
		players.remove(p);
	}
	
	public void update(){
		toSend = 0 + " " + adds + entityAdds + deletes + entityDeletes + moves + entitymoves;
		adds = 0;
		moves = 0;
		deletes = 0;
		entityAdds = " ";
		entitymoves = " ";
		entityDeletes = " ";
		for(Player p: players){
			p.update(toSend);
		}
		
		map.update(translationX, translationY, component.getWidth(), component.getHeight());
		
		if(input.up.isPressed()) translationY += 5;
		if(input.down.isPressed()) translationY -= 5;
		if(input.left.isPressed()) translationX += 5;
		if(input.right.isPressed()) translationX -= 5;
		
		super.update();
	}
	
	public void entityMoved(int index, int oldX, int oldY, int newX, int newY){
		entitymoves = entitymoves + index + " " + oldX + " " + oldY + " " + newX + " " + newY; 
		moves++;
	}
	
	public void entityAdded(int ID, int xPos, int yPos){
		entityAdds = entityAdds + ID + " " + xPos + " " + yPos;
		adds++;
	}
	
	public void entityRemoved(int index){
		entityDeletes = entityDeletes + index + " ";
		deletes++;
	}

	public void render(Graphics g) {
		map.render(g, new Point(translationX, translationY), component.getSize(), component.getWidth(), component.getHeight());
	}
	
}

