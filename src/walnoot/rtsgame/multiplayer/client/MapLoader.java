package walnoot.rtsgame.multiplayer.client;

import java.util.ArrayList;

import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.rest.Util;

public class MapLoader extends Thread {
	
	String mapInString;
	MPMapClient map;
	InputListener input;
	
	int counter, length = 1;
	
	public MapLoader(String mapInString, MPMapClient map, InputListener input){
		this.map = map;
		this.mapInString = mapInString;
		this.input = input;
		
	}
	
	public void run(){
		ArrayList<String> mapInStrings =Util.splitString(mapInString);
		length = mapInStrings.size();
		int mapSize = Util.parseInt(mapInStrings.get(1));
		counter = 3;
		if(mapSize == 0){
			System.out.println(mapInStrings);
			System.out.println(mapInString);
		}
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
		
		for(int i = 0; i < amountEntities; i++){
			String entity = mapInStrings.get(counter++) + " " + mapInStrings.get(counter++) + " " + mapInStrings.get(counter++) + " " + mapInStrings.get(counter++) + " " + mapInStrings.get(counter++); 
			Entity e = Util.getEntity(entity, map);
			map.addEntityFromHost(e);
		}
		
		map.hasLoaded = true;
		input.send("nothing usefull ;)");
		System.out.println("send");
	}
	
	public int checkProgress() {
		return (int) ((counter * 100) / length);
	}
	
}
