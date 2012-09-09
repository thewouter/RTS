package walnoot.rtsgame.rest;

import java.util.ArrayList;
import java.util.Random;

import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.DeerEntity;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.SheepEntity;
import walnoot.rtsgame.map.entities.SnakeEntity;
import walnoot.rtsgame.map.entities.players.HunterEntity;
import walnoot.rtsgame.map.entities.players.MinerEntity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.structures.natural.GoldMine;
import walnoot.rtsgame.map.structures.natural.TreeStructure;
import walnoot.rtsgame.map.structures.nonnatural.BaseOfOperations;
import walnoot.rtsgame.map.structures.nonnatural.CampFireStructure;
import walnoot.rtsgame.map.structures.nonnatural.TentIStructure;
import walnoot.rtsgame.map.tiles.Tile;

/**
 * Class met handige static methods en waarden
 */

public class Util {
	public static final Random RANDOM = new Random();
	public static final NameGenerator NAME_GEN = new NameGenerator();
	
	public static int getScreenX(int mapX, int mapY){
		return (mapX - mapY) * (-Tile.WIDTH / 2);
	}
	
	public static int getScreenY(int mapX, int mapY){
		return (mapX + mapY) * (Tile.HEIGHT / 2);
	}
	
	public static int getMapX(int screenX, int screenY){
		return (int) Math.floor((screenY / 16.0) - ((screenX - Tile.WIDTH / 2) / 32.0));
	}
	
	public static int getMapY(int screenX, int screenY){
		return (int) Math.floor((screenY / 16.0) + ((screenX - Tile.WIDTH / 2) / 32.0));
	}
	
	public static int abs(int x) {
		return (x > 0) ? x : -x;
	}
	
	public static int getDistance(int x1, int y1, int x2, int y2){
		return (int) Math.sqrt(Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
	}
	
	public static ArrayList<String> splitString(String string){
		char[] inChars = string.toCharArray();		
		ArrayList<String> result = new ArrayList<String>();
		int lastSpace = -1;
		for(int i = 0; i < inChars.length; i++){
			if((int)inChars[i]==32){
				String s = "";
				for(int ii = lastSpace + 1; ii < i; ii++){
					s = s + inChars[ii];
				}
				s.trim();
				result.add(s);
				lastSpace = i;
			}
		}
		String s = "";
		for(int i = lastSpace + 1; i < inChars.length; i++){
			s = s + inChars[i];
		}
		s.trim();
		result.add(s);
		return result;
	}
	
	public static int parseInt( final String s ){
	    if (s == null )
	        throw new NumberFormatException( "Null string" );
	    else if (s == ""){
	    	System.out.println("string is '' (empty) ");
	    	return 0;
	    }
	    
	    int num  = 0;
	    int sign = -1;
	    final int len  = s.length();
	    final char ch  = s.charAt(0);
	    if ( ch == '-' ){
	        if ( len == 1 )
	            throw new NumberFormatException( "Missing digits:  " + s );
	        sign = 1;
	    }else{
	        final int d = ch - '0';
	        if ( d < 0 || d > 9 )
	            throw new NumberFormatException( "Malformed:  " + s );
	        num = -d;
	    }
	    
	    final int max = (sign == -1) ? -Integer.MAX_VALUE : Integer.MIN_VALUE;
	    final int multmax = max / 10;
	    int i = 1;
	    while ( i < len ){
	        int d = s.charAt(i++) - '0';
	        if ( d < 0 || d > 9 )
	            throw new NumberFormatException( "Malformed:  " + s );
	        if ( num < multmax )
	            throw new NumberFormatException( "Over/underflow:  " + s );
	        num *= 10;
	        if ( num < (max+d) )
	            throw new NumberFormatException( "Over/underflow:  " + s );
	        num -= d;
	    }
	    
	    return sign * num;
	}
	
	public static Entity getEntity(Map map, int ID, int xPos, int yPos, int health, int extraInfoOne){
		if(ID >= 300){				//mines
			switch(ID){
			case 300:
				return new GoldMine(map, null, xPos, yPos, extraInfoOne, health);
			}	
		}else if(ID >= 200){		//structures
			switch(ID){
			case 200:
				return new CampFireStructure(map, null,  xPos, yPos, health);
				
			case 201:
				return new TentIStructure(map, null,  xPos, yPos, health);
				
			case 202:
				return new TreeStructure(map, null,  xPos, yPos, health);
				
			case 203:
				return new BaseOfOperations(map, null,  xPos, yPos, health);
				
			}
		}else if(ID >= 100){		//movingEntities
			switch(ID){
			case 100:
				return new SnakeEntity(map, null,  xPos, yPos, health);
				
			case 101:
				return new SheepEntity(map, null,  xPos, yPos, health);
				
			case 102:
				return new PlayerEntity(map, null,  xPos, yPos, null);
				
			case 103:
				return new DeerEntity(map, null,  xPos, yPos, health);
				
			case 104:
				return new HunterEntity(map, null,  xPos, yPos, null, health);
				
			case 105:
				return new MinerEntity(map, null,  xPos, yPos, null, health);
				
			}
		}
		System.out.println("no entity found!");
		return null;
	}
	
	public static Entity getEntity(String entity, Map map){
		
		ArrayList<String> entityInstrings = splitString(entity);
		int ID = parseInt(entityInstrings.get(0));
		int xPos = parseInt(entityInstrings.get(1));
		int yPos = parseInt(entityInstrings.get(2));
		int health = parseInt(entityInstrings.get(3));
		int extraInfoOne = parseInt(entityInstrings.get(4));
		
		return getEntity(map, ID, xPos, yPos, health, extraInfoOne);
	}
}	
	