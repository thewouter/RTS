package walnoot.rtsgame.multiplayer.host;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.rest.Inventory;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.GameScreen;

public class Player extends GameScreen {
	
	public Inventory inventory;
	public InputListener input;
	public MPHost host;
	private boolean hasLoaded = false;
	private String firstUpdate = "";
	
	public Player(RTSComponent component, InputHandler input, MPHost host, BufferedReader r, PrintStream p, List<Entity> entities) {
		super(component, input);
		
		this.input = new InputListener(this, r, p);
		this.host = host;
		
		String mapInString = host.map.getLength() + " " + host.map.amountSheepGroups + " ";
		
		for(int x = 0 ;x < host.map.getLength(); x++){
			for(int y = 0; y < host.map.getWidth(); y++){
				mapInString = mapInString + host.map.surface[x][y].getID() + " ";
			}
		}
		
		mapInString = 2 + " " + mapInString + host.map.entities.size() + " ";
		
		for(Entity e: entities){
			mapInString = mapInString + e.ID + " " + e.xPos + " " + e.yPos + " " + e.getHealth() + " " + e.getExtraOne() + " ";
		}
		
		this.input.send(mapInString);
		
		inventory = new Inventory(this);
		
		this.input.read(); 		// wait for confirmation from the client that the map is decoded.
		
		hasLoaded = true;
		
		this.input.start();
	}

	public void save() {}

	public void save(String fileName) {}

	public void load() {}

	public void load(String nameFile) {}

	public void update() {}
	
	public void update(String update){
		if(hasLoaded) input.send(update);
		else{
			System.out.println("test");
			ArrayList<String> inStrings = Util.splitString(update);
			int moves = Util.parseInt(inStrings.get(1));
			System.out.println(moves);
		}
	}
	
	public void InputReceived(String message){
		
	}
	
	public void sendTextMessage(String message){
		 input.send(1 + " " + message);
	}
	
	public void quit() {
		host.removePlayer(this);
	}
	
}
