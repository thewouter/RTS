package walnoot.rtsgame.multiplayer.host;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.List;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.rest.Inventory;
import walnoot.rtsgame.screen.GameScreen;

public class Player extends GameScreen {
	
	public Inventory inventory;
	public InputListener input;
	public MPHost host;
	public final int ID;
	private boolean isLoaded = false;
	
	private static int PLAYERID = 0;
	
	private static int getNextPlayerID(){
		PLAYERID +=1;
		return PLAYERID;
	}
	
	public Player(RTSComponent component, InputHandler input, MPHost host, BufferedReader r, PrintStream p, List<Entity> entities) {
		super(component, input);
		inventory = host.inventory;
		
		ID = getNextPlayerID();
		
		this.input = new InputListener(this, r, p);
		this.host = host;
		
		this.input.send(host.map.getData());
		
		inventory = new Inventory(this);
		
		this.input.read(); 		// wait for confirmation from the client that the map has been decoded.
		
		this.input.start();
		isLoaded = true;
	}

	public void save() {}

	public void save(String fileName) {}

	public void load() {}

	public void load(String nameFile) {}

	public void update() {}
	
	public void update(String update){
		if(isLoaded()) input.send(update);
	}
	
	public void InputReceived(String message){
		host.messageReceived(message, this);
	}
	
	public void sendTextMessage(String message){
		 input.send(message);
	}
	
	public void quit() {
		host.removePlayer(this);
		input.quit();
	}
	
	public boolean isLoaded(){
		return isLoaded;
	}
	
}
