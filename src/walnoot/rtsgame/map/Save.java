package walnoot.rtsgame.map;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Save {
	Map map;
	
	public static String FILENAME;
	public Save(Map map, String name){
		FILENAME = name;
		this.map = map;
	}
	
	public void save(){
		File file = new File(FILENAME);
		
		try{
			FileOutputStream file_output = new FileOutputStream(file);
			DataOutputStream data_out = new DataOutputStream(file_output);
			int mapWidth = map.getWidth();
			int mapHeight = map.getLength();
			
			
			data_out.writeInt(mapWidth);
			data_out.writeInt(mapHeight);
			
			file_output.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/** herlaad options.dat en vervangt variabelen */
	public void load(){
		File file = new File(FILENAME);
		
		try{
			
			//hiermee kun je date op files schrijven
			FileInputStream file_input = new FileInputStream(file);
			DataInputStream data_in = new DataInputStream(file_input);
			
			try{
				System.out.println(data_in.readInt());
				System.out.println(data_in.readInt());
				
			}catch(EOFException e){ //als hij bij het einde van de file is
				System.out.println("end of file");
			}
			
		}catch(Exception e){
			System.out.println("Error! : " + e);
		}
	}

}
