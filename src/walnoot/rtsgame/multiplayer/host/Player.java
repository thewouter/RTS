package walnoot.rtsgame.multiplayer.host;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.rest.Inventory;
import walnoot.rtsgame.screen.GameScreen;

public class Player extends GameScreen {
	
	public Inventory inventory;
	public InputListener input;
	
	public Player(RTSComponent component, InputHandler input, MPHost host, BufferedReader r, PrintStream p, List<Entity> entities) {
		super(component, input);
		
		this.input = new InputListener(this, r, p);
		
		this.input.start();
		
		String mapInString = host.map.getLength() + " " + host.map.amountSheepGroups + " ";
		
		for(int x = 0 ;x < host.map.getLength(); x++){
			for(int y = 0; y < host.map.getWidth(); y++){
				mapInString = mapInString + host.map.surface[x][y].getID() + " ";
			}
		}
		
		mapInString = mapInString + host.map.entities.size() + " ";
		
		for(Entity e: entities){
			mapInString = mapInString + e.ID + " " + e.xPos + " " + e.yPos + " " + e.getHealth() + " " + e.getExtraOne() + " ";
		}
		
		this.input.send(mapInString);
		
		inventory = new Inventory(this);
	}

	public void save() {}

	public void save(String fileName) {}

	public void load() {}

	public void load(String nameFile) {}

	public void update() {}
	
	public void update(String update){
		input.send(update);
	}
	
	public void InputReceived(String message){
		
	}
	
	public void sendTextMessage(String message){
		 input.send(1 + " " + message);
	}
	
}
