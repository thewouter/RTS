package walnoot.rtsgame.map;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import walnoot.rtsgame.map.entities.DeerEntity;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.PlayerEntity;
import walnoot.rtsgame.map.entities.SheepEntity;
import walnoot.rtsgame.map.entities.SnakeEntity;
import walnoot.rtsgame.map.structures.CampFireStructure;
import walnoot.rtsgame.map.structures.TentStructure;
import walnoot.rtsgame.map.structures.TreeStructure;
import walnoot.rtsgame.map.tiles.Tile;

public class Save {
	
	private static String EXTENSION = ".rts";
	
	public static void save(Map map, String nameFile){
		String fileName = "saves/" + nameFile + EXTENSION;
		File file = new File(fileName);
		
		try{
			FileOutputStream file_output = new FileOutputStream(file);
			DataOutputStream data_out = new DataOutputStream(file_output);
			int mapWidth = map.getWidth();
			int mapHeight = map.getLength();
			
			data_out.writeInt(mapWidth);				// first  2 ints are the map width and height
			data_out.writeInt(mapHeight);
			
			for(int x = 0; x < mapHeight; x++){			// after that the ID's of all the tiles
				for(int y = 0; y < mapWidth; y++){
					data_out.writeInt(map.getTile(x, y).getID());
				}
			}
			
			data_out.writeInt(map.entities.size());		//the amount of entities
			
			
			for(Entity e: map.entities){		  		// then the Entities
				data_out.writeInt(e.ID);
				data_out.writeInt(e.getxPos());
				data_out.writeInt(e.getyPos());
				data_out.writeInt(e.getHealth());
				
			}
			
			file_output.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	public static Map load(String nameFile){
		String fileName  = "saves/" + nameFile + EXTENSION;
		File file = new File(fileName);
		Map map = null;
		
		try{
			
			FileInputStream file_input = new FileInputStream(file);
			DataInputStream data_in = new DataInputStream(file_input);
			
			try{
				
				int width = data_in.readInt();
				int height = data_in.readInt();
				
				map = new Map(width, true);
				
				for(int x = 0; x < height; x++){
					for( int y = 0; y < width; y++){
						int ID = data_in.readInt();
						map.changeTile(x, y, Tile.getTile(ID));
					}
				}
				
				int amountEntities = data_in.readInt();
				
				
				
				for(int i = 0; i < amountEntities; i++){
					int ID = data_in.readInt();
					int xPos = data_in.readInt();
					int yPos = data_in.readInt();
					int health = data_in.readInt();
					if(ID >= 200){
						switch(ID){
						case 200:
							map.addEntity(new CampFireStructure(map, xPos, yPos, health));
							break;
						case 201:
							map.addEntity(new TentStructure(map, xPos, yPos, health));
							break;
						case 202:
							map.addEntity(new TreeStructure(map, xPos, yPos, health));
							break;
						}
					}else if(ID >= 100){
						switch(ID){
						case 100:
							map.addEntity(new SnakeEntity(map, xPos, yPos, health));
							break;
						case 101:
							map.addEntity(new SheepEntity(map, xPos, yPos, health));
							break;
						case 102:
							map.addEntity(new PlayerEntity(map, xPos, yPos, health));
							break;
						case 103:
							map.addEntity(new DeerEntity(map, xPos, yPos, health));
							break;
						}
					}
				}
				
				
				
			}catch(EOFException e){ //als hij bij het einde van de file is
				System.out.println("end of file");
			}
			data_in.close();
			
		}catch(Exception e){
			System.out.println("file not present");
			//e.printStackTrace();
		}
		
		
		return map;
		
		
	}

}