package walnoot.rtsgame.rest;

import java.util.ArrayList;
import java.util.Random;

import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.DeerEntity;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.SheepEntity;
import walnoot.rtsgame.map.entities.SnakeEntity;
import walnoot.rtsgame.map.entities.players.AlertComponent;
import walnoot.rtsgame.map.entities.players.Bow;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.entities.players.Shield;
import walnoot.rtsgame.map.entities.players.Soldier;
import walnoot.rtsgame.map.entities.players.SoldierComponent;
import walnoot.rtsgame.map.entities.players.Sword;
import walnoot.rtsgame.map.entities.players.Weapon;
import walnoot.rtsgame.map.entities.players.professions.Farmer;
import walnoot.rtsgame.map.entities.players.professions.Founder;
import walnoot.rtsgame.map.entities.players.professions.Hunter;
import walnoot.rtsgame.map.entities.players.professions.LumberJacker;
import walnoot.rtsgame.map.entities.players.professions.Miner;
import walnoot.rtsgame.map.entities.players.professions.Profession;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.map.structures.natural.GoldMine;
import walnoot.rtsgame.map.structures.natural.IronMine;
import walnoot.rtsgame.map.structures.natural.StoneMine;
import walnoot.rtsgame.map.structures.natural.TreeStructure;
import walnoot.rtsgame.map.structures.nonnatural.BaseOfOperations;
import walnoot.rtsgame.map.structures.nonnatural.CampFireStructure;
import walnoot.rtsgame.map.structures.nonnatural.Farm;
import walnoot.rtsgame.map.structures.nonnatural.IronSmelter;
import walnoot.rtsgame.map.structures.nonnatural.SchoolI;
import walnoot.rtsgame.map.structures.nonnatural.Tent;
import walnoot.rtsgame.map.structures.nonnatural.warrelated.Barracks;
import walnoot.rtsgame.map.structures.nonnatural.warrelated.StoneDefenseTower;
import walnoot.rtsgame.map.structures.nonnatural.warrelated.WoodenGate;
import walnoot.rtsgame.map.structures.nonnatural.warrelated.WoodenWall;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.screen.GameScreen;

/**
 * Class met handige static methods en waarden
 */

public class Util {
	public static final Random RANDOM = new Random(2222);
	public static final NameGenerator NAME_GEN = new NameGenerator();
	
	public static int getDistance(Entity a, Entity b){
		int x1 = a.getxPos();
		int y1 = a.getyPos();
		int x2 = b.getxPos();
		int y2 = b.getyPos();
		return getDistance(x1, y1, x2, y2);
	}
	
	public static int getDirectionInDegrees(Entity a, Entity b, boolean fromTop){
		int screenXA = a.getScreenX() + Tile.getWidth() / 2;
		int screenYA = a.getScreenY() + Tile.getHeight() / 2;
		int screenXB = b.getScreenX() + Tile.getWidth() / 2;
		int screenYB = b.getScreenY() + Tile.getHeight() / 2;
		
		int x = - screenXA + screenXB;
		int y = screenYA - screenYB;

		if(x == 0 && y < 0){
			return 180;
		}
		int reminder = 0;
		if(x >= 0 && y <= 0){
			reminder = 90;
			int temp = y;
			y = x;
			x = -temp;
		}else if(x < 0 && y <= 0){
			reminder = 180;
			int temp = x;
			x = abs(y);
			y = abs(temp);
		}else if(x < 0 && y >= 0){
			reminder = 270;
			int temp = y;
			y = -x;
			x = -temp;
		}
		
		x = abs(x);
		y = abs(y);
		
		return (int) (reminder + Math.toDegrees(Math.atan2(y , x)));
		
	}
	
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
				lastSpace = i;
				if(s == "") continue;
				result.add(s);
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
	
