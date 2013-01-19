package walnoot.rtsgame.multiplayer.client;

import java.awt.Point;
import java.util.ArrayList;

import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.entities.players.professions.Founder;
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
		int mapSize = Util.parseInt(mapInStrings.get(1));
		counter = 3;
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
		//System.out.println("entities: " + amountEntities);
		
		for(int i = 0; i < amountEntities; i++){
			String entity = mapInStrings.get(counter++) + " " + mapInStrings.get(counter++) + " " + mapInStrings.get(counter++) + " " + mapInStrings.get(counter++) + " " + mapInStrings.get(counter++) + " " + mapInStrings.get(counter++); 
			Entity e = Util.getEntity(map, entity,screen);
			e.screen = null;
			map.addEntityFromHost(e);
			
		}
		int amountMovements = Util.parseInt(mapInStrings.get(counter++));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < amountMovements; i++){
			int uniqueNumber = Util.parseInt(mapInStrings.get(counter++));
			//System.out.println("uN: " + uniqueNumber);
			((MovingEntity)map.getEntity(uniqueNumber)).moveToFromHost(new Point(Util.parseInt(mapInStrings.get(counter++)),Util.parseInt(mapInStrings.get(counter++))));
		}
		//System.out.println(6);
		input.send("1 Received!");
		
		PlayerEntity p = new PlayerEntity(map, screen, 11, 11, null);
		p.setProfession(new Founder(p));
		map.addEntity(p);
	}
	
	public int checkProgress() {
		return (int) ((counter * 100) / length);
	}
	
}
