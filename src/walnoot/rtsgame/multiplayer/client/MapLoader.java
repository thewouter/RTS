package walnoot.rtsgame.multiplayer.client;

import java.util.ArrayList;

import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.rest.Util;

public class MapLoader extends Thread {
	
	String mapInString;
	MPMapClient map;
	
	int counter, length = 1;
	
	public MapLoader(String mapInString, MPMapClient map){
		this.map = map;
		this.mapInString = mapInString;
		
	}
	
	public void run(){
		ArrayList<String> mapInStrings =Util.splitString(mapInString);
		length = mapInStrings.size();
		int mapSize = Util.parseInt(mapInStrings.get(0));
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
		
		for(int i = 0; i < amountEntities; i++){
			String entity = mapInStrings.get(counter++) + " " + mapInStrings.get(counter++) + " " + mapInStrings.get(counter++) + " " + mapInStrings.get(counter++) + " " + mapInStrings.get(counter++); 
			Entity e = Util.getEntity(entity, map);
			map.addEntityFromHost(e);
		}
		
		map.hasLoaded = true;
	}
	
	public int checkProgress() {
		return (int) ((counter * 100) / length);
	}
	
}
