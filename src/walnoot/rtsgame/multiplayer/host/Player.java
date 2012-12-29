package walnoot.rtsgame.multiplayer.host;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.rest.Inventory;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.GameScreen;

public class Player extends GameScreen {
	
	public Inventory inventory;
	public InputListener input;
	public MPHost host;
	
	public Player(RTSComponent component, InputHandler input, MPHost host, BufferedReader r, PrintStream p, List<Entity> entities) {
		super(component, input);
		
		this.input = new InputListener(this, r, p);
		this.host = host;
		
		//add size map
		String mapInString = host.map.getLength() + " " + host.map.amountSheepGroups + " ";
		
		//add map
		for(int x = 0 ;x < host.map.getLength(); x++){
			for(int y = 0; y < host.map.getWidth(); y++){
				mapInString = mapInString + host.map.surface[x][y].getID() + " ";
			}
		}
		
		//add number of entities
		mapInString = 2 + " " + mapInString + entities.size() + " ";
		
		System.out.println(entities.size());
		
		//add entities
		for(Entity e: entities){
			mapInString = mapInString + e.ID + " " + e.xPos + " " + e.yPos + " " + e.getHealth() + " " + e.getExtraOne() + " " + e.uniqueNumber + " ";
		}
		
		//add movement entities
		String movements = "";
		int numberOfMovements = 0;
		for(Entity e:entities){
			if(e instanceof MovingEntity && ((MovingEntity)e).isMoving()){
				numberOfMovements++;
				movements = movements + " " + e.uniqueNumber + " " + ((MovingEntity)e).getEndPoint().x + " " + ((MovingEntity)e).getEndPoint().y;
			}
		}
		mapInString = mapInString + numberOfMovements + movements;
		
		this.input.send(mapInString);
		
		inventory = new Inventory(this);
		
		this.input.read(); 		// wait for confirmation from the client that the map is decoded.
		
		this.input.start();
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
		host.messageReceived(message);
	}
	
	public void sendTextMessage(String message){
		 input.send(message);
	}
	
	public void quit() {
		host.removePlayer(this);
		input.quit();
	}
	
}
