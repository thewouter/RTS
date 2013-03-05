package walnoot.rtsgame.multiplayer.client;

import java.awt.Point;
import java.util.ArrayList;

import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.MPGameScreen;

public class MapLoader extends Thread {
	
	String mapInString;
	MPMapClient map;
	InputListener input;
	MPGameScreen screen;
	int counter, length = 1;
	
	public MapLoader(String mapInString, MPMapClient map, InputListener input, MPGameScreen screen){
		this.map = map;
		this.mapInString = mapInString;
		this.input = input;
		this.screen = screen;
		
	}
	
	public void run(){
		ArrayList<String> mapInStrings =Util.splitString(mapInString);
		length = mapInStrings.size();
		int mapSize = Util.parseInt(mapInStrings.get(0));
		//System.out.println(mapSize);
		counter = 2;
		for(int x = 0 ; x < mapSize ; x++){
			for(int y = 0; y < mapSize; y++, counter++){
				String s = mapInStrings.get(counter);
				int ID = Util.parseInt(s);
				Tile t;
				switch(ID){
				case 0:
					t = Tile.grass2;
					break;
				case 1:
					t = Tile.grass1;
					break;
				case 2:
					t = Tile.water1;
					break;
				case 16:
					t = Tile.sand1;
					break;
				default:
					t = Tile.sand1;
					break;
				}
				map.changeTile(x, y, t);
			}
		}
		int amountEntities = Util.parseInt(mapInStrings.get(counter++));
		//System.out.println(amountEntities);
		//System.out.println(screen);
		for(int i = 0; i < amountEntities; i++){
			String entity = mapInStrings.get(counter++) + " " + mapInStrings.get(counter++) + " " + mapInStrings.get(counter++) + " " + mapInStrings.get(counter++) + " ";
			int number = Util.parseInt(mapInStrings.get(counter++));
			entity = entity + number + " ";
			for(int j = 0; j < number; j++){
				entity = entity + mapInStrings.get(counter++) + " ";
			}
			entity = entity + mapInStrings.get(counter++); 
			Entity e = Util.getEntity(map, entity, screen);
			e.isOwnedByPlayer = false;
			map.addEntityFromHost(e);
		}
		int amountMovements = Util.parseInt(mapInStrings.get(counter++));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//System.out.println(amountMovements + " \n");
		
		for (int i = 0; i < amountMovements; i++){
			int uniqueNumber = Util.parseInt(mapInStrings.get(counter++));
			((MovingEntity)map.getEntity(uniqueNumber)).moveToFromHost(new Point(Util.parseInt(mapInStrings.get(counter++)),Util.parseInt(mapInStrings.get(counter++))));
			//System.out.println(uniqueNumber);
		}
		input.send("1 Received!");
		screen.setIsLoaded(true);
	}
	
	public int checkProgress() {
		return (int) ((counter * 100) / length);
	}
	
}