	public static int parseInt(final String s){
	    if (s == null )
	        throw new NumberFormatException("Null string");
	    else if (s == ""){
	    	System.out.println("string is '' (empty) ");
	    	new Exception().printStackTrace();
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
	
	public static Entity getEntity(Map map, String entity, GameScreen screen){
		Entity e = getEntity(entity, map, screen);
		return e;
	}
	
	public static Entity getEntity(Map map, GameScreen screen, int ID, int xPos, int yPos, int health, int[] extraInfoOne, int front){
		int extraInfo = 0;
		if(extraInfoOne.length == 2){
			extraInfo = extraInfoOne[1];
		}
		Entity e = null;
		switch(ID){
		case 300:
			e = new GoldMine(map, screen, xPos, yPos, extraInfo, health);
			break;
		case 301:
			e = new IronMine(map, screen, xPos, yPos, extraInfo, health);
			break;
		case 200:
			e = new CampFireStructure(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			break;
		case 201:
			e = new Tent(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST, extraInfo);
			break;
		case 202:
			e = new TreeStructure(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			break;
		case 203:
			e = new BaseOfOperations(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			break;
		case 206:
			e = new StoneMine(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			break;
		case 209:
			e = new Farm(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			break;
		case 210:
			e = new SchoolI(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			break;
		case 211:
			e = new Barracks(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			break;
		case 212:
			e = new StoneDefenseTower(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			if(extraInfoOne.length > 2){
				int[] extraInfoSoldier = new int[extraInfoOne.length - 5];
				for(int i = 5; i < extraInfoOne.length; i++){
					extraInfoSoldier[i - 5] = extraInfoOne[i];
				}
				Entity en = getEntity(map, screen, extraInfoOne[1], extraInfoOne[2], extraInfoOne[3], extraInfoOne[4], extraInfoSoldier, extraInfoOne[5], extraInfoOne[6]);
				System.out.println(en);
				((StoneDefenseTower)e).setGuard(en);
			}
			break;
		case 213:
			e = new WoodenWall(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			break;
		case 214:
			e = new WoodenGate(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			break;
		case 215:
			e = new IronSmelter(map, screen,  xPos, yPos, health, Direction.SOUTH_WEST);
			break;
		case 100:
			e = new SnakeEntity(map, screen,  xPos, yPos, health);
			break;
		case 101:
			e = new SheepEntity(map, screen,  xPos, yPos, health);
			break;
		case 102:
			e = new PlayerEntity(map, screen,  xPos, yPos, null);
			((PlayerEntity)e).setProfessionFromHost(getProfession(extraInfo, (PlayerEntity)e));
			e.setHealth(health);
			break;
		case 103:
			e = new DeerEntity(map, screen,  xPos, yPos, health);
			break;
		case 107:
			e = new Soldier(map, screen, xPos, yPos, null);
			for(SoldierComponent c: getComponents(extraInfoOne, (Soldier) e)){
				((Soldier)e).addSoldierComponent(c);
			}
			break;
		default:
			System.out.println("Entity not Found: " + ID);
		}
		if(e instanceof BasicStructure && front == 1){
			((BasicStructure)e).setFront(Direction.SOUTH_EAST);
		}
		return e;
	}
	
	public static ArrayList<SoldierComponent> getComponents(int[] data, Soldier owner){
		ArrayList<SoldierComponent> components = new ArrayList<>();
		for(int i = 1; i < data.length; i++){
			components.add(getSoldierComponent(data[i], owner));
		}
		return components;
	}
	
	public static SoldierComponent getSoldierComponent(int ID, Soldier owner){
		System.out.println(ID);
		SoldierComponent c = null;
		switch(ID){
		case 600:
			c = new AlertComponent(owner, ID % 100);
			break;
		case 631:
			c = new Shield(owner, 1, 100);
			break;
		case 632:
			c = new Sword(owner, 1);
			break;
		case 633:
			c = new Bow(owner);
			break;
		}
		return c;
	}
	
	public static Profession getProfession(int ID, PlayerEntity player){
		//System.out.println(ID);
		switch(ID){
		case(400):
			return new LumberJacker(player);
		case(401):
			return new Miner(player, 1);
		case(402):
			return new Hunter(player);
		case(403):
			return new Founder(player);
		case(404):
			return new Farmer(player);
		case(405):
			return new Miner(player, 2);
		case(406):
			return new Miner(player, 3);
		default:
			return null;
		}
	}
	
	public static Entity getEntity(Map map, GameScreen screen, int ID, int xPos, int yPos, int health, int[] extraInfoOne, int uniqeNumber, int front){
		Entity e = getEntity(map, screen, ID, xPos, yPos, health, extraInfoOne, front);
		e.uniqueNumber = uniqeNumber;
		return e;
	}
	
	public static Entity getEntity(String entity, Map map, GameScreen screen){
		//System.out.println(entity);
		ArrayList<String> entityInstrings = splitString(entity);
		int ID = parseInt(entityInstrings.get(0));
		int xPos = parseInt(entityInstrings.get(1));
		int yPos = parseInt(entityInstrings.get(2));
		int health = parseInt(entityInstrings.get(3));
		int number = parseInt(entityInstrings.get(4));
		int[] extraInfoOne = new int[number + 1];
		extraInfoOne[0] = number;
		int n = 5;
		for(int i = 1; i < number + 1; i++){
			extraInfoOne[i] = parseInt(entityInstrings.get(n));
			n++;
		}
		int uniqueNumber = parseInt(entityInstrings.get(n++));
		int front = parseInt(entityInstrings.get(n++));
		
		
		return getEntity(map, screen, ID, xPos, yPos, health, extraInfoOne, uniqueNumber, front);
	}
	
	public static int getProfessionID(Profession prof){
		System.out.println(prof);
		if (prof instanceof LumberJacker){
			return 400;
		}
		if(prof instanceof Miner){
			if(((Miner) prof).level == 1){
				return 401;
			}
			if(((Miner) prof).level == 2){
				return 405;
			}
		}
		if(prof instanceof Hunter){
			return 402; 
		}
		if(prof instanceof Founder){
			return 403;
		}
		if(prof instanceof Farmer){
			return 404;
		}
		return 0;
	}
	
	public static Weapon getWeapon(int ID, Soldier owner){
		switch(ID){
		case 501:
			return new Sword(owner, 1);
		case 500:
			return new Bow(owner);
		default:
			return null;
		}
	}
}	
	